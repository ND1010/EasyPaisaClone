package com.app.bhaskar.easypaisa.mvp.presenter

import android.content.Intent
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.request_model.AepsRequest
import com.app.bhaskar.easypaisa.ui.activity.CaptureFingerActivity
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class RegisterRemitterPresenterImpl(val view: RegisterRemitterPresenter.RegisterRemitterView) : RegisterRemitterPresenter {

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

    override fun goRegisterRemiter() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress("Registering User Wait for while...")
            val request = view.doRetriveModel().getRegisterRemitterRequest()
            repository.apiRegisterRemitter(request, {
                view.hideProgress()
                view.doRetriveModel().getLoginDomain().remitterRegisterResponse = it
                view.onRegisterRemiiter()
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

    override fun sendOtp() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getGenerateOtpReq()
            repository.apiGenerateOtp(request, {
                view.hideProgress()
                view.doRetriveModel().getLoginDomain().generateOtpResponse = it
                view.onGenerateOtp()
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

    override fun doVerifyRemitter() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress("Verifying wait for while....")
            val request = view.doRetriveModel().getVerifyRemitterReq()
            repository.apiVerifyRemitter(request, {
                view.hideProgress()
                view.doRetriveModel().getLoginDomain().verifyRemitterResponse = it
                view.onVerifyRemitter()
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

}