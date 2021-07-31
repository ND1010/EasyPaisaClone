package com.app.bhaskar.easypaisa.mvp.presenter

import android.content.Intent
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.ui.activity.HomeActivity
import com.app.bhaskar.easypaisa.ui.activity.RegistrationActivity
import com.app.bhaskar.easypaisa.utils.Utils
import javax.inject.Inject

class LoginPresenterImpl(val view: LoginPresenter.LoginView) : LoginPresenter {

    private val mEventBus = EasyPaisaApp.getDefault()
    @Inject
    lateinit var repository: EasyPaisaRepository

    /*override fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest) {

        if (Utils.isNetworkConnected(view.getViewActivity())) {
            if (forgotPasswordRequest.email.trim().isEmpty()) {
                view.showError(view.getViewActivity().getString(R.string.label_enter_email_id))
            } else {
                view.showProgress()
                repository.forgotPassword(forgotPasswordRequest, {
                    view.hideProgress()
                    view.onForgotPassword(it!!)
                }, {
                    view.hideProgress()
                    view.showError(it!!.localizedMessage)
                })
            }
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }

    }

    override fun doSignin(signinRequest: SigninRequest) {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            if (signinRequest.email.trim().isEmpty()) {
                view.showError(view.getViewActivity().getString(R.string.label_enter_email_id))
            } else if (!Utils.isValidEmail(signinRequest.email.trim())) {
                view.showError(view.getViewActivity().getString(R.string.validation_Please_enter_valid_email_id))
            } else if (signinRequest.password.trim().isEmpty()) {
                view.showError(view.getViewActivity().getString(R.string.label_enter_password))
            } else if (signinRequest.password.trim().length < 6) {
                view.showError(view.getViewActivity().getString(R.string.password_should_be_more_then_6_character))
            } else {
                view.showProgress()
                repository.doLogin(signinRequest, {
                    view.hideProgress()
                    view.onLoginSuccessful(it!!)
                }, {
                    view.hideProgress()
                    view.showError(it!!.localizedMessage)
                })
            }
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }*/

    override fun doLogin() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getLoginRequest()
            repository.apiLoginToApp(request, {
                view.hideProgress()
                view.doRetriveModel().getLoginDomain().loginResponse= it!!
                view.onLoginResponse()
            }, {
                view.hideProgress()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        }else{
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

    override fun doCallApiForSendPasswordToken() {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            view.showProgress()
            val request = view.doRetriveModel().getPasswordTokenRequest()
            repository.apiPasswordResetnToken(request, {
                view.hideProgress()
                view.doRetriveModel().getLoginDomain().passwordResetTokenResponse= it
                view.onResrtToken()
            }, {
                view.hideProgress()
                if (it?.message != null) {
                    view.showError(it.message!!)
                }
            })
        }else{
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }

    override fun onError(message: String) {
    }

    override fun registerBus() {
        mEventBus.register(this)
    }

    override fun unRegisterBus() {
        mEventBus.unregister(this)
    }

    override fun gotoForgotPassword() {

    }

    override fun gotoRegistrationScreen() {
        val intent = Intent(view.getViewActivity(), RegistrationActivity::class.java)
        view.getViewActivity().startActivity(intent)
    }

    override fun gotoHomeScreen() {
        val intent = Intent(view.getViewActivity(), HomeActivity::class.java)
        view.getViewActivity().startActivity(intent)
        view.getViewActivity().finishAffinity()
    }
}