package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.utils.Utils
import javax.inject.Inject

class RegistrationPresenterImpl(val view: RegistrationPresenter.RegistrationView) : RegistrationPresenter{


    private val mEventBus = EasyPaisaApp.getDefault()
    @Inject
    lateinit var repository: EasyPaisaRepository

    override fun doGetOtp() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            val request  = view.doRetriveModel().getOtpRequest()
            view.showProgress()
            repository.doGetOtp(request, {
                view.hideProgress()
                view.onOtpSent(it)
            }, {
                view.hideProgress()
                view.showError(it!!.localizedMessage!!)
            })
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

    override fun doRegisterUser() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            val request  = view.doRetriveModel().getRegistrationRequest()
            view.showProgress()
            repository.doRegisterUser(request, {
                view.hideProgress()
                view.onRegistrationDone(it)
            }, {
                view.hideProgress()
                view.showError(it!!.localizedMessage!!)
            })
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }


    override fun onError(message: String) {
    }

    override fun registerBus() {
        mEventBus.register(this)
    }

    override fun unRegisterBus() {
        mEventBus.unregister(this)
    }

}