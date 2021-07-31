package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.utils.Utils
import javax.inject.Inject

class ChangePassPresenterImpl(val view: ChangePasswordPresenter.ChangePassView) :
    ChangePasswordPresenter {

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

    override fun changePassword() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress("Processing password change request,Please wait...")
            val request = view.doRetriveModel().getChangePassRequest()
            repository.apiChangePassword(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().updatePasswordResponse = it
                view.onPasswordChanged()
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