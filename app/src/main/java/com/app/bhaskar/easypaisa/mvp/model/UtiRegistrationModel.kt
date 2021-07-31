package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.UtiRegistrationActivityDomain
import com.app.bhaskar.easypaisa.request_model.UtiRegisterRequest
import com.pa.baseframework.baseview.BaseViewModel

class UtiRegistrationModel : BaseViewModel {

    private var domain: UtiRegistrationActivityDomain
    private var utiRegisterRequest: UtiRegisterRequest

    constructor(mContext: Context) : super(mContext) {
        domain = UtiRegistrationActivityDomain()
        utiRegisterRequest = UtiRegisterRequest()
    }

    fun getDomain(): UtiRegistrationActivityDomain {
        return domain
    }

    fun getUtiRegisterRequest():UtiRegisterRequest{
        return utiRegisterRequest
    }
}