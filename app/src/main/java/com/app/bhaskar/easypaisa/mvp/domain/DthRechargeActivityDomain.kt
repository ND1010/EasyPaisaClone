package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.ElectricityBillPaymentResponse
import com.app.bhaskar.easypaisa.response_model.RechargeProviderResponse

data class DthRechargeActivityDomain(var providerResponse: RechargeProviderResponse,var electricityBillPaymentResponse: ElectricityBillPaymentResponse) {
    constructor():this(RechargeProviderResponse(),ElectricityBillPaymentResponse())
}