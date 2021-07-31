package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.BaseResponse
import com.app.bhaskar.easypaisa.response_model.OnlinePaymentInitResponse

data class LoadWalletUpiDomain(var loadWalletUpiResponse: LoadWalletUpiResponse,var onlinePaymentInitResponse: OnlinePaymentInitResponse,var baseResponse: BaseResponse) {
    constructor():this(LoadWalletUpiResponse(),OnlinePaymentInitResponse(),BaseResponse())
}