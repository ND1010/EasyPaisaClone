package com.app.bhaskar.easypaisa.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.MicroAtmIciciActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.MicroAtmIciciPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.MicroAtmIciciPresenterImpl
import com.app.bhaskar.easypaisa.response_model.FingpayMiniStatementResponse
import com.app.bhaskar.easypaisa.ui.listener.OnUserLocationListener
import com.app.bhaskar.easypaisa.ui.service.GoogleFuesedLocationService
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.fingpay.microatmsdk.data.MiniStatementModel
import com.google.gson.reflect.TypeToken
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_micro_atm_icici_activty.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class MicroAtmIciciActivty : BaseActivity(), MicroAtmIciciPresenter.MicroAtmIciciView {
    companion object {
        const val TAG = "MicroAtmIciciActivty"
        const val SUPER_MERCHANT_ID = "710"
        const val MY_PERMISSIONS_REQUEST_PHONE_STATE = 12
    }

    private lateinit var googleFusedLocation: GoogleFuesedLocationService
    private var intentMicroAtmReceipt: Intent? = null
    var lat = 25.0961
    var lng = 85.3131
    var mImei = ""
    private var firstTimePermission = false
    private lateinit var model: MicroAtmIciciActivityModel
    private val REQUEST_PERMISSION_SETTING = 101
    var permissionsRequired = arrayOf(
        Manifest.permission.READ_PHONE_STATE
    )
    private val PERMISSION_CALLBACK_CONSTANT = 100
    private var permissionStatus: SharedPreferences? = null

    @Inject
    lateinit var presenter: MicroAtmIciciPresenterImpl
    private var typeOfTransaction: Int = 0
    private val isValide: Boolean
        get() {
            val forTransaction = when (typeOfTransaction) {
                Constants.MicroAtm.MICRO_CHANGE_PIN
                    , Constants.MicroAtm.MICRO_RESET_PIN
                    , Constants.MicroAtm.MICRO_MINI_STATEMENT
                    , Constants.MicroAtm.MICRO_BALANCE -> {
                    false
                }
                else -> {
                    true
                }
            }

            if (forTransaction) {
                if (edtAmount.text.toString().trim().isEmpty()) {
                    showToast("Please enter Amount")
                    return false
                }
                if (edtAmount.text.toString().trim().isNotEmpty()
                    && edtAmount.text.toString().trim().toDouble() <= 0
                ) {
                    showToast("Invalid Amount")
                    return false
                }
            }

            if (edtMobileNumber.text.toString().trim().isEmpty()) {
                showToast("Please enter Mobile Number")
                return false
            }

            if (edtMobileNumber.text.toString().trim().isNotEmpty()
                && edtMobileNumber.text.toString().trim().length != 10
            ) {
                showToast("Invalid Mobile Number")
                return false
            }




            return true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_micro_atm_icici_activty)
        initView()
        googleFusedLocation = GoogleFuesedLocationService(getViewActivity(), object :
            OnUserLocationListener {
            override fun onUserLocationSuccess(location: Location) {
                if (EasyPaisaApp.getUserLatLng() == null) {
                    showToast("User location not found, please enable GPS")
                    return
                }
                lat = EasyPaisaApp.getUserLatLng()!!.latitude
                lng = EasyPaisaApp.getUserLatLng()!!.longitude
                doTransaction()
            }

            override fun onUserLocationFail(failMessage: String) {
            }

        }, true)
    }

    private fun doTransaction() {
        //doRetriveModel().getMicroAtmInitTransactionRequest().amount = edtAmount.text.toString().trim()
        doRetriveModel().getMicroAtmInitTransactionRequest().remark =
            edtRemark.text.toString().trim()
        doRetriveModel().getMicroAtmInitTransactionRequest().mobile =
            edtMobileNumber.text.toString().trim()
        doRetriveModel().getMicroAtmInitTransactionRequest().imei = mImei
        doRetriveModel().getMicroAtmInitTransactionRequest().token =
            EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
        doRetriveModel().getMicroAtmInitTransactionRequest().userId =
            EasyPaisaApp.getLoggedInUser()?.id.toString()
        doRetriveModel().getMicroAtmInitTransactionRequest().latitude =
            lat.toString()
        doRetriveModel().getMicroAtmInitTransactionRequest().longitude =
            lng.toString()
        doRetriveModel().getMicroAtmInitTransactionRequest().transactionType =
            when (typeOfTransaction) {
                Constants.MicroAtm.MICRO_CHANGE_PIN -> {
                    doRetriveModel().getMicroAtmInitTransactionRequest().amount = "0"
                    "CP"
                }
                Constants.MicroAtm.MICRO_RESET_PIN -> {
                    doRetriveModel().getMicroAtmInitTransactionRequest().amount = "0"
                    "RP"
                }
                Constants.MicroAtm.MICRO_WITHDRWAL -> {
                    doRetriveModel().getMicroAtmInitTransactionRequest().amount =
                        edtAmount.text.toString().trim()
                    "CW"
                }
                Constants.MicroAtm.MICRO_BALANCE -> {
                    doRetriveModel().getMicroAtmInitTransactionRequest().amount = "0"
                    "BE"
                }
                Constants.MicroAtm.MICRO_MINI_STATEMENT -> {
                    doRetriveModel().getMicroAtmInitTransactionRequest().amount = "0"
                    "MS"
                }
                Constants.MicroAtm.MICRO_DEPOSIT -> {
                    doRetriveModel().getMicroAtmInitTransactionRequest().amount =
                        edtAmount.text.toString().trim()
                    "CD"
                }
                else -> ""
            }

        presenter.doAtmInitTransaction()

        /*val intent = Intent(getViewActivity(), MicroAtmLoginScreen::class.java)
        intent.putExtra(Constants.MicroAtm.MERCHANT_USERID, "easypaym")
        intent.putExtra(Constants.MicroAtm.MERCHANT_PASSWORD, "1234m")
        intent.putExtra(Constants.MicroAtm.SUPER_MERCHANTID, SUPER_MERCHANT_ID)
        intent.putExtra(Constants.MicroAtm.AMOUNT, edtAmount.text.toString().trim())
        intent.putExtra(Constants.MicroAtm.REMARKS, edtRemark.text.toString().trim())
        intent.putExtra(
            Constants.MicroAtm.MOBILE_NUMBER,
            edtMobileNumber.text.toString().trim()
        )
        intent.putExtra(Constants.MicroAtm.AMOUNT_EDITABLE, false)
        intent.putExtra(Constants.MicroAtm.IMEI, imei)
        intent.putExtra(Constants.MicroAtm.TXN_ID, "fingpay${Date().time}")
        intent.putExtra(Constants.MicroAtm.TYPE, typeOfTransaction)
        intent.putExtra(Constants.MicroAtm.LATITUDE, lat)
        intent.putExtra(Constants.MicroAtm.LONGITUDE, lng)
        intent.putExtra(Constants.MicroAtm.MICROATM_MANUFACTURER, 2)
        startActivityForResult(intent, Constants.MicroAtm.RES_CODE)*/
    }

    private fun initView() {
        setSupportActionBar(toolbarAtm)
        toolbarAtm.setNavigationOnClickListener { onBackPressed() }
        permissionStatus = getSharedPreferences("microatm_sample", 0)
        model = MicroAtmIciciActivityModel(getViewActivity())
        presenter = MicroAtmIciciPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        typeOfTransaction = intent.getIntExtra("type", 0)
        if (typeOfTransaction == 0) {
            finish()
            showToast(getString(R.string.some_thing_wants_wong))
        }
        updateUI()
        checkPermissions()
        btn_submit.setOnClickListener {
            if (!hasPhoneStatePermission()) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        getViewActivity(),
                        Manifest.permission.READ_PHONE_STATE
                    )
                ) {
                    Utils.showAlert(
                        getViewActivity(),
                        "Please provide phone state permission to read Phone's IMEI number",
                        "Phone state Permission",
                        View.OnClickListener {
                            //Yes
                            ActivityCompat.requestPermissions(
                                getViewActivity(),
                                arrayOf(Manifest.permission.READ_PHONE_STATE),
                                MY_PERMISSIONS_REQUEST_PHONE_STATE
                            )
                        },
                        View.OnClickListener {
                            //No
                        })
                } else {
                    ActivityCompat.requestPermissions(
                        getViewActivity(),
                        arrayOf(Manifest.permission.READ_PHONE_STATE),
                        MY_PERMISSIONS_REQUEST_PHONE_STATE
                    )
                }
                return@setOnClickListener
            } else {
                mImei = getImei()
            }




            if (isValide) {
                if (EasyPaisaApp.getUserLatLng() == null) {
                    googleFusedLocation.needToShowDialog =true
                    googleFusedLocation.accessUserCurrentLocation()
                    return@setOnClickListener
                }

                lat = EasyPaisaApp.getUserLatLng()!!.latitude
                lng = EasyPaisaApp.getUserLatLng()!!.longitude
                doTransaction()
            }
        }

        if (!hasPhoneStatePermission()) {
            firstTimePermission = true
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    getViewActivity(),
                    Manifest.permission.READ_PHONE_STATE
                )
            ) {
                Utils.showAlert(
                    getViewActivity(),
                    "Please provide phone state permission to read Phone's IMEI number",
                    "Camera Permission",
                    View.OnClickListener {
                        //Yes
                        ActivityCompat.requestPermissions(
                            getViewActivity(),
                            arrayOf(Manifest.permission.READ_PHONE_STATE),
                            MY_PERMISSIONS_REQUEST_PHONE_STATE
                        )
                    },
                    View.OnClickListener {
                        //No
                    })
            } else {
                ActivityCompat.requestPermissions(
                    getViewActivity(),
                    arrayOf(Manifest.permission.READ_PHONE_STATE),
                    MY_PERMISSIONS_REQUEST_PHONE_STATE
                )
            }
        }
    }

    private fun hasPhoneStatePermission(): Boolean {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
            getViewActivity(),
            Manifest.permission.READ_PHONE_STATE
        ) != PackageManager.PERMISSION_GRANTED)
    }

    private fun updateUI() {
        when (typeOfTransaction) {
            Constants.MicroAtm.MICRO_CHANGE_PIN
                , Constants.MicroAtm.MICRO_RESET_PIN
                , Constants.MicroAtm.MICRO_MINI_STATEMENT
                , Constants.MicroAtm.MICRO_BALANCE -> {
                cardviewAmount.visibility = View.GONE
                cv_remark.visibility = View.GONE
            }
        }

        tv_payment_title.text = when (typeOfTransaction) {
            Constants.MicroAtm.MICRO_CHANGE_PIN -> {
                "ATM Change Pin"
            }
            Constants.MicroAtm.MICRO_RESET_PIN -> {
                "ATM Reset Pin"
            }
            Constants.MicroAtm.MICRO_WITHDRWAL -> {
                "ATM Cash Withdrawal"
            }
            Constants.MicroAtm.MICRO_BALANCE -> {
                "ATM Balance Inquiry"
            }
            Constants.MicroAtm.MICRO_MINI_STATEMENT -> {
                "ATM Mini Statement"
            }
            Constants.MicroAtm.MICRO_DEPOSIT -> {
                "ATM Cash Deposit"
            }
            else -> ""
        }
    }

    override fun doRetriveModel(): MicroAtmIciciActivityModel {
        return model
    }

    override fun getViewActivity(): Activity {
        return this@MicroAtmIciciActivty
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.MicroAtm.RES_CODE) {

            Log.e(TAG, "res" + data?.extras.toString())

            val status = data?.getBooleanExtra(Constants.MicroAtm.TRANS_STATUS, false)
            val response = data?.getStringExtra(Constants.MicroAtm.MESSAGE)
            val transAmount = data?.getDoubleExtra(Constants.MicroAtm.TRANS_AMOUNT, 0.0)
            val balAmount = data?.getDoubleExtra(Constants.MicroAtm.BALANCE_AMOUNT, 0.0)
            var bankRrn = data?.getStringExtra(Constants.MicroAtm.RRN)
            var transType = data?.getStringExtra(Constants.MicroAtm.TRANS_TYPE)
            val type =
                data?.getIntExtra(Constants.MicroAtm.TYPE, Constants.MicroAtm.MICRO_WITHDRWAL)
            val cardNum = data?.getStringExtra(Constants.MicroAtm.CARD_NUM)
            val bankName = data?.getStringExtra(Constants.MicroAtm.BANK_NAME)
            val cardType = data?.getStringExtra(Constants.MicroAtm.CARD_TYPE)
            val terminalId = data?.getStringExtra(Constants.MicroAtm.TERMINAL_ID)
            val fpid = data?.getStringExtra(Constants.MicroAtm.FP_TRANS_ID)
            val txnid = data?.getStringExtra(Constants.MicroAtm.TXN_ID)

            if (type == Constants.MicroAtm.MICRO_WITHDRWAL ||
                type == Constants.MicroAtm.MICRO_DEPOSIT
            ) {
                if (status!!) {
                    //MATM Withdrwal or Debit
                    doRetriveModel().getMicroAtmUpdateTransactionRequest().token =
                        EasyPaisaApp.getLoggedInUser()?.apptoken!!.toString()
                    doRetriveModel().getMicroAtmUpdateTransactionRequest().userId =
                        EasyPaisaApp.getLoggedInUser()?.id!!.toString()
                    doRetriveModel().getMicroAtmUpdateTransactionRequest().txnid = txnid ?: ""
                    doRetriveModel().getMicroAtmUpdateTransactionRequest().status = status ?: false
                    doRetriveModel().getMicroAtmUpdateTransactionRequest().bankRRN = bankRrn ?: ""
                    doRetriveModel().getMicroAtmUpdateTransactionRequest().fpId = fpid ?: ""
                    doRetriveModel().getMicroAtmUpdateTransactionRequest().cardNum = cardNum ?: ""
                    doRetriveModel().getMicroAtmUpdateTransactionRequest().cardType = cardType ?: ""
                    doRetriveModel().getMicroAtmUpdateTransactionRequest().bankName = bankName ?: ""
                    doRetriveModel().getMicroAtmUpdateTransactionRequest().message = response ?: ""

                    intentMicroAtmReceipt =
                        Intent(getViewActivity(), MicroAtmIciciTxnReceipt::class.java)
                    intentMicroAtmReceipt?.putExtra("type", type)
                    intentMicroAtmReceipt?.putExtra("status", status)
                    intentMicroAtmReceipt?.putExtra("response", response)
                    intentMicroAtmReceipt?.putExtra("transType", transType)
                    intentMicroAtmReceipt?.putExtra("transAmount", transAmount)
                    intentMicroAtmReceipt?.putExtra("balAmount", balAmount)
                    intentMicroAtmReceipt?.putExtra("bankRrn", bankRrn)
                    intentMicroAtmReceipt?.putExtra("cardNum", cardNum)
                    intentMicroAtmReceipt?.putExtra("bankName", bankName)
                    intentMicroAtmReceipt?.putExtra("cardType", cardType)
                    intentMicroAtmReceipt?.putExtra("terminalId", terminalId)
                    intentMicroAtmReceipt?.putExtra("txnId", txnid)
                    intentMicroAtmReceipt?.putExtra("fpid", fpid)
                    presenter.doAtmUpdateTransaction()

                } else {
                    Utils.showAlert(
                        getViewActivity(),
                        response ?: getString(R.string.some_thing_wants_wong),
                        "",
                        View.OnClickListener { })
                }

            } else if (type == Constants.MicroAtm.MICRO_BALANCE) {
                if (status!!) {
                    intentMicroAtmReceipt =
                        Intent(getViewActivity(), MicroAtmIciciTxnReceipt::class.java)
                    intentMicroAtmReceipt?.putExtra("type", type)
                    intentMicroAtmReceipt?.putExtra("status", status)
                    intentMicroAtmReceipt?.putExtra("response", "Balance fetch successfully")
                    intentMicroAtmReceipt?.putExtra("transType", transType)
                    intentMicroAtmReceipt?.putExtra("transAmount", transAmount)
                    intentMicroAtmReceipt?.putExtra("balAmount", balAmount)
                    intentMicroAtmReceipt?.putExtra("bankRrn", bankRrn)
                    intentMicroAtmReceipt?.putExtra("cardNum", cardNum)
                    intentMicroAtmReceipt?.putExtra("bankName", bankName)
                    intentMicroAtmReceipt?.putExtra("cardType", cardType)
                    intentMicroAtmReceipt?.putExtra("terminalId", terminalId)
                    intentMicroAtmReceipt?.putExtra("txnId", txnid)
                    intentMicroAtmReceipt?.putExtra("fpid", fpid)
                    Utils.showSuccessAlert(getViewActivity(), "Balance fetch success")
                    Handler(Looper.getMainLooper()).postDelayed({

                        Utils.dismissSuccessAlert()
                        startActivity(intentMicroAtmReceipt)
                        finish()

                    }, 1500)
                } else {
                    Utils.showAlert(
                        getViewActivity(),
                        response ?: getString(R.string.some_thing_wants_wong),
                        "",
                        View.OnClickListener { })
                }

            } else if (type == Constants.MicroAtm.MICRO_MINI_STATEMENT) {
                status?.let {
                    if (status) {
                        val l: List<MiniStatementModel> =
                            data.getParcelableArrayListExtra(Constants.MicroAtm.LIST_DATA)
                                ?: ArrayList()
                        if (l!=null && l.isNotEmpty()) {
                            Log.e(TAG, "Mini Statement: -> $l")
                            val fingpayMiniStatementResponse = FingpayMiniStatementResponse()
                            val arrayListm = ArrayList<FingpayMiniStatementResponse.Statement>()

                            l.forEach { element ->
                                val fingpayMiniStatement = FingpayMiniStatementResponse.Statement()
                                fingpayMiniStatement.amount = element.amount
                                fingpayMiniStatement.narration = element.narration
                                fingpayMiniStatement.date = element.date
                                fingpayMiniStatement.txnType = element.txnType
                                arrayListm.add(fingpayMiniStatement)
                            }
                            fingpayMiniStatementResponse.balance = balAmount.toString()
                            fingpayMiniStatementResponse.bank = bankName.toString()
                            fingpayMiniStatementResponse.rrn = bankRrn.toString()
                            fingpayMiniStatementResponse.statement = arrayListm
                            fingpayMiniStatementResponse.transactionType = transType.toString()
                            fingpayMiniStatementResponse.txnid = txnid.toString()

                            Utils.showSuccessAlert(getViewActivity(), "Statement fetched")
                            Handler(Looper.getMainLooper()).postDelayed({
                                
                                Utils.dismissSuccessAlert()
                                val intent =
                                    Intent(getViewActivity(), MiniStatementActivity::class.java)
                                val type =
                                    object : TypeToken<FingpayMiniStatementResponse>() {}.type
                                intent.putExtra(
                                    Constants.UI.AEPS_MINI_RESPONSE,
                                    EasyPaisaApp.getGson()
                                        .toJson(fingpayMiniStatementResponse, type)
                                )
                                startActivity(intent)
                                finish()

                            }, 1500)
                        }
                    } else {
                        //Failed
                        Utils.showAlert(
                            getViewActivity(),
                            response ?: getString(R.string.some_thing_wants_wong),
                            "",
                            View.OnClickListener {
                            })
                    }
                }

            } else if (type == Constants.MicroAtm.MICRO_RESET_PIN) {
                status?.let {
                    if (status) {
                        //Success
                        Utils.showSuccessAlert(
                            getViewActivity(),
                            response ?: "Card pin has successfully reset"
                        )
                    } else {
                        //Failed
                        Utils.showAlert(
                            getViewActivity(),
                            response ?: getString(R.string.some_thing_wants_wong),
                            "",
                            View.OnClickListener {
                            })
                    }
                }
            } else if (type == Constants.MicroAtm.MICRO_CHANGE_PIN) {
                status?.let {
                    if (status) {
                        //Success
                        Utils.showSuccessAlert(
                            getViewActivity(),
                            response ?: "Card pin has successfully changed"
                        )
                    } else {
                        //Failed
                        Utils.showAlert(
                            getViewActivity(),
                            response ?: getString(R.string.some_thing_wants_wong),
                            "",
                            View.OnClickListener {
                            })
                    }
                }
            }

            if (!response.isNullOrEmpty()) {
                if (bankRrn.isNullOrEmpty()) bankRrn = ""
                if (transType.isNullOrEmpty()) transType = ""
                val s = """
                    Status :$status
                    Message : $response
                    Trans Amount : $transAmount
                    Balance Amount : $balAmount
                    Bank RRN : $bankRrn
                    Trand Type : $transType
                    Type : $type
                    TxnId : $txnid
                    Card Num :$cardNum
                    CardType :$cardType
                    Bank Name :$bankName
                    Terminal Id :$terminalId
                    """.trimIndent()
                Log.e(TAG, "micro atm result: $s")
            }

        }
        else if (requestCode == Constants.MicroAtm.RES_CODE &&requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(
                    getViewActivity(),
                    permissionsRequired[0]
                ) == PackageManager.PERMISSION_GRANTED
            ) {
            }
        } else if (requestCode == Constants.MicroAtm.RES_CODE && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getViewActivity(), "cancelled", Toast.LENGTH_SHORT).show()
        }else if(requestCode == 3030 && resultCode == Activity.RESULT_OK && data != null){
            googleFusedLocation.getUserLocation()
        }
    }

    private fun checkPermissions() {
        val permissions: List<String> = getUngrantedPermissions()
        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                getViewActivity(),
                permissions.toTypedArray(),
                PERMISSION_CALLBACK_CONSTANT
            )
            val editor: SharedPreferences.Editor = permissionStatus!!.edit()
            editor.putBoolean(permissionsRequired[0], true)
            editor.apply()
        }
    }

    private fun getUngrantedPermissions(): List<String> {
        val permissions: MutableList<String> = ArrayList()
        for (s in permissionsRequired) {
            if (ContextCompat.checkSelfPermission(
                    getViewActivity(),
                    s
                ) != PackageManager.PERMISSION_GRANTED
            ) permissions.add(s)
        }
        return permissions
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            var allgranted = false
            for (i in 0 until grantResults.size) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true
                } else {
                    allgranted = false
                    break
                }
            }
            when {
                allgranted -> {

                }
                !(getUngrantedPermissions() as ArrayList<*>).isNullOrEmpty() -> {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(getViewActivity())
                    builder.setTitle(getString(R.string.need_permissions))
                    builder.setMessage(getString(R.string.device_permission))
                    builder.setPositiveButton(
                        getString(R.string.grant),
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog.cancel()
                            ActivityCompat.requestPermissions(
                                getViewActivity(),
                                permissionsRequired,
                                PERMISSION_CALLBACK_CONSTANT
                            )
                        })
                    builder.setNegativeButton(
                        getString(R.string.cancel_btn),
                        DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                    builder.show()
                }
                else -> {
                    Toast.makeText(
                        getViewActivity(),
                        getString(R.string.unable_toget_permission),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    @SuppressLint("HardwareIds")
    fun getImei(): String {
        var imeiNumber = ""
        val telephonyManager: TelephonyManager =
            getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        imeiNumber = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                telephonyManager.imei
                    ?: Settings.Secure.getString(
                        applicationContext.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
            } catch (e: SecurityException) {
                Settings.Secure.getString(
                    applicationContext.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            }
        } else {
            telephonyManager.deviceId
                ?: Settings.Secure.getString(
                    applicationContext.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
        }

        return imeiNumber
    }

    override fun onMicroInitResponse() {
        val response = doRetriveModel().getDomain().microAtmInitTransactionResponse
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            doRetriveModel().getAtmRequest().mID = response.data.merchantId
            doRetriveModel().getAtmRequest().mPss = response.data.merchantPassword
            doRetriveModel().getAtmRequest().smID = response.data.superMerchentId
            doRetriveModel().getAtmRequest().amount = edtAmount.text.toString().trim()
            doRetriveModel().getAtmRequest().remarks = edtRemark.text.toString()
            doRetriveModel().getAtmRequest().number = edtMobileNumber.text.toString()
            doRetriveModel().getAtmRequest().imei = mImei
            doRetriveModel().getAtmRequest().lat = lat
            doRetriveModel().getAtmRequest().long =lng
            doRetriveModel().getAtmRequest().type = typeOfTransaction
            doRetriveModel().getAtmRequest().txnID = response.data.txnid //"fingpay${Date().time}"
            doRetriveModel().getAtmRequest().amountEditable = false
            presenter.doTransaction()
        } else {

            showToast(response.message)
        }
    }

    override fun onMicroUpdate() {
        if (intentMicroAtmReceipt != null) {

            Utils.showSuccessAlert(getViewActivity(), "Transaction success")
            Handler(Looper.getMainLooper()).postDelayed({

                Utils.dismissSuccessAlert()
                startActivity(intentMicroAtmReceipt)
                finish()

            }, 1500)
            //finish()
        }
    }
}