package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.ChangePassActivityDomain
import com.app.bhaskar.easypaisa.request_model.AepsRequest
import com.app.bhaskar.easypaisa.request_model.ChangePasswordRequest
import com.pa.baseframework.baseview.BaseViewModel

class ChangePassActivityModel: BaseViewModel {

    private var domain : ChangePassActivityDomain
    private var changePasswordRequest = ChangePasswordRequest()

    constructor(mContext: Context):super(mContext){
        domain = ChangePassActivityDomain()
    }

    fun getChangePassRequest():ChangePasswordRequest{
        return changePasswordRequest
    }

    fun getDomain():ChangePassActivityDomain{
        return domain
    }
}