package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.DepositeActivityModel
import com.app.bhaskar.easypaisa.response_model.DepositeOtpVerifyResponse
import com.app.bhaskar.easypaisa.response_model.RegOtpResponse
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface CashDepositePresenter : BasePresenter {
    interface CashDepositeView : BaseView {
        fun doRetriveModel(): DepositeActivityModel
        fun onOtpSent(it: RegOtpResponse)
        fun onOtpVerified(it: DepositeOtpVerifyResponse)
        fun onCashDeposit(it: RegOtpResponse)
    }
    fun doGetOtp()
    fun doVerifyOtp()
    fun doCashDeposit()
}