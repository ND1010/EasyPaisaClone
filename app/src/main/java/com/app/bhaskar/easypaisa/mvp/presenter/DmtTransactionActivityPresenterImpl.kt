package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.utils.Utils
import javax.inject.Inject

class DmtTransactionActivityPresenterImpl(val view: DmtTransactionActivityPresenter.DmtTransactionView) : DmtTransactionActivityPresenter {

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

    override fun doTransferAmount() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            val request = view.doRetriveModel().getDmtTransactionRequest()
            view.showProgress("Transferring ${request.amount} to ${request.benename}'s ${request.benebank} account..." )
            repository.apiDoDmtTransaction(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().dmtTransactionResponse = it
                view.onDmtTransactionDone()
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
