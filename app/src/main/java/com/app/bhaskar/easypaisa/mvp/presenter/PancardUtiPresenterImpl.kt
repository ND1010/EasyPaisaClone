package com.app.bhaskar.easypaisa.mvp.presenter

import android.util.Log
import android.view.View
import android.widget.Toast
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.request_model.PancardDataRequest
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.razorpay.Checkout
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class PancardUtiPresenterImpl(val view: PancardUtiPresenter.PancardUtiView) : PancardUtiPresenter {

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

    override fun doGetPanStatusForAgent() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val req  = PancardDataRequest()
            req.token = EasyPaisaApp.getLoggedInUser()!!.apptoken
            req.userId = EasyPaisaApp.getLoggedInUser()!!.id.toString()
            repository.apiGetPancardData(req, {
                view.doRetriveModel().getDomain().pancardResponse = it
                view.hideProgress()
                view.pancardData()
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


    override fun docallapiMatmOrderOnline() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress("Please wait for while...")
            val req  = view.doRetriveModel().getOrderDeviceOnlineMatm()
            repository.apiOnlineOrderMatm(req, {response ->
                view.doRetriveModel().getDomain().matmOnlinePaymentResponse = response
                view.hideProgress()
                if(response.status == Constants.ApiResponse.RES_SUCCESS_TPAY){
                    startPaymentGateWayProcess()
                }else{
                    Utils.showAlert(view.getViewActivity(),response.message,"", View.OnClickListener {  })
                }
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

    override fun doUpdateMatmOrder() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress("Please wait for while...")
            val req  = view.doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest()
            repository.apiOnlineOrderMatmUpdate(req, {response ->
                view.hideProgress()
                view.doRetriveModel().getDomain().baseResponse = response
                view.onMatmDeviceOnlinePaymentUpdate()
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

    override fun doOrderDevice() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress("Processing Order...")
            val req  = view.doRetriveModel().getOrderDeviceReq()
            repository.apiOrderNewDevice(req, {
                view.doRetriveModel().getDomain().orderDeviceResponse = it
                view.hideProgress()
                view.onDeviceOrdered()
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

    override fun doGetTokenForPan() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress("Processing for Tokens...")
            val req  = view.doRetriveModel().getPanTokenRequest()
            repository.apiGetTokenForPancard(req, {
                view.doRetriveModel().getDomain().panTokenResponse = it
                view.hideProgress()
                view.panTokenPurchased()
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

    override fun resetPSAID() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress("Processing for Rest PSA ID...")
            val req  = view.doRetriveModel().getUtiPanTokenResetRequest()
            repository.apiResetPsaId(req, {
                view.doRetriveModel().getDomain().resetPsaIDResponse = it
                view.hideProgress()
                view.resetPsaId()
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

    private fun startPaymentGateWayProcess() {
        val co = Checkout()
        co.setKeyID(
            view.doRetriveModel().getDomain().matmOnlinePaymentResponse.key
        )

        try {
            val options = JSONObject()
            options.put("name", view.getViewActivity().getString(R.string.app_name))
            options.put("description", "Micro Atm Device")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://retail.easypaisa.in/public/logos/logo1.png")
            options.put("theme.color", "#170E5F")
            options.put("currency", "INR");
            options.put("order_id", view.doRetriveModel().getDomain().matmOnlinePaymentResponse.orderid)
            options.put("amount", view.doRetriveModel().getDomain().matmOnlinePaymentResponse.amount)

            val retryObj = JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 3);
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
//            methodObj.put("apps", apps)
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
}