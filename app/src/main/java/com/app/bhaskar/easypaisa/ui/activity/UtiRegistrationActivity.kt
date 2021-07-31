package com.app.bhaskar.easypaisa.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.UtiRegistrationModel
import com.app.bhaskar.easypaisa.mvp.presenter.UtiRegistrationPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.UtiRegistrationPresenterImpl
import com.app.bhaskar.easypaisa.response_model.LoginResponse
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_uti_registration.*
import kotlinx.android.synthetic.main.toolbar_simple.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject


class UtiRegistrationActivity : BaseActivity(), UtiRegistrationPresenter.UtiRegistrationView {

    companion object {
        const val TAG = "UtiRegistrationActivity"
    }

    private lateinit var model: UtiRegistrationModel

    @Inject
    lateinit var presenter: UtiRegistrationPresenterImpl
    lateinit var matcher: Matcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uti_registration)
        model = UtiRegistrationModel(getViewActivity())
        presenter = UtiRegistrationPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)

        initViews()
    }

    private fun initViews() {
        tvToolbarTitle.text = "Uti Pan Registration"
        ivHomeBack.setOnClickListener {
            onBackPressed()
        }

        val user: LoginResponse.Userdata = EasyPaisaApp.getLoggedInUser()!!
        textInputLayoutUtiShopName.editText!!.setText(user.shopname)
        textInputLayoutUtiMobNo.editText!!.setText(user.mobile)
        textInputLayoutUtiAddress.editText!!.setText(user.address)
        textInputLayoutUtiAadharNo.editText!!.setText(user.aadharcard)
        textInputLayoutUtiPan.editText!!.setText(user.pancard)
        textInputLayoutUtiEmail.editText!!.setText(user.email)
        textInputLayoutUtiState.editText!!.setText(user.state)
        textInputLayoutUtiPinCode.editText!!.setText(user.pincode)
        textInputLayoutUtiContactPerson.editText!!.setText(user.name)

        btnProceedUtiRegistration.setOnClickListener {
            if (isValide) {
                textInputLayoutUtiPan.editText!!.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                doRetriveModel().getUtiRegisterRequest().name =
                    textInputLayoutUtiContactPerson.editText!!.text.toString().trim()
                doRetriveModel().getUtiRegisterRequest().adhaar =
                    textInputLayoutUtiAadharNo.editText!!.text.toString().trim()
                doRetriveModel().getUtiRegisterRequest().contactPerson =
                    textInputLayoutUtiContactPerson.editText!!.text.toString().trim()
                doRetriveModel().getUtiRegisterRequest().email =
                    textInputLayoutUtiEmail.editText!!.text.toString().trim()
                doRetriveModel().getUtiRegisterRequest().location =
                    textInputLayoutUtiAddress.editText!!.text.toString().trim()
                doRetriveModel().getUtiRegisterRequest().mobile =
                    textInputLayoutUtiMobNo.editText!!.text.toString().trim()
                doRetriveModel().getUtiRegisterRequest().pan =
                    textInputLayoutUtiPan.editText!!.text.toString().trim()
                doRetriveModel().getUtiRegisterRequest().state =
                    textInputLayoutUtiState.editText!!.text.toString().trim()
                doRetriveModel().getUtiRegisterRequest().pincode =
                    textInputLayoutUtiPinCode.editText!!.text.toString().trim()
                doRetriveModel().getUtiRegisterRequest().token =
                    EasyPaisaApp.getLoggedInUser()!!.apptoken
                doRetriveModel().getUtiRegisterRequest().userId =
                    EasyPaisaApp.getLoggedInUser()!!.id.toString()
                doRetriveModel().getUtiRegisterRequest().transactionType = "psaid"


                presenter.registerAgentUti()
            }
        }

    }

    override fun doRetriveModel(): UtiRegistrationModel {
        return model
    }

    override fun getViewActivity(): Activity {
        return this@UtiRegistrationActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    val isValide: Boolean
        get() {
            if (textInputLayoutUtiShopName.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutUtiShopName.error = "Please enter your shop name"
                textInputLayoutUtiShopName.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutUtiShopName.isErrorEnabled = false
            }

            if (textInputLayoutUtiMobNo.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutUtiMobNo.error = "Please enter mobile number"
                textInputLayoutUtiMobNo.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutUtiMobNo.isErrorEnabled = false
            }

            if (textInputLayoutUtiMobNo.editText!!.text.toString().trim().length != 10) {
                textInputLayoutUtiMobNo.error = "Invalid mobile number"
                textInputLayoutUtiMobNo.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutUtiMobNo.isErrorEnabled = false
            }

            if (textInputLayoutUtiEmail.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutUtiEmail.error = "Please enter email address"
                textInputLayoutUtiEmail.editText!!.requestFocus()
                return false
            }
            if (!Utils.isValidEmail(textInputLayoutUtiEmail.editText!!.text.toString().trim())) {
                textInputLayoutUtiEmail.error = "Invalid email address"
                textInputLayoutUtiEmail.editText!!.requestFocus()
                return false
            }


            if (textInputLayoutUtiAadharNo.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutUtiAadharNo.error = "Enter Aadhaar Number"
                textInputLayoutUtiAadharNo.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutUtiAadharNo.isErrorEnabled = false
            }

            if (textInputLayoutUtiAadharNo.editText!!.text.toString().trim().length != 12) {
                textInputLayoutUtiAadharNo.error = "Invalid Aadhaar Number"
                textInputLayoutUtiAadharNo.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutUtiAadharNo.isErrorEnabled = false
            }

            if (textInputLayoutUtiPan.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutUtiPan.error = "Please enter PAN card number"
                textInputLayoutUtiPan.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutUtiPan.isErrorEnabled = false
            }

            val pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
            matcher = pattern.matcher(textInputLayoutUtiPan.editText!!.text.toString().trim())
            if (textInputLayoutUtiPan.editText!!.text.toString()
                    .trim().length != 10 || !matcher.matches()
            ) {
                textInputLayoutUtiPan.error = "Enter valid PAN card number"
                textInputLayoutUtiPan.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutUtiPan.isErrorEnabled = false
            }

            if (textInputLayoutUtiAddress.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutUtiAddress.error = "Please enter address"
                textInputLayoutUtiAddress.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutUtiAddress.isErrorEnabled = false
            }

            if (textInputLayoutUtiAddress.editText!!.text.toString().trim().length <= 10) {
                textInputLayoutUtiAddress.error = "Address should be more then 10 characters"
                textInputLayoutUtiAddress.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutUtiAddress.isErrorEnabled = false
            }

            if (textInputLayoutUtiState.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutUtiState.error = "Please enter state"
                textInputLayoutUtiState.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutUtiState.isErrorEnabled = false
            }

            if (textInputLayoutUtiPinCode.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutUtiPinCode.error = "Please enter Pincode"
                textInputLayoutUtiPinCode.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutUtiPinCode.isErrorEnabled = false
            }

            if (textInputLayoutUtiPinCode.editText!!.text.toString().trim().length != 6) {
                textInputLayoutUtiPinCode.error = "Invalid Pincode"
                textInputLayoutUtiPinCode.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutUtiPinCode.isErrorEnabled = false
            }
            return true
        }

    override fun onUtiRegistered() {
        val response = doRetriveModel().getDomain().utiRegistrationResponse
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            val returnIntent = Intent()
            returnIntent.putExtra("result", Constants.ApiResponse.RES_SUCCESS)
            returnIntent.putExtra("message", "${response.message}, transaction id ${response.txnid}")
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        } else {
            showError(response.message)
        }
    }

    override fun onBackPressed() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_CANCELED, returnIntent)
        finish()
    }
}