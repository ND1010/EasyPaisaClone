package com.app.bhaskar.easypaisa.mvp.presenter

import android.content.Intent
import android.util.Log
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.model.piddata.PidOptions
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import javax.inject.Inject

class CaptureFingerPresenterImpl(val view: CaptureFingerPresenter.CaptureFingerView) :
    CaptureFingerPresenter {

    private val mEventBus = EasyPaisaApp.getDefault()
    @Inject
    lateinit var repository: EasyPaisaRepository


    override fun onError(message: String) {
    }

    override fun registerBus() {
        mEventBus.register(this)
    }

    override fun unRegisterBus() {
        mEventBus.unregister(this)
    }

    override fun apiCallforAepsFinpayTxn() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress("Processing AePS transaction,Please wait...")
            val request = view.doRetriveModel().getFingPayAepsRequest()
            repository.apiFingPayICICITransaction(request, {
                view.hideProgress()
                view.doRetriveModel().getCaptureDomain().fingPayAepsTxnResponse = it
                view.onFingpayAepsTxnDone()
            }, {
                view.hideProgress()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

    override fun apiCallforAepsIciciEasyPayTxn() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress("Processing AePS transaction,Please wait...")
            val request = view.doRetriveModel().getFingPayAepsRequest()
            repository.apiFingPayICICITransaction(request, {
                view.hideProgress()
                view.doRetriveModel().getCaptureDomain().fingPayAepsTxnResponse = it
                view.onFingpayAepsTxnDone()
            }, {
                view.hideProgress()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

    override fun apiCallforYesAeps() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress("Processing AePS transaction,Please wait...")
            val request = view.doRetriveModel().getFingPayAepsRequest()
            repository.apiYesAepsTransaction(request, {
                view.hideProgress()
                view.doRetriveModel().getCaptureDomain().fingPayAepsTxnResponse = it
                view.onFingpayAepsTxnDone()
            }, {
                view.hideProgress()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

    override fun doSendOtp() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getSendOtpRequest()
            repository.apiSendOtpEkyc(request, {
                view.hideProgress()
                view.doRetriveModel().getCaptureDomain().sendOtpEkycResponse = it
                view.onOTPSent()
            }, {
                view.hideProgress()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

    override fun apiCallforAepsFinpayTxnMiniSt() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress("Processing AePS Mini statement...")
            val request = view.doRetriveModel().getFingPayAepsRequest()
            repository.apiFingPayICICITransactionMiniStatement(request, {
                view.hideProgress()
                view.doRetriveModel().getCaptureDomain().fingpayMiniStatementResponse = it
                view.onFingpayAepsTxnMiniStmt()
            }, {
                view.hideProgress()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

    override fun doVerifyOtp() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getVerifyOtpEkycRequest()
            repository.apiVerifyOtpEkyc(request, {
                view.hideProgress()
                view.doRetriveModel().getCaptureDomain().baseResponse = it
                view.onOtpVerificationCompleted()
            }, {
                view.hideProgress()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

    override fun proceedForEkycAuth() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress("Authentication in process...")
            val request = view.doRetriveModel().getEkycAuthenticationRequest()
            repository.apiEkycAuthentication(request, {
                view.hideProgress()
                view.doRetriveModel().getCaptureDomain().baseResponse = it
                view.onAuthenticationCompleted()
            }, {
                view.hideProgress()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

    override fun scanFingerprint(devicePackageName: String, pidDataProtoBuff: String, transactionFor: String?) {
        val pidOption =
            PidOptions.getPIDOptions(pidDataProtoBuff,transactionFor)//if (finpayICICIAEPS) "0" else "1"

        if (pidOption != null) {
            Log.e("PidOptions", pidOption)
            val intent2 = Intent()
            intent2.setPackage(devicePackageName)
            intent2.action = "in.gov.uidai.rdservice.fp.CAPTURE"
            intent2.putExtra("PID_OPTIONS", pidOption)
            view.getViewActivity().startActivityForResult(intent2, Constants.UI.FINGERSCAN_CODE)
        }
    }


}