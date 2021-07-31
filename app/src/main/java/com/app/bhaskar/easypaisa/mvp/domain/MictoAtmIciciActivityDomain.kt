package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.AepsTnxDataResponse
import com.app.bhaskar.easypaisa.response_model.MicroAtmInitTransactionResponse
import com.app.bhaskar.easypaisa.response_model.MicroAtmUpdateResponse

data class MictoAtmIciciActivityDomain(var aepsTxnDataResponse: AepsTnxDataResponse,var microAtmUpdateResponse: MicroAtmUpdateResponse,var microAtmInitTransactionResponse: MicroAtmInitTransactionResponse) {
    constructor():this(AepsTnxDataResponse(),MicroAtmUpdateResponse(),MicroAtmInitTransactionResponse())
}