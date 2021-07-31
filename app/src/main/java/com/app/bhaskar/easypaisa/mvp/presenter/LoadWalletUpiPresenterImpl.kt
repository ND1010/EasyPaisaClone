package com.app.bhaskar.easypaisa.mvp.presenter

import android.util.Log
import android.widget.Toast
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.request_model.RozarPayConfig
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.razorpay.Checkout
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class LoadWalletUpiPresenterImpl(val view: LoadWalletUpiPresenter.LoadWalletUpiView) :
    LoadWalletUpiPresenter {

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

    override fun goForLoadWalletUpi() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getLoadWalletUpiReq()
            repository.apiLoadWalletUpi(request, {
                view.hideProgress()
                view.doRetriveModel().getLoginDomain().loadWalletUpiResponse = it
                view.onUpiLoadWallet(it)
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

    override fun doLoadWalletRazorpay() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getOnlinePaymentInitRequest()
            repository.apiInitPaymentGateway(request, {
                view.hideProgress()
                view.doRetriveModel().getLoginDomain().onlinePaymentInitResponse = it
                if (view.doRetriveModel()
                        .getLoginDomain().onlinePaymentInitResponse.status == Constants.ApiResponse.RES_SUCCESS_TPAY
                ) {
                    startPaymentGateWayProcess()
                } else {
                    view.showError(
                        view.doRetriveModel().getLoginDomain().onlinePaymentInitResponse.message
                    )
                }
            }, {
                view.hideProgress()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
            //startPaymentGateWayProcess()
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

    private fun startPaymentGateWayProcess() {
        val co = Checkout()
        co.setKeyID(
            view.doRetriveModel().getLoginDomain().onlinePaymentInitResponse.key
        )

        try {
            val options = JSONObject()
            options.put("name", view.getViewActivity().getString(R.string.app_name))
            options.put("description", "Wallet load Charges")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://retail.easypaisa.in/public/logos/logo1.png")
            options.put("theme.color", "#170E5F");
            options.put("currency", "INR");
//            options.put("partial_payment", 1)
            options.put(
                "order_id",
                view.doRetriveModel().getLoginDomain().onlinePaymentInitResponse.orderid
            )
            options.put(
                "amount",
                view.doRetriveModel().getLoginDomain().onlinePaymentInitResponse.amount
            )//pass amount in currency subunits

            val retryObj = JSONObject();
            retryObj.put("enabled", true)
            retryObj.put("max_count", 3)
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email", EasyPaisaApp.getLoggedInUser()?.email)
            prefill.put("contact", EasyPaisaApp.getLoggedInUser()?.mobile)
            prefill.put("method", "upi")
            options.put("prefill", prefill)

            /*upi only*/
            val displayObj = JSONObject()
            val blocksObj = JSONObject()
            val methodObj = JSONObject()
            val banksObj = JSONObject()

            val apps = JSONArray()
            val flows = JSONArray()
            flows.put("qr")
            flows.put("collect")
            flows.put("intent")
            apps.put("google_pay")
            apps.put("phonepe")
            apps.put("paytm")
            apps.put("amazon")
            methodObj.put("method", "upi")
            methodObj.put("flows", flows)
            //methodObj.put("apps", apps)
            val instrumentsArr = JSONArray()
            instrumentsArr.put(methodObj)

            banksObj.put("name", "Pay using UPI")
            banksObj.put("instruments", instrumentsArr)
            blocksObj.put("banks",banksObj)

            displayObj.put("blocks", blocksObj)
            displayObj.put("sequence", JSONArray().put("block.banks"))
            val preferencesObj = JSONObject()
            preferencesObj.put("show_default_blocks",false)
            displayObj.put("preferences",preferencesObj)

            options.put("config", JSONObject().put("display",displayObj))

                Log.e("TAG", "startPaymentGateWayProces_json ${options.toString(4)}")

            co.open(view.getViewActivity(), options)
        } catch (e: Exception) {
            Toast.makeText(
                view.getViewActivity(),
                "Error in payment: " + e.message,
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
    }

    override fun doUpdatePayment() {
        view.showProgress()
        val request = view.doRetriveModel().getUpdatePaymentStatusRequest()
        repository.apiUpdatePaymentGateway(request, {
            view.hideProgress()
            view.doRetriveModel().getLoginDomain().baseResponse = it
            view.onPaymentStatusUpdate()
        }, {
            view.hideProgress()
            if (it?.message != null) {
                view.showError(it.message!!)
            }
        })
    }


}
