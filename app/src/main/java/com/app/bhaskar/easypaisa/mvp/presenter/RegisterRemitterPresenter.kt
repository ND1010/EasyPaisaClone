package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.RegisterRemitterActivityModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface RegisterRemitterPresenter : BasePresenter {
    fun goRegisterRemiter()
    fun sendOtp()
    fun doVerifyRemitter()

    interface RegisterRemitterView : BaseView {
        fun doRetriveModel(): RegisterRemitterActivityModel
        fun onRegisterRemiiter()
        fun onGenerateOtp()
        fun onVerifyRemitter()
    }
}