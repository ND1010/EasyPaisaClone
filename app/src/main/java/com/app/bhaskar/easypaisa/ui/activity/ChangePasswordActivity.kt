package com.app.bhaskar.easypaisa.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.ChangePassActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.ChangePassPresenterImpl
import com.app.bhaskar.easypaisa.mvp.presenter.ChangePasswordPresenter
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.toolbar_simple.*
import javax.inject.Inject

class ChangePasswordActivity : BaseActivity(), ChangePasswordPresenter.ChangePassView {

    companion object {
        const val TAG = "ChangePasswordActivity"
    }

    private lateinit var model: ChangePassActivityModel

    @Inject
    lateinit var presenter: ChangePassPresenterImpl


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        model = ChangePassActivityModel(getViewActivity())
        presenter = ChangePassPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        initViews()
    }

    private fun initViews() {
        tvToolbarTitle.text = "Change Password"
        ivHomeBack.setOnClickListener {
            onBackPressed()
        }

        btnChangePass.setOnClickListener {
            if (isValide) {
                doRetriveModel().getChangePassRequest().apptoken =
                    EasyPaisaApp.getLoggedInUser()?.apptoken!!
                doRetriveModel().getChangePassRequest().userId =
                    EasyPaisaApp.getLoggedInUser()?.id.toString()
                doRetriveModel().getChangePassRequest().oldpassword =
                    textInputLayoutChangePassOldPass.editText!!.text.toString().trim()
                doRetriveModel().getChangePassRequest().password =
                    textInputLayoutChangePassNewPass.editText!!.text.toString().trim()

                presenter.changePassword()
            }
        }
    }

    val isValide: Boolean
        get() {
            if (textInputLayoutChangePassOldPass.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutChangePassOldPass.error = "Enter Old Password"
                textInputLayoutChangePassOldPass.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutChangePassOldPass.isErrorEnabled = false
            }

            if (textInputLayoutChangePassOldPass.editText!!.text.toString().trim().length < 8) {
                textInputLayoutChangePassOldPass.error =
                    "Your password length should be atleast 8 character"
                textInputLayoutChangePassOldPass.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutChangePassOldPass.isErrorEnabled = false
            }

            if (textInputLayoutChangePassNewPass.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutChangePassNewPass.error = "Enter New Password"
                textInputLayoutChangePassNewPass.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutChangePassNewPass.isErrorEnabled = false
            }

            if (textInputLayoutChangePassNewPass.editText!!.text.toString().trim().length < 8) {
                textInputLayoutChangePassNewPass.error =
                    "Your password length should be atleast 8 character"
                textInputLayoutChangePassNewPass.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutChangePassNewPass.isErrorEnabled = false
            }

            if (textInputLayoutChangePassNewConfmPass.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutChangePassNewConfmPass.error = "Enter Confirm Password"
                textInputLayoutChangePassNewConfmPass.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutChangePassNewConfmPass.isErrorEnabled = false
            }

            if (textInputLayoutChangePassNewConfmPass.editText!!.text.toString()
                    .trim().length < 8
            ) {
                textInputLayoutChangePassNewConfmPass.error =
                    "Your password length should be atleast 8 character"
                textInputLayoutChangePassNewConfmPass.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutChangePassNewConfmPass.isErrorEnabled = false
            }

            if (textInputLayoutChangePassNewPass.editText!!.text.toString()
                    .trim() != textInputLayoutChangePassNewConfmPass.editText!!.text.toString()
                    .trim()
            ) {
                textInputLayoutChangePassNewConfmPass.error =
                    "New password and confirmed password should be equal"
                textInputLayoutChangePassNewConfmPass.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutChangePassNewConfmPass.isErrorEnabled = false
            }

            return true
        }

    override fun doRetriveModel(): ChangePassActivityModel {
        return model
    }

    override fun getViewActivity(): Activity {
        return this@ChangePasswordActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    override fun onPasswordChanged() {
        val response = doRetriveModel().getDomain().updatePasswordResponse
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            textInputLayoutChangePassOldPass.editText!!.setText("")
            textInputLayoutChangePassNewConfmPass.editText!!.setText("")
            textInputLayoutChangePassNewPass.editText!!.setText("")
            Utils.showAlert(
                getViewActivity(),
                response.message,
                "Change Password",
                View.OnClickListener {
                    onBackPressed()
                })
            EasyPaisaApp.setNeedUpdateUserDetail(true)
        } else {
            Utils.showAlert(
                getViewActivity(),
                response.message,
                "Change Password Error",
                View.OnClickListener {
                })
        }
    }
}