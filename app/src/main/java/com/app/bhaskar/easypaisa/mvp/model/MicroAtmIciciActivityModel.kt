package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.MictoAtmIciciActivityDomain
import com.app.bhaskar.easypaisa.request_model.MicroAtmIciciTransactionRequest
import com.app.bhaskar.easypaisa.request_model.MicroAtmInitTransactionRequest
import com.app.bhaskar.easypaisa.request_model.MicroAtmUpdateRequest
import com.pa.baseframework.baseview.BaseViewModel

class MicroAtmIciciActivityModel : BaseViewModel {

    private var domain: MictoAtmIciciActivityDomain = MictoAtmIciciActivityDomain()
    private var atmrequest = MicroAtmIciciTransactionRequest()
    private var microAtmInitTransactionRequest = MicroAtmInitTransactionRequest()
    private var microAtmUpdateRequest = MicroAtmUpdateRequest()

    constructor(mContext: Context) : super(mContext)

    fun getDomain(): MictoAtmIciciActivityDomain {
        return domain
    }

    fun getAtmRequest():MicroAtmIciciTransactionRequest{
        return atmrequest
    }

    fun getMicroAtmInitTransactionRequest(): MicroAtmInitTransactionRequest {
        return microAtmInitTransactionRequest
    }
    fun getMicroAtmUpdateTransactionRequest(): MicroAtmUpdateRequest {
        return microAtmUpdateRequest
    }
}