package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.MobileRechargeModel
import com.app.bhaskar.easypaisa.mvp.model.MobileRechargePlansModel
import com.app.bhaskar.easypaisa.request_model.MobileRechargeRequest
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface MobileRechargePresenter : BasePresenter {
    fun getProviderList()
    fun getOperatorFromMobile()
    fun doMobileRecharge(mobileRechargeReq: MobileRechargeRequest)

    interface MobileRechargeView : BaseView {
        fun doRetriveModel(): MobileRechargeModel
        fun onMobileOperator()
        fun onMobileOperatorByMobile()
        fun onMobileRechargeDone()
    }
}