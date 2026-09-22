package com.example.cryptox.domain.use_case.auth

data class AuthUseCases(
    val signUp: SignUpUseCase,
    val signUpWithGoogle: SignUpWithGoogleUseCase,
    val login: LoginUseCase,
    val logout: LogOutUseCase,
    val getUser: GetCurrentUserUseCase,
    val getUserId: GetUserIdUseCase,
    val isLoggedIn: IsUserLoggedInUseCase,
    val forgotPassword: ForgotPasswordUseCase,
    val changePassword: ChangePasswordUseCase,
    val deleteAccount: DeleteAccountUseCase,
    val getProviders: GetProvidersUseCase,
    val updateCurrentUser: UpdateCurrentUserUseCase,
    val sendPhoneOTP: SendPhoneOTPUseCase,
    val verifyPhoneOTP: VerifyPhoneOTPUseCase
)