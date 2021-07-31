package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.AepsActivityDomain
import com.app.bhaskar.easypaisa.mvp.domain.LoginActivityDomain
import com.app.bhaskar.easypaisa.request_model.*
import com.pa.baseframework.baseview.BaseViewModel

class AepsActivityModel : BaseViewModel {

    private var domain: AepsActivityDomain
    private var aepsRequest = AepsRequest()
    private var aepstxnRequest = AepsTransactionRequest()
    private var yesValidateUserRequest = YesValidateUserRequest()

    constructor(mContext: Context) : super(mContext) {
        domain = AepsActivityDomain()
        aepstxnRequest = AepsTransactionRequest()
        yesValidateUserRequest = YesValidateUserRequest()
    }

    fun getValidateUserReq():YesValidateUserRequest{
        return yesValidateUserRequest
    }

    fun getAepsRequest(): AepsRequest {
        return aepsRequest
    }

    fun getAepsTransactionRequest(): AepsTransactionRequest {
        return aepstxnRequest
    }

    fun getLoginDomain(): AepsActivityDomain {
        return domain
    }
}