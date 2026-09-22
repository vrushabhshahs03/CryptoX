package com.example.cryptox.data.repository


import com.example.cryptox.common.Resource
import com.example.cryptox.data.remote.dto.UserDto
import com.example.cryptox.data.remote.dto.toUser
import com.example.cryptox.domain.model.PortfolioItem
import com.example.cryptox.domain.model.User
import com.example.cryptox.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStore: FirebaseFirestore,
): UserRepository {
    private val usersCollection = firebaseStore.collection("users")
    override fun createUser(user: User): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            delay(2000)
            usersCollection
                .document(user.uid)
                .set(user, SetOptions.merge())
                .await()

            emit(Resource.Success(Unit))
        } catch(e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unknown error occurred!"))
        }
    }

    override fun getUser(): Flow<Resource<User>> = callbackFlow {
        trySend(Resource.Loading())
        val uid = firebaseAuth.currentUser?.uid
        if(uid.isNullOrBlank()) {
            trySend(Resource.Error(message = "User not logged in!"))
            close()
            return@callbackFlow
        }
        val listener = usersCollection.document(uid)
            .addSnapshotListener { snapshot, error ->
                if(error != null) {
                    trySend(
                        Resource.Error(
                            error.localizedMessage
                                ?: "Failed to get user"
                        )
                    )
                    return@addSnapshotListener
                }
                val userDto = snapshot?.toObject(UserDto::class.java)
                if(userDto != null) {
                    trySend(
                        Resource.Success(
                            userDto.toUser()
                        )
                    )
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    override fun addFavorite(symbol: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val uid = firebaseAuth.currentUser?.uid ?: null
        if(uid.isNullOrBlank()) {
            emit(Resource.Error(message = "Login to use this feature"))
            return@flow
        } else {
            try {
                usersCollection.document(uid)
                    .update("favorites.$symbol", true)
                    .await()
                emit(Resource.Success(Unit))
            } catch(e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: "Failed to add coin to favorites!"))
            }
        }
    }

    override fun removeFavorites(symbol: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val uid = firebaseAuth.currentUser?.uid
        if(uid.isNullOrBlank()) {
            emit(Resource.Error(message = "Login to use this feature"))
            return@flow
        } else {
            try {
                usersCollection.document(uid)
                    .update("favorites.$symbol", FieldValue.delete())
                    .await()
                emit(Resource.Success(Unit))
            } catch(e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error has occurred!"))
            }
        }
    }

    override fun addPortfolioItem(portFolioItem: PortfolioItem): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val uid = firebaseAuth.currentUser?.uid
        if(uid.isNullOrBlank()) {
            emit(Resource.Error(message = "Login to use this feature"))
            return@flow
        } else {
            try {
                usersCollection.document(uid)
                    .update("portfolio.${portFolioItem.coinId}", portFolioItem)
                    .await()
                emit(Resource.Success(Unit))
            } catch(e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: "Failed to add coin to portfolio!"))
            }
        }
    }

    override fun removePortfolioItem(symbol: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val uid = firebaseAuth.currentUser?.uid ?: null
        if(uid.isNullOrBlank()) {
            emit(Resource.Error(message = "Login to use this feature"))
            return@flow
        } else {
            try {
                usersCollection.document(uid)
                    .update("portfolio.$symbol", FieldValue.delete())
                    .await()
                emit(Resource.Success(Unit))
            } catch(e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error has occurred!"))
            }
        }
    }

    override suspend fun userExists(uid: String): Boolean {
        return firebaseStore.collection("users")
            .document(uid)
            .get().await().exists()
    }

    override suspend fun deleteUser(uid: String): Boolean {
        try {
            firebaseStore.collection("users")
                .document(uid).delete().await()
            return true
        } catch(e: Exception) {
            return false
        }
    }


}