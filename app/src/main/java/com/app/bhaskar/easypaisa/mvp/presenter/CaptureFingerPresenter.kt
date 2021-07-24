package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.mvp.model.CaptureFingerModel
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView


interface CaptureFingerPresenter : BasePresenter {
    fun apiCallforAepsFinpayTxn()
    fun scanFingerprint(devicePackageName: String, pidDataProtoBuff: String, transactionFor: String?)
    fun apiCallforAepsFinpayTxnMiniSt()
    fun apiCallforYesAeps()
    fun doSendOtp()
    fun doVerifyOtp()
    fun proceedForEkycAuth()
    fun apiCallforAepsIciciEasyPayTxn()

    interface CaptureFingerView : BaseView {
        fun doRetriveModel(): CaptureFingerModel
        fun onFingpayAepsTxnDone()
        fun onFingpayAepsTxnMiniStmt()
        fun onOTPSent()
        fun onOtpVerificationCompleted()
        fun onAuthenticationCompleted()
    }
}