package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.DepositeActivityDomain
import com.app.bhaskar.easypaisa.request_model.DepositeGetOtpRequest
import com.app.bhaskar.easypaisa.request_model.DepositeVerifyOtpRequest
import com.pa.baseframework.baseview.BaseViewModel

class DepositeActivityModel : BaseViewModel {

    private var domain: DepositeActivityDomain
    private var otpRequest = DepositeGetOtpRequest()
    private var otpVerifyRequest = DepositeVerifyOtpRequest()

    constructor(mContext: Context) : super(mContext) {
        domain = DepositeActivityDomain()
    }

    fun getOtpRequest():DepositeGetOtpRequest{
        return otpRequest
    }
    fun getVerifyOtpRequest(): DepositeVerifyOtpRequest {
        return otpVerifyRequest
    }
}