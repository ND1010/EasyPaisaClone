package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.EelectricityStateResponse
import com.app.bhaskar.easypaisa.response_model.ElectricityBillPaymentResponse
import com.app.bhaskar.easypaisa.response_model.ElectricityBoardResponse
import com.app.bhaskar.easypaisa.response_model.FetchElBillDetailResponse

data class ElectricityBillPaymentDomain(
    var electricityStateResponse: EelectricityStateResponse,
    var electricityBoardResponse: ElectricityBoardResponse,
    var fetchElBillDetailResponse: FetchElBillDetailResponse,
    var electricityBillPaymentResponse: ElectricityBillPaymentResponse) {
    constructor() : this(
        EelectricityStateResponse(),
        ElectricityBoardResponse(),
        FetchElBillDetailResponse(),
        ElectricityBillPaymentResponse()
    )
}