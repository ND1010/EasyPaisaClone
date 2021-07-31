package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.GenerateOtpResponse
import com.app.bhaskar.easypaisa.response_model.RemitterRegisterResponse
import com.app.bhaskar.easypaisa.response_model.VerifyRemitterResponse

data class RegisterRemitterActivityDomain(var remitterRegisterResponse: RemitterRegisterResponse,var generateOtpResponse: GenerateOtpResponse,var verifyRemitterResponse: VerifyRemitterResponse) {
    constructor():this(RemitterRegisterResponse(),GenerateOtpResponse(),VerifyRemitterResponse())
}