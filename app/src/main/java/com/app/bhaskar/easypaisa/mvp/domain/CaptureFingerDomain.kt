package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.BaseResponse
import com.app.bhaskar.easypaisa.response_model.FingPayAepsTxnResponse
import com.app.bhaskar.easypaisa.response_model.FingpayMiniStatementResponse
import com.app.bhaskar.easypaisa.response_model.SednOtpEkycResponse

data class CaptureFingerDomain(var fingPayAepsTxnResponse: FingPayAepsTxnResponse,var fingpayMiniStatementResponse: FingpayMiniStatementResponse,var sendOtpEkycResponse: SednOtpEkycResponse,var baseResponse: BaseResponse) {
    constructor():this(FingPayAepsTxnResponse(),FingpayMiniStatementResponse(),SednOtpEkycResponse(),BaseResponse())
}