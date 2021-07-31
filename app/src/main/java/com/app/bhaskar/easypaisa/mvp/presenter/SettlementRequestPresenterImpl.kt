package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.request_model.MoveToBankWalletRequest
import com.app.bhaskar.easypaisa.request_model.WalletRequiredDataRequest
import com.app.bhaskar.easypaisa.utils.Utils
import javax.inject.Inject

class SettlementRequestPresenterImpl(val view: SettlementRequestPresenter.SettlementRequestView) :
    SettlementRequestPresenter {

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

    override fun getRequiredData() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = WalletRequiredDataRequest()
            request.token = EasyPaisaApp.getLoggedInUser()?.apptoken!!
            request.userId = EasyPaisaApp.getLoggedInUser()?.id!!.toString()

            repository.apiWalletLoadReqData(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().walletRequiredDataResponse = it
                view.onWalletLoadData()
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

    override fun requestForMoveWalletBank(request: MoveToBankWalletRequest) {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress(if (request.type == "wallet") "Request for Wallet to wallet" else "Request for Wallet to bank")
            repository.apiMoveWalletBank(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().walletLoadRequestResponse = it
                view.onMoveWalletOrBank()
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