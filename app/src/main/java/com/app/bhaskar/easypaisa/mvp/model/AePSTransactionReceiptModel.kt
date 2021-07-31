package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.AepsTransactionDomain
import com.pa.baseframework.baseview.BaseViewModel

class AePSTransactionReceiptModel : BaseViewModel {

    private var domain: AepsTransactionDomain

    constructor(mContext: Context) : super(mContext) {
        domain = AepsTransactionDomain()
    }


    fun getLoginDomain(): AepsTransactionDomain {
        return domain
    }
}