package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.utils.Utils
import javax.inject.Inject

class AddNewBeneActivityPresenterImpl(val view: AddNewBeneficiaryPresenter.AddNewBeneficiaryView) : AddNewBeneficiaryPresenter {

    private val mEventBus = EasyPaisaApp.getDefault()

    @Inject
    lateinit var repository: EasyPaisaRepository

    override fun doAddNewBeneficiary() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getAddNewBeneRequest()
            repository.apiAddNewBene(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().addNewBenRe = it
                view.onBenAdded()
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

    /*override fun goForTransactionAeps() {
        val req = view.doRetriveModel().getAepsRequest()
        val type = object : TypeToken<AepsRequest>() {}.type
        val intent = Intent(view.getViewActivity(), CaptureFingerActivity::class.java)
        intent.putExtra(Constants.UI.AEPSREQUEST, EasyPaisaApp.getGson().toJson(req, type))
        view.getViewActivity().startActivity(intent)
    }

    override fun doAepsTransaction() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getAepsTransactionRequest()
            repository.apiAepsTxnData(request, {
                view.hideProgress()
                view.doRetriveModel().getLoginDomain().aepsTxnDataResponse = it
                view.onAepTxnData()
            }, {
                view.hideProgress()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }*/
}