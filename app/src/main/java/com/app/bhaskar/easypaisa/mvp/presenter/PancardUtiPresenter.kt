package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.PancardUtiActivityModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface PancardUtiPresenter : BasePresenter {
    fun doGetPanStatusForAgent()
    fun doGetTokenForPan()
    fun resetPSAID()
    fun doOrderDevice()
    fun docallapiMatmOrderOnline()
    fun doUpdateMatmOrder()

    interface PancardUtiView : BaseView {
        fun doRetriveModel(): PancardUtiActivityModel
        fun pancardData()
        fun onDeviceOrdered()
        fun panTokenPurchased()
        fun resetPsaId()
        fun onMatmDeviceOnlinePaymentUpdate()
    }
}