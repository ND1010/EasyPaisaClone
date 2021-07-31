package com.app.bhaskar.easypaisa.ui.activity

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.RegistrationActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.RegistrationPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.RegistrationPresenterImpl
import com.app.bhaskar.easypaisa.response_model.RegOtpResponse
import com.app.bhaskar.easypaisa.response_model.UserRegistrationResponse
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject


class RegistrationActivity : BaseActivity(), RegistrationPresenter.RegistrationView {

    companion object {
        const val TAG = "RegistrationActivity "
        var PROCESS_TYPE = 0
    }

    private var countDownTimer: CountDownTimer? = null
    private lateinit var model: RegistrationActivityModel

    @Inject
    lateinit var presenter: RegistrationPresenterImpl
    lateinit var matcher: Matcher
    private var arraySpUserType = ArrayList<String>()
    private var mOtp: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        model = RegistrationActivityModel(getViewActivity())
        presenter = RegistrationPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        tvLoginHere.setOnClickListener {
            onBackPressed()
        }
        PROCESS_TYPE = 0
        updateUI(false)
        tvOtpTimeRemain.setOnClickListener {
            if (tvOtpTimeRemain.text == getString(R.string.resend_otp)) {
                textInputLayoutOtp.editText!!.setText("")
                doRetriveModel().getOtpRequest().mobile =
                    textInputLayoutRegMobileNo.editText!!.text.toString().trim()
                presenter.doGetOtp()
            }
        }
        ivCancel.setOnClickListener {
            Utils.showAlert(
                getViewActivity(),
                "Are you sure you wants close registration process?",
                "Easy Paisa",
                View.OnClickListener {
                    resetRegistration()
                },
                View.OnClickListener { })
        }

        arraySpUserType.add("Select Category")
        arraySpUserType.add("Retailer")
        arraySpUserType.add("Distributor")
        arraySpUserType.add("Super Distributor")
        arraySpUserType.add("B2B Portal Partner")

        btnGetOtp.setOnClickListener {
            doRetriveModel().getOtpRequest().mobile =
                textInputLayoutRegMobileNo.editText!!.text.toString().trim()
            presenter.doGetOtp()
        }

        btnRegistration.setOnClickListener {
            if (isValide) {
                when (PROCESS_TYPE) {
                    0 -> {
                        doRetriveModel().getOtpRequest().mobile =
                            textInputLayoutRegMobileNo.editText!!.text.toString().trim()
                        presenter.doGetOtp()
                    }
                    1 -> {
                        mOtp = textInputLayoutOtp.editText!!.text.toString().trim()
                        if (mOtp.isEmpty() || mOtp.length < 4){
                            textInputLayoutOtp.error = "Invalid OTP."
                            return@setOnClickListener
                        }
                        doRetriveModel().getRegistrationRequest().name =
                            textInputLayoutRegFullName.editText!!.text.toString().trim()
                        doRetriveModel().getRegistrationRequest().mobile =
                            textInputLayoutRegMobileNo.editText!!.text.toString().trim()
                        doRetriveModel().getRegistrationRequest().email =
                            textInputLayoutRegEmail.editText!!.text.toString().trim()
                        doRetriveModel().getRegistrationRequest().otp = mOtp
                        presenter.doRegisterUser()
                    }
                }
            }
            /*if (isValide){
                doRetriveModel().getRegistrationRequest().name = textInputLayoutRegFullName.editText!!.text.toString().trim()
                doRetriveModel().getRegistrationRequest().mobile = textInputLayoutRegMobileNo.editText!!.text.toString().trim()
                doRetriveModel().getRegistrationRequest().email = textInputLayoutRegEmail.editText!!.text.toString().trim()
                *//*doRetriveModel().getRegistrationRequest().pincode = textInputLayoutRegPinCode.editText!!.text.toString().trim()
                doRetriveModel().getRegistrationRequest().pancard = textInputLayoutRegPanCardNo.editText!!.text.toString().trim()
                doRetriveModel().getRegistrationRequest().aadharcard = textInputLayoutRegAadhaarNo.editText!!.text.toString().trim()
                doRetriveModel().getRegistrationRequest().state = textInputLayoutRegState.editText!!.text.toString().trim()
                doRetriveModel().getRegistrationRequest().city = textInputLayoutRegCity.editText!!.text.toString().trim()
                doRetriveModel().getRegistrationRequest().address = textInputLayoutRegAddress.editText!!.text.toString().trim()*//*
                presenter.doRegisterUser()
            }*/
        }

