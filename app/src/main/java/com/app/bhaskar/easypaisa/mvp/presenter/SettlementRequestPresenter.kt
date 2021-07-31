package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.SettlementRequestActivityModel
import com.app.bhaskar.easypaisa.request_model.MoveToBankWalletRequest
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface SettlementRequestPresenter : BasePresenter {
    fun getRequiredData()
    fun requestForMoveWalletBank(requst: MoveToBankWalletRequest)

    interface SettlementRequestView : BaseView {
        fun doRetriveModel(): SettlementRequestActivityModel
        fun onWalletLoadData()
        fun onMoveWalletOrBank()
    }
}