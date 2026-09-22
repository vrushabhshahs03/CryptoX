package com.example.cryptox.domain.use_case.auth

import android.app.Activity
import com.example.cryptox.common.Resource
import com.example.cryptox.domain.repository.AuthRepository
import javax.inject.Inject

class SendPhoneOTPUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(activity: Activity, phoneNumber: String): Resource<String> {
        return repository.sendPhoneOTP(
            activity = activity,
            phoneNumber = phoneNumber,
        )
    }
}