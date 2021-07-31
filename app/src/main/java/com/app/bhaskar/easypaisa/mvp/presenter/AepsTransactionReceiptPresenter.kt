package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.AePSTransactionReceiptModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface AepsTransactionReceiptPresenter : BasePresenter {

    interface AepsTransactionReceiptView : BaseView {
        fun doRetriveModel(): AePSTransactionReceiptModel
    }
}