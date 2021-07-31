package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.utils.Utils
import javax.inject.Inject

class DthRechargePresenterImpl(val view: DthRechargeActivityPresenter.DthRechargeView) : DthRechargeActivityPresenter {

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

    override fun getRechargeProvider() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getGetMobileRechargeProviderRequest()
            request.token = EasyPaisaApp.getLoggedInUser()?.apptoken!!
            request.userId = EasyPaisaApp.getLoggedInUser()?.id!!.toString()
            request.type = "dth"

            repository.apiRechargeProviders(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().providerResponse = it
                view.onProviderResponse()
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

    override fun getProviders() {

    }

    override fun rechargeDth() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            val request = view.doRetriveModel().getElectricityBillPaymentRequest()
            view.showProgress("DTH recharge of ${Utils.formatAmount(request.amount.toDouble())}...")
            repository.apiRechargeDTH(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().electricityBillPaymentResponse = it
                view.onDthRecharge()
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