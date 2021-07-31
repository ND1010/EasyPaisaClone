package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.DthRechargeActivityModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface DthRechargeActivityPresenter : BasePresenter {
    fun getRechargeProvider()
    fun getProviders()
    fun rechargeDth()

    interface DthRechargeView : BaseView {
        fun doRetriveModel(): DthRechargeActivityModel
        fun onProviderResponse()
        fun onDthRecharge()
    }
}