        val itemsArrayAdapter = object :
            ArrayAdapter<String>(
                applicationContext,
                android.R.layout.simple_spinner_item,
                arraySpUserType
            ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }
        }
        itemsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = itemsArrayAdapter

    }

    private fun resetRegistration() {
        PROCESS_TYPE = 0
        groupRegFields.visibility = View.VISIBLE
        textInputLayoutOtp.visibility = View.GONE
        textInputLayoutOtp.editText!!.setText("")
        btnRegistration.text = getString(R.string.get_otp)
        ivCancel.visibility = View.GONE
        linearLoginOtp.visibility = View.GONE
        tvLabelreghere.text =getString(R.string.register_here_)
        updateUI(false)
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

    override fun doRetriveModel(): RegistrationActivityModel {
        return model
    }

    override fun onRegistrationDone(it: UserRegistrationResponse) {
        if (it.status == Constants.ApiResponse.RES_SUCCESS) {
            startTimer()
            showToast(it.message)
            onBackPressed()
        } else {
            Utils.showAlert(
                getViewActivity(),
                it.message,
                "Registration Alert",
                View.OnClickListener {
                })
        }
    }

    override fun getViewActivity(): Activity {
        return this@RegistrationActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    val isValide: Boolean
        get() {
            if (textInputLayoutRegFullName.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutRegFullName.error = "Please enter your full name"
                textInputLayoutRegFullName.editText!!.requestFocus()
                return false
            }

            if (textInputLayoutRegMobileNo.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutRegMobileNo.error = "Please enter mobile number"
                textInputLayoutRegMobileNo.editText!!.requestFocus()
                return false
            }

            if (textInputLayoutRegMobileNo.editText!!.text.toString().trim().length != 10) {
                textInputLayoutRegMobileNo.error = "Invalid mobile number"
                textInputLayoutRegMobileNo.editText!!.requestFocus()
                return false
            }

            if (textInputLayoutRegEmail.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutRegEmail.error = "Please enter email address"
                textInputLayoutRegEmail.editText!!.requestFocus()
                return false
            }
            if (!Utils.isValidEmail(textInputLayoutRegEmail.editText!!.text.toString().trim())) {
                textInputLayoutRegEmail.error = "Invalid email address"
                textInputLayoutRegEmail.editText!!.requestFocus()
                return false
            }

            /*if (textInputLayoutRegPinCode.editText!!.text.toString().trim().isEmpty()){
                textInputLayoutRegPinCode.error ="Please enter pincode"
                textInputLayoutRegPinCode.editText!!.requestFocus()
                return false
            }

            if (textInputLayoutRegPinCode.editText!!.text.toString().trim().length !=6){
                textInputLayoutRegPinCode.error ="Invalid Pincode"
                textInputLayoutRegPinCode.editText!!.requestFocus()
                return false
            }

            *//*if (textInputLayoutRegShopName.editText!!.text.toString().trim().isEmpty()){
                textInputLayoutRegShopName.error ="Please enter shop name"
                textInputLayoutRegShopName.editText!!.requestFocus()
                return false
            }*//*

            if (textInputLayoutRegPanCardNo.editText!!.text.toString().trim().isEmpty()){
                textInputLayoutRegPanCardNo.error ="Please enter PAN card number"
                textInputLayoutRegPanCardNo.editText!!.requestFocus()
                return false
            }

            val pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
            matcher = pattern.matcher(textInputLayoutRegPanCardNo.editText!!.text.toString().trim())
            if (textInputLayoutRegPanCardNo.editText!!.text.toString().trim().length != 10 || !matcher.matches()){
                textInputLayoutRegPanCardNo.error = "Enter valid PAN card number"
                textInputLayoutRegPanCardNo.editText!!.requestFocus()
                return false
            }


            if (textInputLayoutRegAadhaarNo.editText!!.text.toString().trim().isEmpty()){
                textInputLayoutRegAadhaarNo.error = "Please enter Aadhaar Number"
                textInputLayoutRegAadhaarNo.editText!!.requestFocus()
                return false
            }

            if (textInputLayoutRegAadhaarNo.editText!!.text.toString().trim().length != 12){
                textInputLayoutRegAadhaarNo.error = "Invalid Aadhaar Number"
                textInputLayoutRegAadhaarNo.editText!!.requestFocus()
                return false
            }


            if (textInputLayoutRegState.editText!!.text.toString().trim().isEmpty()){
                textInputLayoutRegState.error = "Please enter State"
                textInputLayoutRegState.editText!!.requestFocus()
                return false
            }

            if (textInputLayoutRegCity.editText!!.text.toString().trim().isEmpty()){
                textInputLayoutRegCity.error = "Please enter city"
                textInputLayoutRegCity.editText!!.requestFocus()
                return false
            }

            if (textInputLayoutRegAddress.editText!!.text.toString().trim().isEmpty()){
                textInputLayoutRegAddress.error = "Please enter Address"
                textInputLayoutRegAddress.editText!!.requestFocus()
                return false
            }*/

            /*if (spinnerCategory.selectedItemPosition == 0){
                showToast("Please select agent category")
                return false
            }*/
            textInputLayoutRegFullName.isErrorEnabled =false
            textInputLayoutRegMobileNo.isErrorEnabled =false
            textInputLayoutRegEmail.isErrorEnabled =false
            return true
        }

    override fun onOtpSent(it: RegOtpResponse) {
        if (it.status == Constants.ApiResponse.RES_SUCCESS) {
            PROCESS_TYPE = 1
            groupRegFields.visibility = View.GONE
            textInputLayoutOtp.visibility = View.VISIBLE
            btnRegistration.text = getString(R.string.submit)
            ivCancel.visibility = View.VISIBLE
            linearLoginOtp.visibility = View.VISIBLE
            tvLabelreghere.text ="Enter OTP"
            tvOtpTimeRemainLbl.text = getString(R.string.time_remain_to_verifiy)
            Toast.makeText(getViewActivity(), it.message, Toast.LENGTH_SHORT).show()
            updateUI(true)
        } else {
            Utils.showAlert(
                this@RegistrationActivity,
                it.message,
                "Easy Paisa",
                View.OnClickListener {
                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }

}
