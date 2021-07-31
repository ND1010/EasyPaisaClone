package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.utils.Utils
import javax.inject.Inject

class ElectricityBillPaymentPresenterImpl(val view: ElectricityBillPaymentPresenter.ElecricityBillPaymentView) :
    ElectricityBillPaymentPresenter {

    private val mEventBus = EasyPaisaApp.getDefault()
    @Inject
    lateinit var repository: EasyPaisaRepository

    override fun getElStates() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getElectricityStateRequest()
            request.token = EasyPaisaApp.getLoggedInUser()?.apptoken!!
            request.userId = EasyPaisaApp.getLoggedInUser()?.id!!.toString()

            repository.apiElectricityState(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().electricityStateResponse = it
                view.onState()
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

    override fun getBillBoard(id: String) {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getElectricityBoardRequest()
            request.token = EasyPaisaApp.getLoggedInUser()?.apptoken!!
            request.userId = EasyPaisaApp.getLoggedInUser()?.id!!.toString()
            request.state = id
            request.type = view.doGetProvider()

            repository.apiElectricityBoard(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().electricityBoardResponse = it
                view.onBoard()
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

    override fun getchBillDetails() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            val request = view.doRetriveModel().getFetchElBillDetailRequest()
            view.showProgress("Fetching Bill Detail for ${request.number}")
            repository.apiFetchElBillDetails(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().fetchElBillDetailResponse = it
                view.onFetchBill()
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

    override fun payElectricityBill() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            val request = view.doRetriveModel().getElectricityBillPaymentRequest()
            view.showProgress("Paying Bill of ${Utils.formatAmount(request.amount.toDouble())}...")
            repository.apiPayElectricityBill(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().electricityBillPaymentResponse = it
                view.onBillPaid()
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