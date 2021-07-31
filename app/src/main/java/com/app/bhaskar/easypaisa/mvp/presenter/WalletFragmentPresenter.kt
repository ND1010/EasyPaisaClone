package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.WalletFragmentModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface WalletFragmentPresenter : BasePresenter {
    fun getWalletBalance()

    interface WalletView : BaseView {
        fun doRetriveModel(): WalletFragmentModel
        fun onWalletBalance()
    }
}