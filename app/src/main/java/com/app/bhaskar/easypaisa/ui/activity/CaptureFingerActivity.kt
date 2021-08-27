package com.app.bhaskar.easypaisa.ui.activity

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.model.piddata.PidData
import com.app.bhaskar.easypaisa.mvp.model.CaptureFingerModel
import com.app.bhaskar.easypaisa.mvp.presenter.CaptureFingerPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.CaptureFingerPresenterImpl
import com.app.bhaskar.easypaisa.request_model.AepsRequest
import com.app.bhaskar.easypaisa.response_model.FingPayAepsTxnResponse
import com.app.bhaskar.easypaisa.response_model.FingpayMiniStatementResponse
import com.app.bhaskar.easypaisa.response_model.SednOtpEkycResponse
import com.app.bhaskar.easypaisa.ui.listener.OnUserLocationListener
import com.app.bhaskar.easypaisa.ui.service.GoogleFuesedLocationService
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.google.gson.reflect.TypeToken
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_capture_finger.*
import kotlinx.android.synthetic.main.activity_capture_finger.linearLoginOtp
import kotlinx.android.synthetic.main.activity_capture_finger.textInputLayoutOtp
import kotlinx.android.synthetic.main.activity_capture_finger.tvOtpTimeRemain
import kotlinx.android.synthetic.main.activity_capture_finger.tvOtpTimeRemainLbl
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.toolbar_simple.*
import org.json.JSONException
import org.json.XML
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CaptureFingerActivity : BaseActivity(), CaptureFingerPresenter.CaptureFingerView {

    override fun doRetriveModel(): CaptureFingerModel {
        return model
    }

    companion object {
        const val TAG = "CaptureFingerActivity"
    }

    private var responseOtpSent: SednOtpEkycResponse? = null
    private var ipAddress: String = ""
    private var bioDeviceresult: String? = null
    private var pidData: PidData? = null
    private lateinit var googleFusedLocation: GoogleFuesedLocationService

    //    private var pidDataMorpho: PidDataMorpho? = null
    @Inject
    lateinit var presenter: CaptureFingerPresenterImpl
    lateinit var model: CaptureFingerModel
    private var aepsRequest: AepsRequest? = null
    private var devicePackageName = "com.mantra.rdservice"
    private var deviceSelected = "MANTRA"
    private var deviceSelectedServer = "MANTRA_PROTOBUF"
    private var serializer: Serializer? = null
    private var arrayListDevice = ArrayList<String>()
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_finger)
        tvToolbarTitle.text = "AePS transaction"
        serializer = Persister()
        ipAddress = Utils.getIPAddress(true)
        model = CaptureFingerModel(getViewActivity())
        presenter = CaptureFingerPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)

        initView()
        googleFusedLocation = GoogleFuesedLocationService(getViewActivity(), object :
            OnUserLocationListener {
            override fun onUserLocationSuccess(location: Location) {
                if (EasyPaisaApp.getUserLatLng() == null) {
                    showToast("User location not found, please enable GPS")
                    return
                }
                doTransaction()
            }

            override fun onUserLocationFail(failMessage: String) {
            }

        }, true)
    }

    override fun onOTPSent() {
        responseOtpSent = doRetriveModel().getCaptureDomain().sendOtpEkycResponse

        if (responseOtpSent?.status == Constants.ApiResponse.RES_TXNOTP) {
            btnOtpVerify.visibility = View.GONE
            linearLoginOtp.visibility = View.VISIBLE
            btnProceed.visibility = View.VISIBLE
            tvOtpTimeRemainLbl.text = getString(R.string.time_remain_to_verifiy)
            textInputLayoutOtp.visibility =View.VISIBLE

            Toast.makeText(
                getViewActivity(),
                responseOtpSent?.message.toString(),
                Toast.LENGTH_SHORT
            ).show()
            updateUI(true)
        } else {
            Utils.showAlert(
                getViewActivity(),
                responseOtpSent?.message.toString(),
                "",
                View.OnClickListener {
                    finish()
                })
        }

    }


    private fun startTimer() {
        tvOtpTimeRemainLbl.text = getString(R.string.time_remain_to_verifiy)
        countDownTimer = object : CountDownTimer(3 * 60 * 1000, 1000) {
            override fun onFinish() {
                updateUI(false)
            }

            override fun onTick(millisUntilFinished: Long) {
                val time = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                    ),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    )
                )
                tvOtpTimeRemain.text = time
            }
        }
        countDownTimer!!.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }

    private fun stopTimer() {
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
            countDownTimer = null
        }
    }

    private fun updateUI(b: Boolean) {
        if (!b) {
            stopTimer()
            tvOtpTimeRemain.text = getString(R.string.resend_otp)
            tvOtpTimeRemainLbl.text = getString(R.string.don_t_receive_otp)
        } else {
            tvOtpTimeRemainLbl.text = getString(R.string.time_remain_to_verifiy)
            startTimer()
        }
    }

    private fun fillAePSTxnDetails() {
        if (aepsRequest?.transactionFor == getString(R.string.ser_auth_biometric)) {
            tvToolbarTitle.text = "eKYC Authentication"
            tvTxnType.text = "Biometric authentication required in order to access services"
            groupDetail.visibility = View.GONE
            btnOtpVerify.visibility = View.VISIBLE
            btnProceed.visibility = View.GONE
            doRetriveModel().getSendOtpRequest().token =
                EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
            doRetriveModel().getSendOtpRequest().userId =
                EasyPaisaApp.getLoggedInUser()?.id.toString()
            doRetriveModel().getSendOtpRequest().transactionType = "useronboardotp"
            presenter.doSendOtp()
        } else {
            tvMobileNumber.text = aepsRequest?.mobile
            tvAadhaarNumber.text = aepsRequest?.aadharNo
            tvBankName.text = aepsRequest?.bank?.bankName
            tvAmount.text = aepsRequest?.amount
        }

        /*when (aepsRequest?.aepsServiceFor) {
            Constants.AvailableService.SERVICE_ICICI_AEPS -> {
                arrayListDevice.add("MANTRA")
                arrayListDevice.add("TATVIK")
                arrayListDevice.add("STARTEK")
            }
            else -> {
                arrayListDevice.add("MANTRA")
                arrayListDevice.add("MORPHO")
                arrayListDevice.add("TATVIK")
                arrayListDevice.add("STARTEK")
            }
        }*/

        arrayListDevice.add("MANTRA")
        arrayListDevice.add("MORPHO")
        arrayListDevice.add("TATVIK")
        arrayListDevice.add("STARTEK")

        when (aepsRequest?.transactionFor) {
            getString(R.string.ser_withdraw) -> {
                tvTxnType.text = "Cash Withdrawal"
            }
            getString(R.string.ser_mini_statement) -> {
                tvTxnType.text = "Mini Statement"
                tvLabelAmount.visibility = View.GONE
                tvAmount.visibility = View.GONE
            }
            getString(R.string.ser_aadhar_pay) -> {
                tvTxnType.text = "Aadhar Pay"
            }
            getString(R.string.ser_balance) -> {
                tvTxnType.text = "Balance Inquiry"
                tvLabelAmount.visibility = View.GONE
                tvAmount.visibility = View.GONE
            }
            getString(R.string.ser_deposit) -> {
                tvTxnType.text = "Cash Deposit"
            }
            else -> ""
        }
    }

    private fun initView() {

        if (intent.hasExtra(Constants.UI.AEPSREQUEST)) {
            val type = object : TypeToken<AepsRequest>() {}.type
            val jsonData = intent.getStringExtra(Constants.UI.AEPSREQUEST)
            aepsRequest = EasyPaisaApp.getGson().fromJson(jsonData, type)
            fillAePSTxnDetails()
        } else {
            showToast(getString(R.string.some_thing_wants_wong))
            finish()
        }
        val itemsArrayAdapter =
            ArrayAdapter<String>(
                this@CaptureFingerActivity,
                android.R.layout.simple_spinner_item,
                arrayListDevice
            )
        itemsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFingerDevice.adapter = itemsArrayAdapter
        spinnerFingerDevice.prompt = "Select Scanner Device"
        spinnerFingerDevice.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        devicePackageName = "com.mantra.rdservice"
                        deviceSelected = "MANTRA"
                        deviceSelectedServer = "MANTRA_PROTOBUF"
                    }
                    1 -> {
                        devicePackageName = "com.scl.rdservice"
                        deviceSelected = "MORPHO"
                        deviceSelectedServer = "MORPHO_PROTOBUF"
                    }
                    2 -> {
                        devicePackageName = "tmf20.bio.tatvik.com.tmf20client"
                        deviceSelected = "TATVIK"
                        deviceSelectedServer = "TATVIK_PROTOBUF"
                    }
                    3 -> {
                        devicePackageName = "com.acpl.registersdk"
                        deviceSelected = "STARTEK"
                        deviceSelectedServer = "STARTEK_PROTOBUF"
                    }
                    else -> {
                        devicePackageName = "com.mantra.rdservice"
                        deviceSelected = "MANTRA"
                        deviceSelectedServer = "MANTRA_PROTOBUF"
                    }
                }
            }
        }

        btnProceed.setOnClickListener {
            if (EasyPaisaApp.getUserLatLng() == null) {
                googleFusedLocation.needToShowDialog = true
                googleFusedLocation.accessUserCurrentLocation()
                return@setOnClickListener
            }
            doTransaction()
//            getFingerData("dhruv")

        }
        ivHomeBack.setOnClickListener {
            onBackPressed()
        }

        tvOtpTimeRemain.setOnClickListener {
            if (tvOtpTimeRemain.text == getString(R.string.resend_otp)) {
                textInputLayoutOtp.editText!!.setText("")
                btnOtpVerify.visibility = View.VISIBLE
                btnProceed.visibility = View.GONE
                doRetriveModel().getSendOtpRequest().token =
                    EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
                doRetriveModel().getSendOtpRequest().userId =
                    EasyPaisaApp.getLoggedInUser()?.id.toString()
                doRetriveModel().getSendOtpRequest().transactionType = "useronboardotp"
                presenter.doSendOtp()
            }
        }
    }

    private fun doTransaction() {
        if (aepsRequest?.transactionFor == getString(R.string.ser_auth_biometric)) {
            when {
                textInputLayoutOtp.editText!!.text.toString().trim().isEmpty() -> {
                    textInputLayoutOtp.error = "Please enter OTP"
                    return
                }
                textInputLayoutOtp.editText!!.text.toString().trim().length <= 4 -> {
                    textInputLayoutOtp.error = "Please enter valid OTP"
                    return
                }
                else -> {
                    textInputLayoutOtp.isErrorEnabled  =false
                }
            }
        }

        if (!Utils.isAppInstalled(this@CaptureFingerActivity, devicePackageName)) {
            return
        }
        val pidDataProtoBuff =
            if (aepsRequest?.aepsServiceFor == Constants.AvailableService.SERVICE_ICICI_AEPS) "0" else "1"
        presenter.scanFingerprint(devicePackageName, pidDataProtoBuff,aepsRequest?.transactionFor)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                3030 -> {
                    googleFusedLocation.getUserLocation()
                }
                Constants.UI.FINGERSCAN_CODE -> {
                    if (data != null) {
                        bioDeviceresult = data.getStringExtra("PID_DATA")
                        if (bioDeviceresult != null) {
                            try {
                                pidData = serializer!!.read(PidData::class.java, bioDeviceresult)
                                if (isValidResponse(bioDeviceresult!!)) {
                                    getFingerData()
                                }
                            } catch (e: Exception) {
                                bioDeviceresult = null
                                Utils.showAlert(
                                    this@CaptureFingerActivity,
                                    getString(R.string.unable_capture),
                                    "Easy Paisa",
                                    View.OnClickListener {

                                    })
                            }
                        }
                    }
                }
            }
        } else {
            showToast(getString(R.string.not_fingerprint_captured))
        }
    }

    private fun getFingerData() {
        if (aepsRequest?.aepsServiceFor == Constants.AvailableService.SERVICE_ICICI_AEPS) {
            if (aepsRequest?.transactionFor == getString(R.string.ser_auth_biometric)) {
                verifyAndProceedForEkycAuth()
                return
            }
            val request = doRetriveModel().getFingPayAepsRequest()
            request.token = EasyPaisaApp.getLoggedInUser()!!.apptoken
            request.userId = EasyPaisaApp.getLoggedInUser()!!.id.toString()
            request.adhaarNumber = aepsRequest?.aadharNo!!
            request.mobileNumber = aepsRequest?.mobile!!
            request.latitude = EasyPaisaApp.getUserLatLng()?.latitude.toString()
            request.longitude = EasyPaisaApp.getUserLatLng()?.longitude.toString()
            request.ip = ipAddress
            request.name = aepsRequest?.name!!
            request.transactionAmount = aepsRequest?.amount!!
            request.biodata = bioDeviceresult!!
            request.transactionType =
                when (aepsRequest?.transactionFor) {
                    getString(R.string.ser_withdraw) -> {
                        "CW"
                    }
                    getString(R.string.ser_mini_statement) -> {
                        "MS"
                    }
                    getString(R.string.ser_aadhar_pay) -> {
                        "M"
                    }
                    getString(R.string.ser_balance) -> {
                        "BE"
                    }
                    getString(R.string.ser_deposit) -> {
                        "CD"
                    }
                    else -> ""
                }
            request.bankId = aepsRequest?.bank!!.iinno
            request.device = deviceSelectedServer

            when (request.transactionType) {
                "MS" -> {
                    presenter.apiCallforAepsFinpayTxnMiniSt()
                }
                "CD" -> {
                    presenter.apiCallforAepsFinpayTxn()
                }
                else -> {
                    presenter.apiCallforAepsIciciEasyPayTxn()
                }
            }
        } else if (aepsRequest?.aepsServiceFor == Constants.AvailableService.SERVICE_EASYPAY_AEPS) {
            val request = doRetriveModel().getFingPayAepsRequest()
            request.token = EasyPaisaApp.getLoggedInUser()!!.apptoken
            request.userId = EasyPaisaApp.getLoggedInUser()!!.id.toString()
            request.adhaarNumber = aepsRequest?.aadharNo!!
            request.mobileNumber = aepsRequest?.mobile!!
            request.transactionAmount = aepsRequest?.amount!!
            request.name = aepsRequest?.name!!
            request.biodata = bioDeviceresult!!
            request.latitude = EasyPaisaApp.getUserLatLng()?.latitude.toString()
            request.longitude = EasyPaisaApp.getUserLatLng()?.longitude.toString()
            request.ip = ipAddress
            request.transactionType =
                when (aepsRequest?.transactionFor) {
                    getString(R.string.ser_withdraw) -> {
                        "CW"
                    }
                    getString(R.string.ser_mini_statement) -> {
                        "MS"
                    }
                    getString(R.string.ser_aadhar_pay) -> {
                        "M"
                    }
                    getString(R.string.ser_balance) -> {
                        "BE"
                    }
                    getString(R.string.ser_deposit) -> {
                        "CD"
                    }
                    else -> ""
                }
            request.bankId = aepsRequest?.bank!!.iinno
            request.device = deviceSelectedServer
            if (request.transactionType == "MS") {
                presenter.apiCallforAepsFinpayTxnMiniSt()
            } else {
                presenter.apiCallforYesAeps()
            }
        }
    }

    private fun verifyAndProceedForEkycAuth() {
        doRetriveModel().getVerifyOtpEkycRequest().userId =
            EasyPaisaApp.getLoggedInUser()?.id.toString()
        doRetriveModel().getVerifyOtpEkycRequest().token =
            EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
        doRetriveModel().getVerifyOtpEkycRequest().otp =
            textInputLayoutOtp.editText?.text.toString().trim()
        doRetriveModel().getVerifyOtpEkycRequest().encodeFPTxnId =
            responseOtpSent?.encodeFPTxnId.toString()
        doRetriveModel().getVerifyOtpEkycRequest().primaryKeyId =
            responseOtpSent?.primaryKeyId.toString()
        doRetriveModel().getVerifyOtpEkycRequest().transactionType = "useronboardvalidate"
        presenter.doVerifyOtp()
    }

    override fun onOtpVerificationCompleted() {
        val response = doRetriveModel().getCaptureDomain().baseResponse
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            bioDeviceresult?.let {
                doRetriveModel().getEkycAuthenticationRequest().biodata = it
                doRetriveModel().getEkycAuthenticationRequest().userId =
                    EasyPaisaApp.getLoggedInUser()?.id.toString()
                doRetriveModel().getEkycAuthenticationRequest().token =
                    EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
                doRetriveModel().getEkycAuthenticationRequest().transactionType = "useronboardekyc"
                doRetriveModel().getEkycAuthenticationRequest().encodeFPTxnId =
                    responseOtpSent?.encodeFPTxnId.toString()
                doRetriveModel().getEkycAuthenticationRequest().primaryKeyId =
                    responseOtpSent?.primaryKeyId.toString()
                presenter.proceedForEkycAuth()
            }
        } else {
            Utils.showAlert(getViewActivity(), response.message, "", View.OnClickListener { })
        }
    }


    override fun onAuthenticationCompleted() {
        val response = doRetriveModel().getCaptureDomain().baseResponse
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            val userRequiredData = EasyPaisaApp.getUserRequiredData()
            userRequiredData?.let {
                it.agent.everify = "success"
                EasyPaisaApp.setUserRequiredData(it)
            }
            EasyPaisaApp.setNeedUpdateUserDetail(true)
            Utils.showSuccessAlert(getViewActivity(),
                "Congratulations, eKYC verification has been success, You can now access all the services.",
                View
                    .OnClickListener {
                        finish()
                    })

        } else {
            Utils.showAlert(getViewActivity(), response.message, "", View.OnClickListener { })
        }

    }

    override fun getViewActivity(): Activity {
        return this@CaptureFingerActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    override fun onFingpayAepsTxnDone() {
        val response = doRetriveModel().getCaptureDomain().fingPayAepsTxnResponse
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            val intent = Intent(getViewActivity(), AePSTransactionReceiptActivity::class.java)
            val typeRes = object : TypeToken<FingPayAepsTxnResponse>() {}.type
            val typeReq = object : TypeToken<AepsRequest>() {}.type
            intent.putExtra(
                Constants.UI.AEPS_RESPONSE,
                EasyPaisaApp.getGson().toJson(response, typeRes)
            )
            intent.putExtra(
                Constants.UI.AEPS_REQUEST,
                EasyPaisaApp.getGson().toJson(aepsRequest, typeReq)
            )
            startActivity(intent)
            finish()
        } else {
            Utils.showAlert(getViewActivity(), response.message, "", View.OnClickListener { })
        }
    }

    private fun isValidResponse(data: String): Boolean {
        try {
            val objJson = XML.toJSONObject(data)
            val pidJson = objJson.optJSONObject("PidData")
            if (pidJson != null) {
                val respJson = pidJson.optJSONObject("Resp")
                if (respJson != null) {
                    val code = respJson.optInt("errCode")
                    if (code > 0) {
                        Utils.showAlert(
                            getViewActivity(),
                            respJson.getString("errInfo"),
                            "",
                            View.OnClickListener {

                            })
                        return false
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return true
    }

    override fun onFingpayAepsTxnMiniStmt() {
        val response = doRetriveModel().getCaptureDomain().fingpayMiniStatementResponse
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            val intent = Intent(getViewActivity(), MiniStatementActivity::class.java)
            val type = object : TypeToken<FingpayMiniStatementResponse>() {}.type
            intent.putExtra(
                Constants.UI.AEPS_MINI_RESPONSE,
                EasyPaisaApp.getGson().toJson(response, type)
            )
            startActivity(intent)
            finish()
        } else {
            Utils.showAlert(getViewActivity(), response.message, "", View.OnClickListener { })
        }
    }
}
