package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.AepsTnxDataResponse

    data class DepositeActivityDomain(var aepsTxnDataResponse: AepsTnxDataResponse) {
    constructor():this(AepsTnxDataResponse())
}