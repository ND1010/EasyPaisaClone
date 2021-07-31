package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.MobileRechargePlansModel
import com.app.bhaskar.easypaisa.request_model.MobileRechargePlanRequest
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface MobileRechargePlansPresenter : BasePresenter {

    fun getPlans(req: MobileRechargePlanRequest)
    interface MobileRechargePlansView : BaseView {
        fun doRetriveModel(): MobileRechargePlansModel
        fun onMobilePlan()
    }
}