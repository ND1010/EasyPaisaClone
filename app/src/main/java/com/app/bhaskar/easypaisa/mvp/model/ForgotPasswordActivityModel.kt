package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.UpdatePasswordActivityDomain
import com.app.bhaskar.easypaisa.request_model.UpdatePasswordRequest
import com.pa.baseframework.baseview.BaseViewModel

class ForgotPasswordActivityModel : BaseViewModel {
    private var updatePassRequest: UpdatePasswordRequest
    private var domain: UpdatePasswordActivityDomain

    constructor(mContext: Context) : super(mContext) {
        updatePassRequest = UpdatePasswordRequest()
        domain = UpdatePasswordActivityDomain()
    }

    fun getUpdatePasswordRequest(): UpdatePasswordRequest {
        return updatePassRequest
    }

    fun getUpdatePasswordActivityDomain(): UpdatePasswordActivityDomain {
        return domain
    }
}