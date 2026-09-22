package com.example.cryptox.domain.repository

import android.app.Activity
import android.net.Uri
import com.example.cryptox.common.Resource
import com.example.cryptox.domain.model.AuthUser
import com.example.cryptox.presentation.auth.common.AuthProvider
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signUp(
        email: String,
        password: String,
        name: String,
    ): Flow<Resource<AuthUser>>

    fun login(
        email: String,
        password: String,
    ): Flow<Resource<AuthUser>>

    fun signUpWithGoogle(
        tokenId: String,
    ): Flow<Resource<AuthUser>>

    fun logout()
    fun deleteAccount(): Flow<Resource<Unit>>
    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String): Flow<Resource<Unit>>
    fun getCurrentUser(): AuthUser?
    fun getCurrentUserId(): String?
    fun isUserLoggedIn(): Boolean
    fun forgotPassword(email: String)
    fun getProviders(): Resource<List<AuthProvider>>
    suspend fun sendPhoneOTP(activity: Activity, phoneNumber: String): Resource<String>
    suspend fun verifyPhoneOTP(verificationId: String, otp: String): Resource<Unit>
    suspend fun updateCurrentUser(name: String?, imageUri: Uri?): Resource<Unit>
}