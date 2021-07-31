package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.utils.Utils
import javax.inject.Inject

class AccountFragmentPresenterImpl(val view: AccountFragmentPresenter.AccountFragmentView) : AccountFragmentPresenter {

    private val mEventBus = EasyPaisaApp.getDefault()
    @Inject
    lateinit var repository: EasyPaisaRepository

    override fun changePassword() {

    }

    override fun doLogout() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress("Processing AePS transaction,Please wait...")
            val request = view.doRetriveModel().getLogoutReq()
            request.apptoken = EasyPaisaApp.getLoggedInUser()?.apptoken!!
            request.userId = EasyPaisaApp.getLoggedInUser()?.id!!.toString()
            repository.apiLogout(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().logoutResponse = it
                view.onLogoutSuccess()
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

    override fun onError(message: String) {
    }

    override fun registerBus() {
        mEventBus.register(this)
    }

    override fun unRegisterBus() {
        mEventBus.unregister(this)
    }

}