package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.UtiRegistrationModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface UtiRegistrationPresenter : BasePresenter {
    fun registerAgentUti()

    interface UtiRegistrationView : BaseView {
        fun doRetriveModel(): UtiRegistrationModel
        fun onUtiRegistered()
    }
}