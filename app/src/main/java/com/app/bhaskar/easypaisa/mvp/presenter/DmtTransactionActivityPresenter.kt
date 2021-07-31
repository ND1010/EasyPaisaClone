package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.DmtTransactionActivityModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface DmtTransactionActivityPresenter : BasePresenter {
    fun doTransferAmount()

    interface DmtTransactionView : BaseView {
        fun doRetriveModel(): DmtTransactionActivityModel
        fun onDmtTransactionDone()
    }
}