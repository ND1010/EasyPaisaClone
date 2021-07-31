package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.RegistrationActivityModel
import com.app.bhaskar.easypaisa.response_model.RegOtpResponse
import com.app.bhaskar.easypaisa.response_model.UserRegistrationResponse
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface RegistrationPresenter : BasePresenter {
    interface RegistrationView : BaseView {
        fun doRetriveModel(): RegistrationActivityModel
        fun onRegistrationDone(it: UserRegistrationResponse)
        fun onOtpSent(it: RegOtpResponse)
    }
    fun doRegisterUser()
    fun doGetOtp()
}