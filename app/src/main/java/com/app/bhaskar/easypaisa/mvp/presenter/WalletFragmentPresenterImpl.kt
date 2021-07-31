package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.utils.Utils
import javax.inject.Inject

class WalletFragmentPresenterImpl(val view: WalletFragmentPresenter.WalletView) :
    WalletFragmentPresenter {


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


    override fun getWalletBalance() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getWalletBalanceReq()
            request.apptoken = EasyPaisaApp.getLoggedInUser()!!.apptoken
            request.userId = EasyPaisaApp.getLoggedInUser()!!.id.toString()

            repository.apiWalletBalance(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().walletBalanceResponse = it!!
                view.onWalletBalance()
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