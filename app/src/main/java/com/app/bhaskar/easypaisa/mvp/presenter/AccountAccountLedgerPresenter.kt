package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.AccountLedgerActivityModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface AccountAccountLedgerPresenter : BasePresenter {
    fun getAccountTransactionLeder()

    interface AccountAccountLedgerView : BaseView {
        fun doRetriveModel(): AccountLedgerActivityModel
        fun onAccountLeder()
    }
}