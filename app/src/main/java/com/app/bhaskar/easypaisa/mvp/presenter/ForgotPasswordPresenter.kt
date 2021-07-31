package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.ForgotPasswordActivityModel
import com.app.bhaskar.easypaisa.mvp.model.LoginActivityModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface ForgotPasswordPresenter : BasePresenter {
    interface ForgotPasswordView : BaseView {
        fun doRetriveModel(): ForgotPasswordActivityModel
        fun onPasswordRest()
    }
    fun doUpdatePassword()
}