package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.AepsActivityDomain
import com.app.bhaskar.easypaisa.mvp.domain.DmtTransactionDomain
import com.app.bhaskar.easypaisa.mvp.domain.LoginActivityDomain
import com.app.bhaskar.easypaisa.request_model.*
import com.pa.baseframework.baseview.BaseViewModel

class DmtTransactionActivityModel : BaseViewModel {

    private var domain: DmtTransactionDomain
    private var requestDtmTransation : DmtTransferRequest

    constructor(mContext: Context) : super(mContext) {
        domain = DmtTransactionDomain()
        requestDtmTransation = DmtTransferRequest()
    }

    fun getDomain(): DmtTransactionDomain {
        return domain
    }

    fun getDmtTransactionRequest():DmtTransferRequest{
       return requestDtmTransation
    }
}