package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.request_model.DmtTransactionResponse
import com.app.bhaskar.easypaisa.response_model.AepsTnxDataResponse

data class DmtTransactionDomain(var aepsTxnDataResponse: AepsTnxDataResponse,var dmtTransactionResponse: DmtTransactionResponse) {
    constructor():this(AepsTnxDataResponse(),DmtTransactionResponse())
}