package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.TransactionFragmentModel
import com.app.bhaskar.easypaisa.response_model.TransactionFilterDataResponse
import com.app.bhaskar.easypaisa.response_model.TransactionResponse
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView
import java.util.ArrayList


interface TransactionFragmentPresenter : BasePresenter {
    fun getFilterList(): ArrayList<TransactionFilterDataResponse.History>
    fun getTransactionHistory()

    interface TransactionFragmentView : BaseView {
        fun doRetriveModel(): TransactionFragmentModel
        fun onTxnHistory()
    }

}