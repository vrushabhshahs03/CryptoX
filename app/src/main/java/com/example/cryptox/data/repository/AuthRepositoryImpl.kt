package com.example.cryptox.data.repository

import android.R.attr.phoneNumber
import android.app.Activity
import android.net.Uri
import com.example.cryptox.common.Resource
import com.example.cryptox.domain.model.AuthUser
import com.example.cryptox.domain.model.toAuthUser
import com.example.cryptox.domain.repository.AuthRepository
import com.example.cryptox.presentation.auth.common.AuthProvider
import com.google.firebase.FirebaseException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
): AuthRepository {
    override fun signUp(
        email: String,
        password: String,
        name: String,
    ): Flow<Resource<AuthUser>> =
        flow {
            emit(Resource.Loading())
            try {
                firebaseAuth.createUserWithEmailAndPassword(
                    email,
                    password
                ).await()
                val firebaseUser = firebaseAuth.currentUser
                if(firebaseUser != null) {
                    firebaseUser.updateProfile(userProfileChangeRequest {
                        displayName = name
                    }).await()
                    firebaseUser.sendEmailVerification().await()
                    val authUser = firebaseUser.toAuthUser()
                    //firebaseAuth.signOut()
                    emit(Resource.Success(authUser))
                } else {
                    emit(Resource.Error("An error occurred!"))
                }
            } catch(e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
            }
        }

    override fun login(
        email: String,
        password: String,
    ): Flow<Resource<AuthUser>> = flow {
        emit(Resource.Loading())
        try {
            firebaseAuth.signInWithEmailAndPassword(
                email,
                password
            ).await()
            val firebaseUser = firebaseAuth.currentUser

            if(firebaseUser != null) {
                if(firebaseUser.isEmailVerified) {
                    emit(Resource.Success(firebaseUser.toAuthUser()))
                } else {
                    emit(Resource.Error("Complete email verification to continue!"))
                    firebaseAuth.signOut()
                }
            } else {
                emit(Resource.Error("An error has occurred!"))
            }
        } catch(e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error has occurred!"))
        }
    }

    override fun signUpWithGoogle(idToken: String): Flow<Resource<AuthUser>> = flow {
        emit(Resource.Loading())
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val user = result.user
            if(user != null) {
                val authUser = user.toAuthUser()
                emit(Resource.Success(authUser))
            } else {
                emit(Resource.Error("An error occurred!"))
            }
        } catch(e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error has occurred!"))
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun deleteAccount(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val user = firebaseAuth.currentUser
            ?: return@flow emit(Resource.Error("User not logged in"))
        try {
            user.delete().await()
            emit(Resource.Success(Unit))
        } catch(e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error has occurred!"))
        }
    }.flowOn(Dispatchers.IO)

    override fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        if(newPassword != confirmPassword) {
            emit(Resource.Error("Passwords don't match!"))
            return@flow
        }
        val user = firebaseAuth.currentUser

        if(user == null) {
            emit(Resource.Error("User not logged in!"))
            return@flow
        }
        val email = user.email

        if(email.isNullOrEmpty()) {
            emit(Resource.Error("Invalid user email!"))
            return@flow
        }

        try {
            val credential = EmailAuthProvider.getCredential(
                email,
                currentPassword
            )

            user.reauthenticate(credential).await()


            user.updatePassword(newPassword).await()

            emit(Resource.Success(Unit))
        } catch(e: Exception) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "An error occurred!"
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    override fun getCurrentUser(): AuthUser? {
        return firebaseAuth.currentUser?.toAuthUser()
    }

    override suspend fun updateCurrentUser(name: String?, imageUri: Uri?): Resource<Unit> {
        try {
            val firebaseUser = firebaseAuth.currentUser
            firebaseUser?.updateProfile(userProfileChangeRequest {
                displayName = name
                photoUri = imageUri
            })?.await()
            return Resource.Success(Unit)
        } catch(e: Exception) {
            return Resource.Error(message = e.localizedMessage ?: "An unexpected error has occurred!")
        }
    }

    override fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun forgotPassword(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
    }

    override fun getProviders(): Resource<List<AuthProvider>> {
        val user = firebaseAuth.currentUser
        if(user != null) {
            val providers = user.providerData.mapNotNull {
                when(it.providerId) {
                    "password" -> AuthProvider.EmailPassword
                    "google.com" -> AuthProvider.Google
                    "facebook.com" -> AuthProvider.Facebook
                    "apple.com" -> AuthProvider.Apple
                    "phone" -> AuthProvider.Phone
                    "anonymous" -> AuthProvider.Anonymous
                    else -> AuthProvider.Unknown
                }
            }
            return Resource.Success(data = providers)
        } else {
            return Resource.Error(message = "User not logged in!")
        }
    }

    override suspend fun sendPhoneOTP(
        activity: Activity,
        phoneNumber: String,
    ): Resource<String> =
        suspendCancellableCoroutine { continuation ->
            val callbacks =
                object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(
                        credential: PhoneAuthCredential,
                    ) {
                    }

                    override fun onVerificationFailed(
                        exception: FirebaseException,
                    ) {
                        continuation.resume(
                            value = Resource.Error(
                                message = exception.localizedMessage ?: "An unexpected error has occurred!"
                            )
                        )
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken,
                    ) {
                        continuation.resume(
                            Resource.Success(
                                verificationId
                            )
                        )
                    }
                }
            val options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(activity)
                    .setCallbacks(callbacks)
                    .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
        }

    override suspend fun verifyPhoneOTP(
        verificationId: String,
        otp: String,
    ): Resource<Unit> =
        suspendCancellableCoroutine { continuation ->
            val credential = PhoneAuthProvider.getCredential(
                verificationId,
                otp
            )

            if(firebaseAuth.currentUser == null) {
                continuation.resume(
                    Resource.Error(
                        "No authenticated user found"
                    )
                )
                return@suspendCancellableCoroutine
            }

            firebaseAuth.currentUser?.let { user ->
                if(user.phoneNumber.isNullOrBlank()) {
                    user.linkWithCredential(credential)
                        .addOnSuccessListener {
                            continuation.resume(
                                Resource.Success(Unit)
                            )
                        }
                        .addOnFailureListener { exception ->
                            continuation.resume(
                                Resource.Error(
                                    exception.message ?: "Verification failed"
                                )
                            )
                        }
                } else {
                    user.updatePhoneNumber(credential)
                        .addOnSuccessListener {
                            continuation.resume(
                                Resource.Success(Unit)
                            )
                        }
                        .addOnFailureListener { exception ->
                            continuation.resume(
                                Resource.Error(
                                    exception.message ?: "Verification failed"
                                )
                            )
                        }
                }
            }
        }
}