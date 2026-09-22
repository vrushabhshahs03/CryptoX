package com.example.cryptox.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.example.cryptox.data.auth.GoogleAuthService
import com.example.cryptox.data.repository.AuthRepositoryImpl
import com.example.cryptox.domain.repository.AuthRepository
import com.example.cryptox.domain.use_case.auth.AuthUseCases
import com.example.cryptox.domain.use_case.auth.ChangePasswordUseCase
import com.example.cryptox.domain.use_case.auth.DeleteAccountUseCase
import com.example.cryptox.domain.use_case.auth.ForgotPasswordUseCase
import com.example.cryptox.domain.use_case.auth.GetCurrentUserUseCase
import com.example.cryptox.domain.use_case.auth.GetProvidersUseCase
import com.example.cryptox.domain.use_case.auth.GetUserIdUseCase
import com.example.cryptox.domain.use_case.auth.IsUserLoggedInUseCase
import com.example.cryptox.domain.use_case.auth.LogOutUseCase
import com.example.cryptox.domain.use_case.auth.LoginUseCase
import com.example.cryptox.domain.use_case.auth.SendPhoneOTPUseCase
import com.example.cryptox.domain.use_case.auth.SignUpUseCase
import com.example.cryptox.domain.use_case.auth.SignUpWithGoogleUseCase
import com.example.cryptox.domain.use_case.auth.UpdateCurrentUserUseCase
import com.example.cryptox.domain.use_case.auth.VerifyPhoneOTPUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        impl: AuthRepositoryImpl,
    ): AuthRepository {
        return impl
    }

    @Provides
    fun provideCredentialManager(
        @ApplicationContext context: Context,
    ): CredentialManager {
        return CredentialManager.create(context)
    }

    @Provides
    @Singleton
    fun provideGoogleAuthService(
        credentialManager: CredentialManager,
    ): GoogleAuthService {
        return GoogleAuthService(credentialManager)
    }

    @Provides
    @Singleton
    fun provideAuthUseCases(
        repository: AuthRepository,
    ): AuthUseCases {
        return AuthUseCases(
            signUp = SignUpUseCase(repository),
            signUpWithGoogle = SignUpWithGoogleUseCase(repository),
            login = LoginUseCase(repository),
            logout = LogOutUseCase(repository),
            getUser = GetCurrentUserUseCase(repository),
            getUserId = GetUserIdUseCase(repository),
            isLoggedIn = IsUserLoggedInUseCase(repository),
            forgotPassword = ForgotPasswordUseCase(repository),
            changePassword = ChangePasswordUseCase(repository),
            deleteAccount = DeleteAccountUseCase(repository),
            getProviders = GetProvidersUseCase(repository),
            updateCurrentUser = UpdateCurrentUserUseCase(repository),
            sendPhoneOTP = SendPhoneOTPUseCase(repository),
            verifyPhoneOTP = VerifyPhoneOTPUseCase(repository)
        )
    }
}