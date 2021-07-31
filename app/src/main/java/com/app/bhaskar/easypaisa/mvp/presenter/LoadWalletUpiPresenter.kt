package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.domain.LoadWalletUpiResponse
import com.app.bhaskar.easypaisa.mvp.model.LoadWalletUpiActivityModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface LoadWalletUpiPresenter : BasePresenter {
    fun goForLoadWalletUpi()
    fun doLoadWalletRazorpay()
    fun doUpdatePayment()

    interface LoadWalletUpiView : BaseView {
        fun doRetriveModel(): LoadWalletUpiActivityModel
        fun onUpiLoadWallet(it:LoadWalletUpiResponse)
        fun onPaymentStatusUpdate()
    }
}