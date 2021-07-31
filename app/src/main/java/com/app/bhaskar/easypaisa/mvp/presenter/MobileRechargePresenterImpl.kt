package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.request_model.MobileRechargeRequest
import com.app.bhaskar.easypaisa.utils.Utils
import javax.inject.Inject

class MobileRechargePresenterImpl(val view: MobileRechargePresenter.MobileRechargeView) : MobileRechargePresenter {


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

    override fun getProviderList() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getMobilerProviderRequest().copy(
                token = EasyPaisaApp.getLoggedInUser()?.apptoken!!,
                type = "mobile",
                userId = EasyPaisaApp.getLoggedInUser()?.id.toString()
            )

            repository.apiRechargeProviders(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().mobileProviderResponse= it
                view.onMobileOperator()
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

    override fun getOperatorFromMobile() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getMonileOperatorFromMobile()
            repository.apiOperatorFromMobile(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().mobileOperatorFromMobile= it
                view.onMobileOperatorByMobile()
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

    override fun doMobileRecharge(mobileRechargeReq: MobileRechargeRequest) {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress("Mobile Recharge of ${Utils.formatAmount(mobileRechargeReq.amount.toDouble())}...")
            val request = view.doRetriveModel().getMobileRechargeRequest()
            repository.apiMobileRecharge(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().mobileRechargeResponse= it
                view.onMobileRechargeDone()
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
}