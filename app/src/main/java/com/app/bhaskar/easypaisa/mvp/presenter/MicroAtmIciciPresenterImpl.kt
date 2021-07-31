package com.app.bhaskar.easypaisa.mvp.presenter

import android.content.Intent
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.ui.activity.MicroAtmIciciActivty
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.fingpay.microatmsdk.MicroAtmLoginScreen
import javax.inject.Inject

class MicroAtmIciciPresenterImpl(val view: MicroAtmIciciPresenter.MicroAtmIciciView) :
    MicroAtmIciciPresenter {

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

    override fun doTransaction() {
        val req = view.doRetriveModel().getAtmRequest()
        val intent = Intent(view.getViewActivity(), MicroAtmLoginScreen::class.java)
        intent.putExtra(Constants.MicroAtm.MERCHANT_USERID, req.mID)
        intent.putExtra(Constants.MicroAtm.MERCHANT_PASSWORD, req.mPss)
        intent.putExtra(Constants.MicroAtm.SUPER_MERCHANTID, req.smID)
        intent.putExtra(Constants.MicroAtm.AMOUNT, req.amount)
        intent.putExtra(Constants.MicroAtm.REMARKS, req.remarks)
        intent.putExtra(Constants.MicroAtm.MOBILE_NUMBER, req.number)
        intent.putExtra(Constants.MicroAtm.AMOUNT_EDITABLE, req.amountEditable)
        intent.putExtra(Constants.MicroAtm.IMEI, req.imei)
        intent.putExtra(Constants.MicroAtm.TXN_ID, req.txnID)
        intent.putExtra(Constants.MicroAtm.TYPE, req.type)
        intent.putExtra(Constants.MicroAtm.LATITUDE, req.lat)
        intent.putExtra(Constants.MicroAtm.LONGITUDE, req.long)
        intent.putExtra(Constants.MicroAtm.MICROATM_MANUFACTURER, 2)
        view.getViewActivity().startActivityForResult(intent,Constants.MicroAtm.RES_CODE)
    }

    override fun doAtmInitTransaction() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getMicroAtmInitTransactionRequest()
            repository.apiMicroAtmInit(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().microAtmInitTransactionResponse = it
                view.onMicroInitResponse()
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

    override fun doAtmUpdateTransaction() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getMicroAtmUpdateTransactionRequest()
            repository.apiMicroAtmUpdate(request, {
                view.hideProgress()
                view.doRetriveModel().getDomain().microAtmUpdateResponse = it
                view.onMicroUpdate()
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