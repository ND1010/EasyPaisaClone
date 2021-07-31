package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.LoginResponse
import com.app.bhaskar.easypaisa.response_model.PasswordResetTokenResponse

data class LoginActivityDomain(var loginResponse: LoginResponse, var passwordResetTokenResponse: PasswordResetTokenResponse) {
    constructor():this(LoginResponse(),PasswordResetTokenResponse())
}