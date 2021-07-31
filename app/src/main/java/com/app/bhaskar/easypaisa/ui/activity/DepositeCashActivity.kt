package com.app.bhaskar.easypaisa.ui.activity

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.DepositeActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.CashDepositePresenter
import com.app.bhaskar.easypaisa.mvp.presenter.DepositeActivityPresenterImpl
import com.app.bhaskar.easypaisa.response_model.DepositeOtpVerifyResponse
import com.app.bhaskar.easypaisa.response_model.RegOtpResponse
import com.app.bhaskar.easypaisa.response_model.UserRequiredDataResponse
import com.app.bhaskar.easypaisa.ui.listener.OnUserLocationListener
import com.app.bhaskar.easypaisa.ui.service.GoogleFuesedLocationService
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_deposite_cash.*
import kotlinx.android.synthetic.main.toolbar_simple.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DepositeCashActivity : BaseActivity(), CashDepositePresenter.CashDepositeView {

    companion object {
        const val TAG = "DepositeCashActivity"
        var PROCESS_TYPE = 0
    }

    private var accountHolderName: String = ""
    private var countDownTimer: CountDownTimer? = null
    private lateinit var model: DepositeActivityModel
    private var selectedBank: UserRequiredDataResponse.Aepsbank? = null
    private lateinit var googleFusedLocation: GoogleFuesedLocationService

    @Inject
    lateinit var presenter: DepositeActivityPresenterImpl
    private var mTxnId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposite_cash)
        model = DepositeActivityModel(getViewActivity())
        presenter = DepositeActivityPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        tvToolbarTitle.text = "Cash Deposit"
        initView()
    }

    private fun initView() {
        inputEdtMobile.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val mobNumber = s.toString().trim()
                if (mobNumber.isEmpty())
                    return
                if (mobNumber.length != 10) {
                    PROCESS_TYPE =0
                    textInputLayoutOtp.editText!!.setText("")
                    gpOTP.visibility = View.GONE
                    btnDeposit.visibility = View.GONE
                    btnGetOtp.visibility = View.VISIBLE
                    btnDeposit.text = getString(R.string.verify_otp)
                    textInputLayoutHolderName.editText!!.setText("")
                    textInputLayoutHolderName.visibility =View.GONE
                    accountHolderName =""
                }
            }
        })
        googleFusedLocation = GoogleFuesedLocationService(getViewActivity(), object :
            OnUserLocationListener {
            override fun onUserLocationSuccess(location: Location) {
                if (EasyPaisaApp.getUserLatLng() == null) {
                    showToast("User location not found, please enable GPS")
                    return
                }
                Toast.makeText(
                    getViewActivity(),
                    "Location successfully found, you can now continue for cash deposit.",
                    Toast.LENGTH_LONG
                ).show();
            }

            override fun onUserLocationFail(failMessage: String) {
            }

        }, true)

        ivHomeBack.setOnClickListener {
            onBackPressed()
        }
        edtSelectBank.setOnClickListener {
            startActivityForResult(
                Intent(
                    getViewActivity(),
                    SelectBankActivty::class.java
                ).putExtra("selectedAepsTransaction", "ser_withdraw"), Constants.UI.SELECT_BANK
            )
        }
        btnGetOtp.setOnClickListener {
            if (isValide) {
                if (EasyPaisaApp.getUserLatLng() == null) {
                    googleFusedLocation.needToShowDialog = true
                    googleFusedLocation.accessUserCurrentLocation()
                    return@setOnClickListener
                }
                doRetriveModel().getOtpRequest().accountNumber =
                    inputEdtAccountNo.text.toString().trim()
                doRetriveModel().getOtpRequest().iinno = selectedBank!!.iinno
                doRetriveModel().getOtpRequest().mobileNumber =
                    inputEdtMobile.text.toString().trim()
                doRetriveModel().getOtpRequest().transactionAmount =
                    aepssearch_edtamount.text.toString().trim()
                doRetriveModel().getOtpRequest().transactionType = "SOTP"
                doRetriveModel().getOtpRequest().token =
                    EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
                doRetriveModel().getOtpRequest().userId =
                    EasyPaisaApp.getLoggedInUser()?.id.toString()
                doRetriveModel().getOtpRequest().latitude =
                    EasyPaisaApp.getUserLatLng()?.latitude.toString()
                doRetriveModel().getOtpRequest().longitude =
                    EasyPaisaApp.getUserLatLng()?.longitude.toString()
                presenter.doGetOtp()
            }
        }

        btnDeposit.setOnClickListener {
            if (isValide) {
                if (EasyPaisaApp.getUserLatLng() == null) {
                    googleFusedLocation.needToShowDialog = true
                    googleFusedLocation.accessUserCurrentLocation()
                    return@setOnClickListener
                }
                when (PROCESS_TYPE) {
                    1 -> {
                        doRetriveModel().getVerifyOtpRequest().accountNumber =
                            inputEdtAccountNo.text.toString().trim()
                        doRetriveModel().getVerifyOtpRequest().iinno = selectedBank!!.iinno
                        doRetriveModel().getVerifyOtpRequest().mobileNumber =
                            inputEdtMobile.text.toString().trim()
                        doRetriveModel().getVerifyOtpRequest().transactionAmount =
                            aepssearch_edtamount.text.toString().trim()
                        doRetriveModel().getVerifyOtpRequest().transactionType = "OTPV"
                        doRetriveModel().getVerifyOtpRequest().token =
                            EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
                        doRetriveModel().getVerifyOtpRequest().userId =
                            EasyPaisaApp.getLoggedInUser()?.id.toString()
                        doRetriveModel().getVerifyOtpRequest().latitude =
                            EasyPaisaApp.getUserLatLng()?.latitude.toString()
                        doRetriveModel().getVerifyOtpRequest().longitude =
                            EasyPaisaApp.getUserLatLng()?.longitude.toString()
                        doRetriveModel().getVerifyOtpRequest().txnid = mTxnId
                        doRetriveModel().getVerifyOtpRequest().otp =
                            textInputLayoutOtp.editText!!.text.toString().trim()
                        doRetriveModel().getVerifyOtpRequest().name =
                            ""
                        presenter.doVerifyOtp()
                    }
                    2 -> {
                        Utils.showAlert(getViewActivity(),"Are you sure you want to transfer ${Utils.formatAmount(textInputLayoutEnterAmount.editText!!.text.toString().trim().toDouble())} to ${accountHolderName} ?","",View.OnClickListener {
                            doTransferMoney()
                        },View.OnClickListener {  })
                    }
                }

            }
        }

        tvAmount100.setOnClickListener {
            aepssearch_edtamount.setText("500")
        }
        tvAmount1000.setOnClickListener {
            aepssearch_edtamount.setText("1000")
        }
        tvAmount2000.setOnClickListener {
            aepssearch_edtamount.setText("2000")
        }
        tvAmount3000.setOnClickListener {
            aepssearch_edtamount.setText("3000")
        }

        tvOtpTimeRemain.setOnClickListener {
            if (tvOtpTimeRemain.text == getString(R.string.resend_otp)) {
                PROCESS_TYPE = 0
                textInputLayoutOtp.editText!!.setText("")
                gpOTP.visibility = View.GONE
                btnDeposit.visibility = View.GONE
                btnGetOtp.visibility = View.VISIBLE
                btnDeposit.text = getString(R.string.verify_otp)
                textInputLayoutHolderName.editText!!.setText("")
                textInputLayoutHolderName.visibility =View.GONE
                accountHolderName =""
                if (isValide) {
                    if (EasyPaisaApp.getUserLatLng() == null) {
                        googleFusedLocation.needToShowDialog = true
                        googleFusedLocation.accessUserCurrentLocation()
                        return@setOnClickListener
                    }
                    doRetriveModel().getOtpRequest().accountNumber =
                        inputEdtAccountNo.text.toString().trim()
                    doRetriveModel().getOtpRequest().iinno = selectedBank!!.iinno
                    doRetriveModel().getOtpRequest().mobileNumber =
                        inputEdtMobile.text.toString().trim()
                    doRetriveModel().getOtpRequest().transactionAmount =
                        aepssearch_edtamount.text.toString().trim()
                    doRetriveModel().getOtpRequest().transactionType = "SOTP"
                    doRetriveModel().getOtpRequest().token =
                        EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
                    doRetriveModel().getOtpRequest().userId =
                        EasyPaisaApp.getLoggedInUser()?.id.toString()
                    doRetriveModel().getOtpRequest().latitude =
                        EasyPaisaApp.getUserLatLng()?.latitude.toString()
                    doRetriveModel().getOtpRequest().longitude =
                        EasyPaisaApp.getUserLatLng()?.longitude.toString()
                    presenter.doGetOtp()
                }
            }
        }
    }

    override fun doRetriveModel(): DepositeActivityModel {
        return model
    }

    override fun onOtpSent(it: RegOtpResponse) {
        if (it.status == Constants.ApiResponse.RES_SUCCESS) {
            showToast(it.message)
            PROCESS_TYPE = 1
            btnGetOtp.visibility = View.GONE
            btnDeposit.visibility = View.VISIBLE
            gpOTP.visibility = View.VISIBLE
            mTxnId = it.txnid
            btnDeposit.text = getString(R.string.verify_otp)
            startTimer()
        } else {
            Utils.showAlert(getViewActivity(), it.message, "Easy paisa", View.OnClickListener {
            })
        }
    }

    override fun onOtpVerified(it: DepositeOtpVerifyResponse) {
        if (it.status == Constants.ApiResponse.RES_SUCCESS) {
            if (it.benename.isEmpty()) {
                showToast("Account Holder name not found ${getString(R.string.some_thing_wants_wong)}")
                return
            }
            PROCESS_TYPE = 2
            textInputLayoutHolderName.visibility = View.VISIBLE
            textInputLayoutHolderName.editText!!.setText(it.benename)
            btnDeposit.text = getString(R.string.deposit_cash)
            accountHolderName = it.benename
            linearLoginOtp.visibility =View.GONE
            stopTimer()
        } else {
            updateUI(false)
            btnDeposit.visibility = View.GONE
            btnDeposit.text = getString(R.string.verify_otp)
            btnGetOtp.visibility = View.VISIBLE
            gpOTP.visibility = View.GONE
            textInputLayoutHolderName.visibility = View.GONE
            textInputLayoutHolderName.editText!!.setText("")
            accountHolderName =""
            Utils.showAlert(getViewActivity(), it.message, "Easy paisa", View.OnClickListener {
            })
        }
    }

    private fun doTransferMoney() {
        doRetriveModel().getVerifyOtpRequest().accountNumber =
            inputEdtAccountNo.text.toString().trim()
        doRetriveModel().getVerifyOtpRequest().iinno = selectedBank!!.iinno
        doRetriveModel().getVerifyOtpRequest().mobileNumber =
            inputEdtMobile.text.toString().trim()
        doRetriveModel().getVerifyOtpRequest().transactionAmount =
            aepssearch_edtamount.text.toString().trim()
        doRetriveModel().getVerifyOtpRequest().name =
            accountHolderName
        doRetriveModel().getVerifyOtpRequest().transactionType = "CD"
        doRetriveModel().getVerifyOtpRequest().token =
            EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
        doRetriveModel().getVerifyOtpRequest().userId =
            EasyPaisaApp.getLoggedInUser()?.id.toString()
        doRetriveModel().getVerifyOtpRequest().latitude =
            EasyPaisaApp.getUserLatLng()?.latitude.toString()
        doRetriveModel().getVerifyOtpRequest().longitude =
            EasyPaisaApp.getUserLatLng()?.longitude.toString()
        doRetriveModel().getVerifyOtpRequest().txnid = mTxnId
        doRetriveModel().getVerifyOtpRequest().otp =
            textInputLayoutOtp.editText!!.text.toString().trim()
        presenter.doCashDeposit()
    }

    override fun onCashDeposit(response: RegOtpResponse) {
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            Utils.showSuccessAlert(getViewActivity(),
                response.message, View.OnClickListener {
                    finish()
                })
        } else {
            Utils.showAlert(
                getViewActivity(),
                response.message,
                "Easy Paisa",
                View.OnClickListener {
                })
        }
    }

    override fun getViewActivity(): Activity {
        return this@DepositeCashActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.UI.SELECT_BANK && resultCode == Activity.RESULT_OK) {
            selectedBank =
                data?.getSerializableExtra("selectedBank") as UserRequiredDataResponse.Aepsbank?
            selectedBank?.let {
                edtSelectBank.setText(it.bankName)
            }
        } else if (requestCode == 3030 && resultCode == Activity.RESULT_OK) {
            googleFusedLocation.getUserLocation()
        }
    }

    private val isValide: Boolean
        get() {
            if (textInputLayoutAepsMobNo.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutAepsMobNo.error = "Please enter mobile number"
                return false
            } else if (textInputLayoutAepsMobNo.editText!!.text.toString().trim().length != 10) {
                textInputLayoutAepsMobNo.error = "Please enter valid mobile number"
                return false
            } else if (textInputLayoutAccountNo.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutAccountNo.error = "Please enter account number"
                return false
            } else if (edtSelectBank.text.toString().trim().isEmpty()) {
                edtSelectBank.error = "Please select bank"
                return false
            } else if (textInputLayoutEnterAmount.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutEnterAmount.error = "Please enter amount"
                return false
            } else if (textInputLayoutEnterAmount.editText!!.text.toString().isNotEmpty() &&
                textInputLayoutEnterAmount.editText!!.text.toString().toDouble() < 100
            ) {
                textInputLayoutEnterAmount.error = "Amount should be 100 or more then 100"
                return false
            } else if (PROCESS_TYPE == 1 && textInputLayoutOtp.editText!!.text.toString().trim()
                    .isEmpty()
            ) {
                textInputLayoutOtp.error = "Please enter OTP"
                return false
            }
            edtSelectBank.error = null
            textInputLayoutAepsMobNo.isErrorEnabled = false
            textInputLayoutOtp.isErrorEnabled = false
            textInputLayoutEnterAmount.isErrorEnabled = false
            textInputLayoutAccountNo.isErrorEnabled = false
            return true
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

    private fun stopTimer() {
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
            countDownTimer = null
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
}