package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.AepsTnxDataResponse
import com.app.bhaskar.easypaisa.response_model.YesValidateUserResponse

data class AepsActivityDomain(var aepsTxnDataResponse: AepsTnxDataResponse,var yesValidateUserResponse: YesValidateUserResponse) {
    constructor():this(AepsTnxDataResponse(),YesValidateUserResponse())
}