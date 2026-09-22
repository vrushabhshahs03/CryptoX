package com.example.cryptox.domain.use_case.auth

import com.example.cryptox.common.Resource
import com.example.cryptox.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyPhoneOTPUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(verificationId: String, otp: String): Resource<Unit> {
        return repository.verifyPhoneOTP(
            verificationId = verificationId,
            otp = otp
        )
    }
}