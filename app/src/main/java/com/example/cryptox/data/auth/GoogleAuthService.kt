package com.example.cryptox.data.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.cryptox.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import javax.inject.Inject

class GoogleAuthService @Inject constructor(
    private val credentialManager: CredentialManager,
) {
    suspend fun signIn(context: Context): String {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .setFilterByAuthorizedAccounts(false)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        val result = credentialManager.getCredential(
            context = context,
            request = request
        )
        val credential = result.credential
        val googleCredential = GoogleIdTokenCredential
            .createFrom(credential.data)

        return googleCredential.idToken
    }
}