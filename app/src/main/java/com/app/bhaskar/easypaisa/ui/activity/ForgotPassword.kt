package com.app.bhaskar.easypaisa.ui.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.ForgotPasswordActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.ForgotPasswordPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.ForgotPasswordPresenterImpl
import com.app.bhaskar.easypaisa.utils.Constants
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*
import javax.inject.Inject

class ForgotPassword : BaseActivity(), ForgotPasswordPresenter.ForgotPasswordView {

    companion object {
        const val TAG = "ForgotPassword"
    }

    @Inject
    lateinit var presenter: ForgotPasswordPresenterImpl
    lateinit var model: ForgotPasswordActivityModel
    private var mobile = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        model = ForgotPasswordActivityModel(getViewActivity())
        presenter = ForgotPasswordPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)

        if (intent.hasExtra(Constants.UI.MOBILE_NUMBER)) {
            mobile = intent.getStringExtra(Constants.UI.MOBILE_NUMBER).toString()
            tvFPassMobile.text = mobile
        }

        btnUpdate.setOnClickListener {
            if (isValide) {
                val otp = textInputLayoutFPOTP.editText!!.text.toString().trim()
                val newpass = textInputLayoutFPPassword.editText!!.text.toString().trim()
                doRetriveModel().getUpdatePasswordRequest().mobile=mobile
                doRetriveModel().getUpdatePasswordRequest().token=otp
                doRetriveModel().getUpdatePasswordRequest().password=newpass
                presenter.doUpdatePassword()
            }
        }
    }

    private val isValide: Boolean
        get() {
            val otp = textInputLayoutFPOTP.editText!!.text.toString().trim()
            val newpass = textInputLayoutFPPassword.editText!!.text.toString().trim()
            val conpass = textInputLayoutFPPasswordConfm.editText!!.text.toString().trim()
            if (otp.isEmpty() || otp.length != 8 || newpass.isEmpty()
                || conpass.isEmpty() || newpass != conpass
            ) {

                if (otp.isNotEmpty() && otp.length != 8) {
                    textInputLayoutFPOTP.error = "Invalid OTP"
                } else if (otp.isEmpty()) {
                    textInputLayoutFPOTP.error = "Enter OTP"
                } else {
                    textInputLayoutFPOTP.isErrorEnabled = false
                }

                if (newpass.isEmpty()) {
                    textInputLayoutFPPassword.error = "Enter new password"
                } else {
                    textInputLayoutFPPassword.isErrorEnabled = false
                }

                if (conpass.isEmpty()) {
                    textInputLayoutFPPasswordConfm.error = "Enter confirm password"
                } else {
                    textInputLayoutFPPasswordConfm.isErrorEnabled = false
                }

                if (newpass != conpass) {
                    textInputLayoutFPPasswordConfm.error =
                        "New password and confirm password should be same!"
                } else {
                    textInputLayoutFPPasswordConfm.isErrorEnabled = false
                }

                return false
            } else {
                textInputLayoutFPOTP.isErrorEnabled = false
                textInputLayoutFPPassword.isErrorEnabled = false
                textInputLayoutFPPasswordConfm.isErrorEnabled = false
                return true
            }
        }

    override fun getViewActivity(): Activity {
        return this@ForgotPassword
    }

    override fun onNetworkStateChange(isConnect: Boolean) {

    }

    override fun doRetriveModel(): ForgotPasswordActivityModel {
        return model
    }

    override fun onPasswordRest() {
        val response = doRetriveModel().getUpdatePasswordActivityDomain().updatePasswordResponse
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            showToast("Password has been updated successfully")
            onBackPressed()
        } else {
            showError(response.message)
        }
    }
}
