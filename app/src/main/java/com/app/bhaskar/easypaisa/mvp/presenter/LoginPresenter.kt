package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.LoginActivityModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface LoginPresenter : BasePresenter {
    interface LoginView : BaseView {
        fun doRetriveModel(): LoginActivityModel
        fun onLoginResponse()
        fun onResrtToken()
    }
    fun gotoRegistrationScreen()
    fun gotoForgotPassword()
    fun doLogin()
    fun gotoHomeScreen()
    fun doCallApiForSendPasswordToken()
}