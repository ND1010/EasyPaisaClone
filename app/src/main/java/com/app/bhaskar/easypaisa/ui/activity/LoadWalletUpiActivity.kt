package com.app.bhaskar.easypaisa.ui.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.domain.LoadWalletUpiResponse
import com.app.bhaskar.easypaisa.mvp.model.AepsActivityModel
import com.app.bhaskar.easypaisa.mvp.model.LoadWalletActivityModel
import com.app.bhaskar.easypaisa.mvp.model.LoadWalletUpiActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.*
import com.app.bhaskar.easypaisa.response_model.OnlinePaymentError
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.pa.baseframework.baseview.BaseActivity
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import kotlinx.android.synthetic.main.activity_load_wallet_upi.*
import kotlinx.android.synthetic.main.activity_online_payment_receipt.*
import java.util.*
import javax.inject.Inject


class LoadWalletUpiActivity : BaseActivity(), LoadWalletUpiPresenter.LoadWalletUpiView,
    PaymentResultWithDataListener {

    val key = "06e1a6d9-1ebf-450d-9d46-cf4bb50ba92e"
    fun isUpiValid(text: String): Boolean {
        return text.matches("^[\\w-]+@\\w+$".toRegex())
    }

    @Inject
    lateinit var presenter: LoadWalletUpiPresenterImpl
    lateinit var model: LoadWalletUpiActivityModel
    private val TAG = LoadWalletUpiActivity::class.java.simpleName
    private var errorDescription = ""
    private var rzpPaymentId = ""
    private var orderId = ""

    private enum class PaymentStatus {
        PENDING,
        SUCCESS,
        FAILED
    }

    private var paymentStatus = PaymentStatus.PENDING


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_wallet_upi)
        setSupportActionBar(toolbarAtm)
        model = LoadWalletUpiActivityModel(getViewActivity())
        presenter = LoadWalletUpiPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)

        toolbarAtm.setNavigationOnClickListener { onBackPressed() }
        btn_submit.setOnClickListener {
            if (edtAmount.text.toString().trim().isEmpty()) {
                Toast.makeText(getViewActivity(), "Please enter amount", Toast.LENGTH_LONG).show();
                return@setOnClickListener
            } else if (edtAmount.text.toString().trim().isNotEmpty() && edtAmount.text.toString()
                    .trim().toDouble() < 10
            ) {
                Toast.makeText(
                    getViewActivity(),
                    "Amount should be 10 or more then 100",
                    Toast.LENGTH_LONG
                ).show();
                return@setOnClickListener
            }
            doRetriveModel().getOnlinePaymentInitRequest().token =
                EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
            doRetriveModel().getOnlinePaymentInitRequest().type = "upiload"
            doRetriveModel().getOnlinePaymentInitRequest().amount = edtAmount.text.toString().trim()
            doRetriveModel().getOnlinePaymentInitRequest().userId =
                EasyPaisaApp.getLoggedInUser()?.id.toString()
            presenter.doLoadWalletRazorpay()

            /*if (edtAmount.text.toString().trim().isEmpty()) {
                Toast.makeText(getViewActivity(), "Please enter amount", Toast.LENGTH_LONG).show();
                return@setOnClickListener
            } else if (edtAmount.text.toString().trim().isNotEmpty() && edtAmount.text.toString()
                    .trim().toDouble() < 100
            ) {
                Toast.makeText(
                    getViewActivity(),
                    "Amount should be 100 or more then 100",
                    Toast.LENGTH_LONG
                ).show();
                return@setOnClickListener
            }

            if (!Utils.isNetworkConnected(getViewActivity())) {
                showError(getViewActivity().getString(R.string.no_internet_message))
                return@setOnClickListener
            }

            doRetriveModel().getOnlinePaymentInitRequest().token =
                EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
            doRetriveModel().getOnlinePaymentInitRequest().type = "upiload"
            doRetriveModel().getOnlinePaymentInitRequest().amount = edtAmount.text.toString().trim()
            doRetriveModel().getOnlinePaymentInitRequest().userId =
                EasyPaisaApp.getLoggedInUser()?.id.toString()
            presenter.doLoadWalletRazorpay()*/

            //OLD code for upi load wallet
            /*doRetriveModel().getLoadWalletUpiReq().amount =edtAmount.text.toString().trim()
            doRetriveModel().getLoadWalletUpiReq().token =EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
            doRetriveModel().getLoadWalletUpiReq().userId =EasyPaisaApp.getLoggedInUser()?.id.toString()
            doRetriveModel().getLoadWalletUpiReq().vpaid =edtMobileNumber.text.toString().trim()
            presenter.goForLoadWalletUpi()*/
        }
    }

    override fun doRetriveModel(): LoadWalletUpiActivityModel {
        return model
    }

    override fun onUpiLoadWallet(it: LoadWalletUpiResponse) {
        if (it.status == Constants.ApiResponse.RES_SUCCESS) {
            val builder: Uri.Builder = Uri.Builder()
            builder.scheme("https")
                .authority("upigateway.com")
                .appendPath("gateway")
                .appendPath("android")
                .appendQueryParameter("key", it.data.data.key)
                .appendQueryParameter("client_vpa", it.data.data.clientVpa)
                .appendQueryParameter("client_txn_id", it.data.data.clientTxnId)
                .appendQueryParameter(
                    "amount",
                    it.data.data.amount
                ) // Amount Can also be hidden if your product price is fix
                .appendQueryParameter("p_info", it.data.data.pInfo)
                .appendQueryParameter(
                    "client_name",
                    it.data.data.clientName
                ) // Set Client Name.
                .appendQueryParameter(
                    "client_email",
                    it.data.data.clientEmail
                )
                .appendQueryParameter(
                    "client_mobile",
                    it.data.data.clientMobile
                )
                .appendQueryParameter(
                    "udf1",
                    it.data.data.udf1
                )
                .appendQueryParameter(
                    "udf2",
                    it.data.data.udf2
                )
                .appendQueryParameter(
                    "udf3",
                    it.data.data.udf3
                )
                .appendQueryParameter("redirect_url", it.data.data.redirectUrl)

            val intent = Intent(Intent.ACTION_VIEW, builder.build())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setPackage("com.android.chrome")
            try {
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                // Chrome browser presumably not installed so allow user to choose instead
                intent.setPackage(null)
                startActivity(intent)
            }
        } else {
            Utils.showAlert(getViewActivity(), it.message, "", View.OnClickListener { })
        }
    }

    override fun getViewActivity(): Activity {
        return this@LoadWalletUpiActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {

    }

    override fun onPaymentSuccess(rzpPaymentId: String?, paymentData: PaymentData?) {
        Log.e(TAG, "onPaymentSuccess: rzpPaymentId-> $rzpPaymentId")
        Log.e(TAG, "onPaymentSuccess: paymentData-> ${paymentData?.data.toString()}")
        edtAmount.setText("")
        this.rzpPaymentId = rzpPaymentId.toString()
        this.orderId = paymentData?.paymentId.toString()
        paymentStatus = PaymentStatus.SUCCESS
        doRetriveModel().getUpdatePaymentStatusRequest().amount =
            doRetriveModel().getOnlinePaymentInitRequest().amount
        doRetriveModel().getUpdatePaymentStatusRequest().orderid = paymentData?.orderId.toString()
        doRetriveModel().getUpdatePaymentStatusRequest().signature = paymentData?.signature.toString()
        doRetriveModel().getUpdatePaymentStatusRequest().payid = paymentData?.paymentId.toString()
        doRetriveModel().getUpdatePaymentStatusRequest().type = "upiload"
        doRetriveModel().getUpdatePaymentStatusRequest().token =
            EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
        doRetriveModel().getUpdatePaymentStatusRequest().userId =
            EasyPaisaApp.getLoggedInUser()?.id.toString()
        presenter.doUpdatePayment()
    }

    override fun onPaymentError(
        errorCode: Int,
        errorDescription: String?,
        paymentData: PaymentData?
    ) {
        paymentStatus = PaymentStatus.FAILED
        this.errorDescription = errorDescription ?:getString(R.string.some_thing_wants_wong)
        if (errorDescription==null){
            this.errorDescription=getString(R.string.some_thing_wants_wong)
        }else{
            Log.e(TAG, "onPaymentError: $errorDescription")
            val errordata = EasyPaisaApp.getGson().fromJson<OnlinePaymentError>(errorDescription,OnlinePaymentError::class.java)
            this.errorDescription  = errordata?.error?.description ?: getString(R.string.some_thing_wants_wong)

        }
        doRetriveModel().getUpdatePaymentStatusRequest().amount =
            doRetriveModel().getOnlinePaymentInitRequest().amount
        doRetriveModel().getUpdatePaymentStatusRequest().orderid = doRetriveModel().getLoginDomain().onlinePaymentInitResponse.orderid
        doRetriveModel().getUpdatePaymentStatusRequest().payid = ""
        doRetriveModel().getUpdatePaymentStatusRequest().token =
            EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
        doRetriveModel().getUpdatePaymentStatusRequest().userId =
            EasyPaisaApp.getLoggedInUser()?.id.toString()
        doRetriveModel().getUpdatePaymentStatusRequest().type = "upiload"
        doRetriveModel().getUpdatePaymentStatusRequest().signature = ""
        doRetriveModel().getUpdatePaymentStatusRequest().status ="failed"
        presenter.doUpdatePayment()
    }

    override fun onPaymentStatusUpdate() {
        when (paymentStatus) {
            PaymentStatus.SUCCESS -> {
                val dialogSuccess = Utils.showSuccessAlertOnly(
                    getViewActivity(),
                    "${Utils.formatAmount(doRetriveModel().getOnlinePaymentInitRequest().amount.toDouble())} has been added successfully in your main wallet."
                )
                dialogSuccess.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    if (dialogSuccess.isShowing){
                        dialogSuccess.dismissDialog()
                    }
                    openPaymentReceipt()
                }, 3000)
            }
            PaymentStatus.FAILED->{
                Utils.showAlert(getViewActivity(),this.errorDescription,"",View.OnClickListener {
                })
            }
        }
    }

    private fun openPaymentReceipt() {
        val intentReceipt = Intent(getViewActivity(),OnlinePaymentReceiptActivity::class.java)
        intentReceipt.putExtra("status","success")
        intentReceipt.putExtra("order_id",orderId)
        intentReceipt.putExtra("payment_id",rzpPaymentId)
        intentReceipt.putExtra("amount",doRetriveModel().getOnlinePaymentInitRequest().amount)
        intentReceipt.putExtra("type","load_wallet")
        startActivity(intentReceipt)
    }
}