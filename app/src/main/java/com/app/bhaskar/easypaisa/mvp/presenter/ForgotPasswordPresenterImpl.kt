package com.app.bhaskar.easypaisa.mvp.presenter

import android.content.Intent
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.ui.activity.HomeActivity
import com.app.bhaskar.easypaisa.ui.activity.RegistrationActivity
import com.app.bhaskar.easypaisa.utils.Utils
import javax.inject.Inject

class ForgotPasswordPresenterImpl(val view: ForgotPasswordPresenter.ForgotPasswordView) : ForgotPasswordPresenter{

    private val mEventBus = EasyPaisaApp.getDefault()
    @Inject
    lateinit var repository: EasyPaisaRepository


    override fun doUpdatePassword() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getUpdatePasswordRequest()
            repository.apiUpdatePassword(request, {
                view.hideProgress()
                view.doRetriveModel().getUpdatePasswordActivityDomain().updatePasswordResponse= it!!
                view.onPasswordRest()
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