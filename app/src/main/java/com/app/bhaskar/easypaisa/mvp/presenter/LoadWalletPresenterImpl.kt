package com.app.bhaskar.easypaisa.mvp.presenter

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.request_model.WalletLoadRequest
import com.app.bhaskar.easypaisa.request_model.WalletRequiredDataRequest
import com.app.bhaskar.easypaisa.utils.Utils
import kotlinx.android.synthetic.main.activity_load_wallet.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LoadWalletPresenterImpl(val view: LoadWalletPresenter.LoadWalletView) : LoadWalletPresenter {

    private val mEventBus = EasyPaisaApp.getDefault()

    @Inject
    lateinit var repository: EasyPaisaRepository
    private lateinit var newCalendar :  Calendar

    override fun onError(message: String) {
    }

    override fun registerBus() {
        mEventBus.register(this)
    }

    override fun unRegisterBus() {
        mEventBus.unregister(this)
    }

    /*override fun doAepsTransaction() {
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

    override fun openDatePicker() {
        newCalendar = Calendar.getInstance()
        val StartTime = DatePickerDialog(
            view.getViewActivity(),
            R.style.DatePicker,
            OnDateSetListener { mview, year, monthOfYear, dayOfMonth ->
                val  myFormat  = "dd-MM-yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                newCalendar.set(Calendar.YEAR, year)
                newCalendar.set(Calendar.MONTH, monthOfYear)
                newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                view.getViewActivity().edtSelectPayDate.setText(sdf.format(newCalendar.time))
            },
            newCalendar[Calendar.YEAR],
            newCalendar[Calendar.MONTH],
            newCalendar[Calendar.DAY_OF_MONTH]
        )
        StartTime.datePicker.maxDate = System.currentTimeMillis()
        StartTime.show()
    }

    override fun loadWalletRequest(walletLoadReq: WalletLoadRequest) {
        view.showProgress()
        repository.apiWalletLoadReq(walletLoadReq, {
            view.hideProgress()
            view.doRetriveModel().getDomain().walletLoadRequestResponse = it
            view.onWalletLoadSuccess()
        }, {
            view.hideProgress()
            if (it?.message != null) {
                view.showError(it.message!!)
            }
        })
    }
}