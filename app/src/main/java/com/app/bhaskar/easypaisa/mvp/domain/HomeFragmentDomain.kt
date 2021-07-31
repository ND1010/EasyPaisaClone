package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.request_model.LogoutResponse
import com.app.bhaskar.easypaisa.response_model.*

data class HomeFragmentDomain(var generateOtpResponse: GenerateOtpResponse, var walletBalanceResponse: WalletBalanceResponse,var userRequiredDataResponse: UserRequiredDataResponse,var searchRemitterResponse: SearchRemitterResponse,var verifyRemitterResponse: VerifyRemitterResponse,var logoutResponse: LogoutResponse) {
    constructor():this(GenerateOtpResponse(),WalletBalanceResponse(),UserRequiredDataResponse(),SearchRemitterResponse(),VerifyRemitterResponse(),LogoutResponse())
}