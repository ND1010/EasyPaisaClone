package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.RegistrationActivityDomain
import com.app.bhaskar.easypaisa.repositories.RegistrationRequest
import com.app.bhaskar.easypaisa.request_model.AepsTransactionRequest
import com.app.bhaskar.easypaisa.request_model.RegisterOtpRequest
import com.pa.baseframework.baseview.BaseViewModel

class RegistrationActivityModel : BaseViewModel {

    private var domain: RegistrationActivityDomain
    private var registrationRequest = RegistrationRequest()
    private var aepstxnRequest = AepsTransactionRequest()
    private var otpRequest = RegisterOtpRequest()

    constructor(mContext: Context) : super(mContext) {
        domain = RegistrationActivityDomain()
        aepstxnRequest = AepsTransactionRequest()
    }

    fun getRegistrationRequest(): RegistrationRequest {
        return registrationRequest
    }

    fun getOtpRequest():RegisterOtpRequest{
        return otpRequest
    }

    fun getLoginDomain(): RegistrationActivityDomain {
        return domain
    }
}