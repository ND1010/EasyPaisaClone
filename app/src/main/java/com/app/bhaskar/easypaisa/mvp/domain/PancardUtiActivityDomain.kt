package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.PanTokenResponse
import com.app.bhaskar.easypaisa.response_model.*

data class PancardUtiActivityDomain(var pancardResponse: AgentPancardResponse,var panTokenResponse: PanTokenResponse,var resetPsaIDResponse: UtiResetPsaIDResponse,var orderDeviceResponse: OrderDeviceResponse,var matmOnlinePaymentResponse: MatmOnlinePaymentResponse,var baseResponse: BaseResponse) {
    constructor():this(AgentPancardResponse(), PanTokenResponse(),UtiResetPsaIDResponse(),OrderDeviceResponse(),MatmOnlinePaymentResponse(),BaseResponse())
}