package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.LoadWalletActivityModel
import com.app.bhaskar.easypaisa.request_model.WalletLoadRequest
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface LoadWalletPresenter : BasePresenter {
    fun getRequiredData()
    fun openDatePicker()
    fun loadWalletRequest(walletLoadReq: WalletLoadRequest)

    interface LoadWalletView : BaseView {
        fun doRetriveModel(): LoadWalletActivityModel
        fun onWalletLoadData()
        fun onWalletLoadSuccess()
    }
}