package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.utils.Utils
import javax.inject.Inject

class UtiRegistrationPresenterImpl(val view: UtiRegistrationPresenter.UtiRegistrationView) : UtiRegistrationPresenter {

    private val mEventBus = EasyPaisaApp.getDefault()
    @Inject
    lateinit var repository: EasyPaisaRepository

    override fun registerAgentUti() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getUtiRegisterRequest()
            repository.apiUtiRegistration(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().utiRegistrationResponse= it
                view.onUtiRegistered()
            }, {
                view.hideProgress()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        }else{
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