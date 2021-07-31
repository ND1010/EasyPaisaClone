package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.TransactionFragmentDomain
import com.app.bhaskar.easypaisa.request_model.TransactionHistoryRequest
import com.pa.baseframework.baseview.BaseViewModel

class TransactionFragmentModel: BaseViewModel {

    private var domain : TransactionFragmentDomain
    private var txnRequest : TransactionHistoryRequest

    constructor(mContext: Context):super(mContext){
        domain = TransactionFragmentDomain()
        txnRequest = TransactionHistoryRequest()
    }

    fun getTransactionRequest():TransactionHistoryRequest{
        return txnRequest
    }

    fun getDomain():TransactionFragmentDomain{
        return domain
    }
}