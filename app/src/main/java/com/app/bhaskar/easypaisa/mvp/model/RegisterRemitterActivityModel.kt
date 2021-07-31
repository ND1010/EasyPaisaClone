package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.RegisterRemitterActivityDomain
import com.app.bhaskar.easypaisa.request_model.GenerateOtpRequest
import com.app.bhaskar.easypaisa.request_model.RegisterRemmiterRequest
import com.app.bhaskar.easypaisa.request_model.VerifyRemitterOtpRequest
import com.pa.baseframework.baseview.BaseViewModel

class RegisterRemitterActivityModel : BaseViewModel {

    private var domain: RegisterRemitterActivityDomain
    private var registerRemmiterRequest = RegisterRemmiterRequest()
    private var generateOtpRequest = GenerateOtpRequest()
    private var verifyRemitterOtpRequest = VerifyRemitterOtpRequest()

    constructor(mContext: Context) : super(mContext) {
        domain = RegisterRemitterActivityDomain()
        registerRemmiterRequest = RegisterRemmiterRequest()
        generateOtpRequest = GenerateOtpRequest()
        verifyRemitterOtpRequest = VerifyRemitterOtpRequest()
    }

    fun getRegisterRemitterRequest(): RegisterRemmiterRequest {
        return registerRemmiterRequest
    }
    fun getGenerateOtpReq(): GenerateOtpRequest {
        return generateOtpRequest
    }

    fun getVerifyRemitterReq(): VerifyRemitterOtpRequest {
        return verifyRemitterOtpRequest
    }

    fun getLoginDomain(): RegisterRemitterActivityDomain {
        return domain
    }
}