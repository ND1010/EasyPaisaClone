package com.app.bhaskar.easypaisa.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.LoginActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.LoginPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.LoginPresenterImpl
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.google.android.material.snackbar.Snackbar
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject


class LoginActivity : BaseActivity(), LoginPresenter.LoginView {

    companion object {
        private val TAG = "LoginActivity"
    }

    @Inject
    lateinit var presenter: LoginPresenterImpl
    lateinit var model: LoginActivityModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBarEasyPaisa)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        model = LoginActivityModel(getViewActivity())
        presenter = LoginPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)

        if (EasyPaisaApp.getLoggedInUser() != null) {
            EasyPaisaApp.removeUserLatLong()
            startActivity(Intent(getViewActivity(), HomeActivity::class.java))
            finish()
        }

        btnLogin.setOnClickListener {

            if (isValide) {
                doRetriveModel().getLoginRequest().mobile =
                    textInputLayoutLoginMobile.editText!!.text.toString()
                doRetriveModel().getLoginRequest().password =
                    textInputLayoutLoginPassword.editText!!.text.toString()
                presenter.doLogin()
            }
        }

        tvForgotPassword.setOnClickListener {
            when {
                TextUtils.isEmpty(inputEdtMobile.text.toString().trim()) -> textInputLayoutLoginMobile.error =
                    "Please enter mobile number"
                inputEdtMobile.text.toString().trim().length != 10 -> textInputLayoutLoginMobile.error =
                    "Please enter valid mobile number"
                else -> {
                    textInputLayoutLoginMobile.isErrorEnabled = false
                    textInputLayoutLoginPassword.isErrorEnabled = false
                    doRetriveModel().getPasswordTokenRequest().mobile =
                        textInputLayoutLoginMobile.editText!!.text.toString().trim()
                    presenter.doCallApiForSendPasswordToken()
                }
            }
            /*val mob  = inputEdtMobile.toString()
            Log.e(TAG,"Mobile_Num: $mob")
            textInputLayoutLoginMobile.isErrorEnabled = false
            textInputLayoutLoginPassword.isErrorEnabled = false
            doRetriveModel().getPasswordTokenRequest().mobile = "9999999933"
            presenter.doCallApiForSendPasswordToken()*/
        }

        tvDontHavAccountRegisterHere.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }
    }

    override fun onResrtToken() {
        val response = doRetriveModel().getLoginDomain().passwordResetTokenResponse
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            startActivity(
                Intent(
                    getViewActivity(),
                    ForgotPassword::class.java
                ).putExtra(Constants.UI.MOBILE_NUMBER, textInputLayoutLoginMobile.editText!!.text.toString().trim())
            )
        } else {
            showError(response.message)
        }
    }

    override fun getViewActivity(): Activity {
        return this@LoginActivity
    }

    override fun doRetriveModel(): LoginActivityModel {
        return model
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
        if (!isConnect) {
            Utils.showSnackBar(
                getViewActivity().findViewById(android.R.id.content),
                "No network connection,Please connect to network",
                Snackbar.LENGTH_LONG,
                true,
                getViewActivity()
            )
        }
    }

    private val isValide: Boolean
        get() {
            val mobileNumber = textInputLayoutLoginMobile.editText!!.text.toString().trim()
            val pass = textInputLayoutLoginPassword.editText!!.text.toString().trim()
            if (mobileNumber.isEmpty() || mobileNumber.length != 10 || pass.isEmpty()
            /*|| pass.length != 6*/) {

                if (mobileNumber.isNotEmpty() && mobileNumber.length != 10) {
                    textInputLayoutLoginMobile.error = "Please enter valid mobile number"
                } else if (mobileNumber.isEmpty()) {
                    textInputLayoutLoginMobile.error = "Please enter mobile number"
                } else {
                    textInputLayoutLoginMobile.isErrorEnabled = false
                }

                if (pass.isEmpty()) {
                    textInputLayoutLoginPassword.error = "Please enter password"
                } else {
                    textInputLayoutLoginPassword.isErrorEnabled = false
                }

                /*if (pass.isNotEmpty() && pass.length !=6){
                    textInputLayoutLoginPassword.editText!!.error = "Password should be at least 6 character"
                }else if (mobileNumber.isEmpty()){
                    textInputLayoutLoginPassword.editText!!.error = "Please enter password"
                }else{
                    textInputLayoutLoginPassword.editText!!.isEnabled = false
                }*/
                return false
            } else {
                textInputLayoutLoginMobile.isErrorEnabled = false
                textInputLayoutLoginPassword.isErrorEnabled = false
                return true
            }
        }

    override fun onLoginResponse() {
        val loginResponse = doRetriveModel().getLoginDomain().loginResponse
        if (loginResponse.status == Constants.ApiResponse.RES_SUCCESS) {
            val userData = loginResponse.userdata
            userData.firstTime = true
            EasyPaisaApp.setLoggedInUser(loginResponse.userdata)
            presenter.gotoHomeScreen()
        } else {
            showError(loginResponse.message)
        }
    }

    /*override fun onData(songs: String) {
        Log.e(TAG,"DATA---> $songs")
        try {
            val stackOverflowXmlParser = StackOverflowXmlParser()
            val listData = stackOverflowXmlParser.parse(songs) as ArrayList<Entry>
            Log.e(TAG,"list--> ${listData.toString()}")

        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }*/
}
