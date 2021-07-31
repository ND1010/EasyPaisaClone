package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.WalletLoadRequestResponse
import com.app.bhaskar.easypaisa.response_model.WalletRequiredDataResponse

data class SettlementRequestActivityDomain(var walletRequiredDataResponse: WalletRequiredDataResponse,var walletLoadRequestResponse: WalletLoadRequestResponse) {
    constructor():this(WalletRequiredDataResponse(),WalletLoadRequestResponse())
}