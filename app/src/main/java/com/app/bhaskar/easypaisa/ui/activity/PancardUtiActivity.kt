package com.app.bhaskar.easypaisa.ui.activity

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.PancardUtiActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.PancardUtiPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.PancardUtiPresenterImpl
import com.app.bhaskar.easypaisa.response_model.AgentPancardResponse
import com.app.bhaskar.easypaisa.response_model.OnlinePaymentError
import com.app.bhaskar.easypaisa.ui.dialog.DialogPancardToken
import com.app.bhaskar.easypaisa.ui.listener.OnUserLocationListener
import com.app.bhaskar.easypaisa.ui.service.GoogleFuesedLocationService
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.pa.baseframework.baseview.BaseActivity
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import kotlinx.android.synthetic.main.activity_pancard_uti.*
import kotlinx.android.synthetic.main.toolbar_simple.*
import java.util.*
import javax.inject.Inject

class PancardUtiActivity : BaseActivity(), View.OnClickListener,
    PancardUtiPresenter.PancardUtiView,
    PaymentResultWithDataListener {

    private var panCardResponse: AgentPancardResponse? = null
    private var amount: Double = 0.0
    private var uticharge: Double = 0.0
    private var rotationAngle = 0
    private lateinit var model: PancardUtiActivityModel
    private lateinit var googleFusedLocation: GoogleFuesedLocationService
    private var serviceFor = Constants.AvailableService.SERVICE_UTI_PAN
    private var rzpPaymentId = ""
    private var orderId = ""

    private enum class PaymentStatus {
        PENDING,
        SUCCESS,
        FAILED
    }

    private var paymentStatus = PaymentStatus.PENDING
    private var errorDescription = ""


    companion object {
        const val TAG = "PancardUtiActivity"
        const val UPDATE_DATA = 112
    }

    @Inject
    lateinit var presenter: PancardUtiPresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pancard_uti)
        initviews()
        linearHeader.setOnClickListener(this)
        tvResetTokenId.setOnClickListener(this)

        btnProceed.setOnClickListener {
            if (panCardResponse == null) {
                showToast(getString(R.string.some_thing_wants_wong))
                return@setOnClickListener
            }
            if (EasyPaisaApp.getUserLatLng() == null) {
                googleFusedLocation.needToShowDialog = true
                googleFusedLocation.accessUserCurrentLocation()
                return@setOnClickListener
            }
            if (serviceFor == Constants.AvailableService.SERVICE_ORDER_DEVICE) {
                if (textInputLayoutAddress.editText!!.text.toString().trim().isEmpty()){
                    showToast("Please enter deliver address.")
                    return@setOnClickListener
                }

                /* if (rbtnMainWallet.isChecked) { // From main wallet transaction
                     doOrderDevice()
                 } else { //From online
                     doRetriveModel().getOrderDeviceOnlineMatm().token =
                         EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
                     doRetriveModel().getOrderDeviceOnlineMatm().userId =
                         EasyPaisaApp.getLoggedInUser()?.id.toString()
                     doRetriveModel().getOrderDeviceOnlineMatm().type = "matm"
                     doRetriveModel().getOrderDeviceOnlineMatm().transactionType = "tokenrequest"
                     doRetriveModel().getOrderDeviceOnlineMatm().pantokens =
                         textInputLayoutUtiToken.editText!!.text.toString().trim()
                     presenter.docallapiMatmOrderOnline()
                 }*/
                //Only Online payment
                doRetriveModel().getOrderDeviceOnlineMatm().token =
                    EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
                doRetriveModel().getOrderDeviceOnlineMatm().userId =
                    EasyPaisaApp.getLoggedInUser()?.id.toString()
                doRetriveModel().getOrderDeviceOnlineMatm().type = "matm"
                doRetriveModel().getOrderDeviceOnlineMatm().address = textInputLayoutAddress.editText!!.text.toString().trim()
                doRetriveModel().getOrderDeviceOnlineMatm().transactionType = "tokenrequest"
                doRetriveModel().getOrderDeviceOnlineMatm().pantokens =
                    textInputLayoutUtiToken.editText!!.text.toString().trim()
                presenter.docallapiMatmOrderOnline()
            } else {
                doGetTokenForPanUTI()
            }
        }
    }

    override fun doRetriveModel(): PancardUtiActivityModel {
        return model
    }

    override fun pancardData() {

        panCardResponse = doRetriveModel().getDomain().pancardResponse
        if (serviceFor == Constants.AvailableService.SERVICE_ORDER_DEVICE) {
            linearPaymentOption.visibility = View.VISIBLE
            textInputLayoutAddress.visibility = View.VISIBLE
            tvToolbarTitle.text = "Order Micro ATM Device"
            tvResetTokenId.visibility = View.GONE
            textInputLayoutUtiVleId.visibility = View.GONE
            textInputLayoutUtiToken.hint = "Number of device to buy"
            btnProceed.text = "Order Device"
            tvLblDeviceDetail.text = "Device Detail"
            tvLbluserName.text = "Device name"
            tvUserName.text = "Micro ATM"
            tvLblPriceToken.text = "Price"
            tvLblPass.visibility = View.GONE
            tvStatus.visibility = View.GONE
            tvPass.visibility = View.GONE
            uticharge = panCardResponse?.matmcharge ?: 0.0
            tvPriceToken.text = Utils.formatAmount(uticharge)
            if (amount > 0 && panCardResponse != null) {
                btnProceed.isEnabled = true
                btnProceed.alpha = 1F
            } else {
                btnProceed.isEnabled = false
                btnProceed.alpha = 0.5F
            }
            return
        }

        if (panCardResponse?.utiagent == null) {
            startActivityForResult(
                Intent(getViewActivity(), UtiRegistrationActivity::class.java), UPDATE_DATA
            )
            return
        }

        ivStatusUti.setImageDrawable(
            ContextCompat.getDrawable(
                getViewActivity(),
                if (panCardResponse?.utiagent?.status == "pending") R.drawable.ic_pending_txn else R.drawable.ic_success_tnx
            )
        )

        uticharge = panCardResponse?.charge!!
        tvPass.text = panCardResponse?.utiagent?.vlepassword
        tvUserName.text = panCardResponse?.utiagent?.vleid
        tvPriceToken.text = Utils.formatAmount(uticharge)

        textInputLayoutUtiVleId.editText!!.setText(panCardResponse?.utiagent?.vleid)
        tvStatus.text =
            if (panCardResponse?.utiagent?.status == "pending") "Approval in pending" else "Uti Approved"
        tvStatus.setTextColor(
            ContextCompat.getColor(
                getViewActivity(),
                if (panCardResponse?.utiagent?.status == "pending") R.color.colorOrangeDark else R.color.colorGreen
            )
        )
        textInputLayoutUtiAmount.editText!!.setText(amount.toString())
        if (panCardResponse?.utiagent?.status == "pending") {
            btnProceed.isEnabled = false
            btnProceed.alpha = 0.5F
        } else {
            btnProceed.isEnabled = true
            btnProceed.alpha = 1F
        }

    }

    private fun doOrderDevice() {
        doRetriveModel().getOrderDeviceReq().token =
            EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
        doRetriveModel().getOrderDeviceReq().userId = EasyPaisaApp.getLoggedInUser()?.id.toString()
        doRetriveModel().getOrderDeviceReq().provider =
            panCardResponse?.matmprovider!!.id.toString()
        doRetriveModel().getOrderDeviceReq().transactionType = "tokenrequest"
        doRetriveModel().getOrderDeviceReq().type = "matm"
        doRetriveModel().getOrderDeviceReq().pantokens =
            textInputLayoutUtiToken.editText!!.text.toString().trim()
        doRetriveModel().getOrderDeviceReq().latitude =
            EasyPaisaApp.getUserLatLng()?.latitude.toString()
        doRetriveModel().getOrderDeviceReq().longitude =
            EasyPaisaApp.getUserLatLng()?.longitude.toString()
        presenter.doOrderDevice()
    }

    private fun doGetTokenForPanUTI() {
        doRetriveModel().getPanTokenRequest().contactPerson =
            panCardResponse?.utiagent?.contactPerson!!
        doRetriveModel().getPanTokenRequest().email = panCardResponse?.utiagent?.email!!
        doRetriveModel().getPanTokenRequest().location = panCardResponse?.utiagent?.location!!
        doRetriveModel().getPanTokenRequest().mobile = panCardResponse?.utiagent?.mobile!!
        doRetriveModel().getPanTokenRequest().name = panCardResponse?.utiagent?.name!!
        doRetriveModel().getPanTokenRequest().state = panCardResponse?.utiagent?.state!!
        doRetriveModel().getPanTokenRequest().pincode = panCardResponse?.utiagent?.pincode!!
        doRetriveModel().getPanTokenRequest().vleid = panCardResponse?.utiagent?.vleid!!
        doRetriveModel().getPanTokenRequest().pantokens =
            textInputLayoutUtiToken.editText!!.text.toString().trim()
        doRetriveModel().getPanTokenRequest().token = EasyPaisaApp.getLoggedInUser()?.apptoken!!
        doRetriveModel().getPanTokenRequest().userId =
            EasyPaisaApp.getLoggedInUser()?.id.toString()
        doRetriveModel().getPanTokenRequest().transactionType = "tokenrequest"
        doRetriveModel().getPanTokenRequest().latitude =
            EasyPaisaApp.getUserLatLng()?.latitude.toString()
        doRetriveModel().getPanTokenRequest().longitude =
            EasyPaisaApp.getUserLatLng()?.longitude.toString()

        presenter.doGetTokenForPan()
    }

    override fun getViewActivity(): Activity {
        return this@PancardUtiActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    private fun initviews() {
        model = PancardUtiActivityModel(getViewActivity())
        presenter = PancardUtiPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        presenter.doGetPanStatusForAgent()
        tvToolbarTitle.text = "Uti Pancard"
        ivHomeBack.setOnClickListener(this)
        serviceFor =
            intent.getIntExtra("selectedService", Constants.AvailableService.SERVICE_UTI_PAN)


        googleFusedLocation = GoogleFuesedLocationService(getViewActivity(), object :
            OnUserLocationListener {
            override fun onUserLocationSuccess(location: Location) {
                if (EasyPaisaApp.getUserLatLng() == null) {
                    showToast("User location not found, please enable GPS")
                    return
                }
                if (serviceFor == Constants.AvailableService.SERVICE_ORDER_DEVICE) {
                    doOrderDevice()
                } else {
                    doGetTokenForPanUTI()
                }
            }

            override fun onUserLocationFail(failMessage: String) {
            }

        }, true)
        textInputLayoutUtiToken.editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {

            }

            override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                amount = 0.0
                amount = if (!char.isNullOrEmpty()) {
                    uticharge * char.toString().toDouble()
                } else {
                    0.0
                }
                textInputLayoutUtiAmount.editText!!.setText(amount.toString())
                if (serviceFor == Constants.AvailableService.SERVICE_ORDER_DEVICE) {
                    if (amount > 0 && panCardResponse != null) {
                        btnProceed.isEnabled = true
                        btnProceed.alpha = 1F
                    } else {
                        btnProceed.isEnabled = false
                        btnProceed.alpha = 0.5F
                    }
                } else {
                    if (amount > 0 && panCardResponse != null && panCardResponse?.utiagent != null && panCardResponse?.utiagent?.status != "pending") {
                        btnProceed.isEnabled = true
                        btnProceed.alpha = 1F
                    } else {
                        btnProceed.isEnabled = false
                        btnProceed.alpha = 0.5F
                    }
                }

            }

        })

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.linearHeader -> {
                rotationAngle = if (rotationAngle == 0) 180 else 0
                ivArrow.animate().rotation(rotationAngle.toFloat()).setDuration(500).start()
                if (constraint.visibility == View.GONE) {
                    constraint.visibility = View.VISIBLE
                } else {
                    constraint.visibility = View.GONE
                }
            }
            R.id.ivHomeBack -> {
                onBackPressed()
            }
            R.id.tvResetTokenId -> {
                if (panCardResponse == null) {
                    showToast(getString(R.string.some_thing_wants_wong))
                    return
                }

                Utils.showAlert(
                    getViewActivity(),
                    "Are you sure you wants to reset PSA ID?",
                    "Pan card",
                    View.OnClickListener {
                        doRetriveModel().getUtiPanTokenResetRequest().adhaar =
                            EasyPaisaApp.getLoggedInUser()?.aadharcard!!
                        doRetriveModel().getUtiPanTokenResetRequest().contactPerson =
                            panCardResponse?.utiagent?.contactPerson!!
                        doRetriveModel().getUtiPanTokenResetRequest().email =
                            panCardResponse?.utiagent?.email!!
                        doRetriveModel().getUtiPanTokenResetRequest().location =
                            panCardResponse?.utiagent?.location!!
                        doRetriveModel().getUtiPanTokenResetRequest().mobile =
                            panCardResponse?.utiagent?.mobile!!
                        doRetriveModel().getUtiPanTokenResetRequest().name =
                            panCardResponse?.utiagent?.name!!
                        doRetriveModel().getUtiPanTokenResetRequest().pincode =
                            panCardResponse?.utiagent?.pincode!!
                        doRetriveModel().getUtiPanTokenResetRequest().vleid =
                            panCardResponse?.utiagent?.vleid!!
                        doRetriveModel().getUtiPanTokenResetRequest().state =
                            panCardResponse?.utiagent?.state!!
                        doRetriveModel().getUtiPanTokenResetRequest().token =
                            EasyPaisaApp.getLoggedInUser()?.apptoken!!
                        doRetriveModel().getUtiPanTokenResetRequest().userId =
                            EasyPaisaApp.getLoggedInUser()?.id.toString()
                        doRetriveModel().getUtiPanTokenResetRequest().transactionType = "psaidreset"
                        presenter.resetPSAID()
                    })
            }
        }
    }

    override fun resetPsaId() {
        val response = doRetriveModel().getDomain().resetPsaIDResponse
        val dialog = DialogPancardToken(getViewActivity())
        dialog.setMessage(response.status, response.message)
        dialog.setPositiveButton("OK", View.OnClickListener {
        })
        dialog.show()
        presenter.doGetPanStatusForAgent()
    }

    override fun showProgress() {
        constraintProgress.visibility = View.VISIBLE
        costraintLayoutMain.visibility = View.GONE
    }

    override fun hideProgress() {
        constraintProgress.visibility = View.GONE
        costraintLayoutMain.visibility = View.VISIBLE
        Utils.hideProgressDialog()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3030 && resultCode == Activity.RESULT_OK) {
            googleFusedLocation.getUserLocation()
        }

        if (requestCode == UPDATE_DATA && resultCode == Activity.RESULT_OK) {
            if (data?.getStringExtra("result") == Constants.ApiResponse.RES_SUCCESS) {
                Utils.showAlert(
                    this@PancardUtiActivity,
                    data.getStringExtra("message") ?: "Uti Registration success",
                    "Uti Registration",
                    View.OnClickListener {
                    })
                presenter.doGetPanStatusForAgent()
            }
        } else if (requestCode == UPDATE_DATA && resultCode == Activity.RESULT_CANCELED) {
            onBackPressed()
        }

    }

    override fun panTokenPurchased() {
        val response = doRetriveModel().getDomain().panTokenResponse
        textInputLayoutUtiToken.editText!!.setText("")
        val dialog = DialogPancardToken(getViewActivity())
        dialog.setMessage(response.status, "${response.message} transaction ID: ${response.txnid}")
        dialog.setPositiveButton("OK", View.OnClickListener {
        })
        dialog.show()
    }


    override fun onDeviceOrdered() {
        val response = doRetriveModel().getDomain().orderDeviceResponse
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            Utils.showSuccessAlert(getViewActivity(), response.message, View.OnClickListener {
                finish()
            })
        } else {
            Utils.showAlert(getViewActivity(), response.message, View.OnClickListener { })
        }
    }

    override fun onPaymentSuccess(rzpPaymentId: String?, paymentData: PaymentData?) {
        Log.e(TAG, "onPaymentSuccess: rzpPaymentId-> $rzpPaymentId")
        Log.e(TAG, "onPaymentSuccess: paymentData-> ${paymentData?.data.toString()}")
        this.rzpPaymentId = rzpPaymentId.toString()
        this.orderId = paymentData?.paymentId.toString()
        paymentStatus = PaymentStatus.SUCCESS

        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().amount =
            doRetriveModel().getDomain().matmOnlinePaymentResponse.amount
        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().orderid =
            paymentData?.orderId.toString()
        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().signature =
            paymentData?.signature.toString()
        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().payid =
            paymentData?.paymentId.toString()
        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().type = "matm"
        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().status = ""
        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().pantokens =
            doRetriveModel().getOrderDeviceOnlineMatm().pantokens
        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().transactionType = "tokenrequest"
        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().token =
            EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().userId =
            EasyPaisaApp.getLoggedInUser()?.id.toString()
        presenter.doUpdateMatmOrder()
    }

    override fun onPaymentError(
        errorCode: Int,
        errorDescription: String?,
        paymentData: PaymentData?
    ) {
        paymentStatus = PaymentStatus.FAILED
        this.errorDescription = errorDescription ?: getString(R.string.some_thing_wants_wong)
        if (errorDescription == null) {
            this.errorDescription = getString(R.string.some_thing_wants_wong)
        } else {
            Log.e(TAG, "onPaymentError: $errorDescription")
            val errordata = EasyPaisaApp.getGson().fromJson<OnlinePaymentError>(
                errorDescription,
                OnlinePaymentError::class.java
            )
            this.errorDescription =
                errordata?.error?.description ?: getString(R.string.some_thing_wants_wong)
        }

        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().amount =
            doRetriveModel().getDomain().matmOnlinePaymentResponse.amount
        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().orderid =
            doRetriveModel().getDomain().matmOnlinePaymentResponse.orderid
        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().signature = ""
        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().payid = ""
        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().type = "matm"
        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().status = "falied"
        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().pantokens =
            doRetriveModel().getOrderDeviceOnlineMatm().pantokens
        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().transactionType = "tokenrequest"
        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().token =
            EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
        doRetriveModel().getMatmOnlineOrderDeviceUpdateRequest().userId =
            EasyPaisaApp.getLoggedInUser()?.id.toString()
        presenter.doUpdateMatmOrder()
    }

    override fun onMatmDeviceOnlinePaymentUpdate() {
        when (paymentStatus) {
            PaymentStatus.SUCCESS -> {
                val dialogSuccess = Utils.showSuccessAlertOnly(
                    getViewActivity(),
                    "Thank you, Micro atm device order has been successfully taken."
                )
                dialogSuccess.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    if (dialogSuccess.isShowing) {
                        dialogSuccess.dismissDialog()
                    }
                    openPaymentReceipt()
                }, 3000)
            }
            PaymentStatus.FAILED -> {
                Utils.showAlert(getViewActivity(), this.errorDescription, "", View.OnClickListener {
                })
            }
        }
    }

    private fun openPaymentReceipt() {
        var amount = 0.0
        if (doRetriveModel().getDomain().matmOnlinePaymentResponse.amount.isNotEmpty()) {
            amount = doRetriveModel().getDomain().matmOnlinePaymentResponse.amount.toDouble() / 100
        }
        val intentReceipt = Intent(getViewActivity(), OnlinePaymentReceiptActivity::class.java)
        intentReceipt.putExtra("status", "success")
        intentReceipt.putExtra("order_id", orderId)
        intentReceipt.putExtra("payment_id", rzpPaymentId)
        intentReceipt.putExtra("amount", amount.toString())
        intentReceipt.putExtra("type", "online_device")
        intentReceipt.putExtra("device_no", doRetriveModel().getOrderDeviceOnlineMatm().pantokens)
        startActivity(intentReceipt)
    }

}