package com.app.bhaskar.easypaisa.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.RegisterRemitterActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.RegisterRemitterPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.RegisterRemitterPresenterImpl
import com.app.bhaskar.easypaisa.ui.dialog.DialogOtpVerification
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_register_remitter.*
import kotlinx.android.synthetic.main.toolbar_simple.*
import javax.inject.Inject

class RegisterRemitterActivity : BaseActivity(), RegisterRemitterPresenter.RegisterRemitterView {

    companion object {
        const val TAG = "RegisterRemitterActivity"
    }

    private var verifiyOtpDialog: DialogOtpVerification? = null
    private var remitterMobileNo: String = ""
    private lateinit var model: RegisterRemitterActivityModel

    @Inject
    lateinit var presenter: RegisterRemitterPresenterImpl
    private val isValide: Boolean
        get() {
            if (textInputLayoutName.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutName.error = "Enter User Name"
                return false
            } else {
                textInputLayoutName.isErrorEnabled = false
            }

            if (textInputLayoutBeneName.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutBeneName.error = "Enter Beneficiary Name"
                return false
            } else {
                textInputLayoutBeneName.isErrorEnabled = false
            }

            if (textInputLayoutBankAccount.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutBankAccount.editText!!.error = "Enter Account Number"
                return false
            } else {
                textInputLayoutBankAccount.isErrorEnabled = false
            }

            if (textInputLayoutBankAccount.editText!!.text.toString().trim().isNotEmpty()
                && textInputLayoutBankAccount.editText!!.text.toString().trim().length < 5
            ) {
                textInputLayoutBankAccount.editText!!.error = "Invalid Account Number"
                return false
            } else {
                textInputLayoutBankAccount.isErrorEnabled = false
            }

            if (textInputLayoutBankAccountCon.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutBankAccountCon.editText!!.error = "Enter Confirm Account Number"
                return false
            } else {
                textInputLayoutBankAccountCon.isErrorEnabled = false
            }

            if (textInputLayoutBankAccountCon.editText!!.text.toString().trim().isNotEmpty()
                && textInputLayoutBankAccountCon.editText!!.text.toString().trim().length < 5
            ) {
                textInputLayoutBankAccountCon.editText!!.error = "Invalid Confirm Account Number"
                return false
            } else {
                textInputLayoutBankAccountCon.isErrorEnabled = false
            }

            if (textInputLayoutBankAccount.editText!!.text.toString()
                    .trim() != textInputLayoutBankAccountCon.editText!!.text.toString().trim()
            ) {
                textInputLayoutBankAccountCon.editText!!.error =
                    "Confirm Account No and Account No is not match!"
                return false
            } else {
                textInputLayoutBankAccountCon.isErrorEnabled = false
            }

            if (textInputLayoutBankIfsc.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutBankIfsc.error = "Please enter ifsc code"
                return false
            } else {
                textInputLayoutBankIfsc.isErrorEnabled = false
            }

            if (textInputLayoutBankIfsc.editText!!.text.toString().trim().length != 11) {
                textInputLayoutBankIfsc.editText!!.requestFocus()
                textInputLayoutBankIfsc.error = getString(R.string.please_enter_valid_IFSC_code)
                return false
            } else {
                textInputLayoutBankIfsc.isErrorEnabled = false
            }

            return true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_remitter)
        model = RegisterRemitterActivityModel(getViewActivity())
        presenter = RegisterRemitterPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)

        ivHomeBack.setOnClickListener { onBackPressed() }
        tvToolbarTitle.text = "Register Remitter"

        initView()
    }

    private fun initView() {
        if (intent.hasExtra(Constants.UI.MOBILE_NO)) {
            remitterMobileNo = intent.getStringExtra(Constants.UI.MOBILE_NO) ?: ""
            if (remitterMobileNo.isEmpty()) {
                showToast(getString(R.string.some_thing_wants_wong))
                finish()
            }
        }
        btnProceed.setOnClickListener {
            if (isValide) {
                doRetriveModel().getRegisterRemitterRequest().token =
                    EasyPaisaApp.getLoggedInUser()!!.apptoken
                doRetriveModel().getRegisterRemitterRequest().userId =
                    EasyPaisaApp.getLoggedInUser()!!.id.toString()
                doRetriveModel().getRegisterRemitterRequest().type = "registration"
                doRetriveModel().getRegisterRemitterRequest().mobile = remitterMobileNo
                doRetriveModel().getRegisterRemitterRequest().name =
                    textInputLayoutName.editText!!.text.toString().trim()
                doRetriveModel().getRegisterRemitterRequest().benename =
                    textInputLayoutBeneName.editText!!.text.toString().trim()
                doRetriveModel().getRegisterRemitterRequest().beneaccount =
                    textInputLayoutBankAccount.editText!!.text.toString().trim()
                doRetriveModel().getRegisterRemitterRequest().beneifsc =
                    textInputLayoutBankIfsc.editText!!.text.toString().trim()
                presenter.goRegisterRemiter()
            }
        }
    }

    override fun doRetriveModel(): RegisterRemitterActivityModel {
        return model
    }

    override fun onRegisterRemiiter() {
        val response = doRetriveModel().getLoginDomain().remitterRegisterResponse
        if (response.status == Constants.ApiResponse.RES_TXNOTP) {
            //call opt dialog
            //generateOtp(doRetriveModel().getRegisterRemitterRequest().mobile)
            val responsecode = response.transdata.transid

            verifiyOtpDialog = DialogOtpVerification(getViewActivity())
            verifiyOtpDialog?.showOTPDialog({
                doRetriveModel().getVerifyRemitterReq().token =
                    EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
                doRetriveModel().getVerifyRemitterReq().userId =
                    EasyPaisaApp.getLoggedInUser()?.id.toString()
                doRetriveModel().getVerifyRemitterReq().mobile =
                    doRetriveModel().getRegisterRemitterRequest().mobile
                doRetriveModel().getVerifyRemitterReq().otp = it
                doRetriveModel().getVerifyRemitterReq().type = "customerverification"
                doRetriveModel().getVerifyRemitterReq().responsecode = responsecode.toString()
                presenter.doVerifyRemitter()
            }, {
                if (verifiyOtpDialog!=null){
                    verifiyOtpDialog?.dismiss()
                    verifiyOtpDialog=null
                }
                generateOtp(doRetriveModel().getRegisterRemitterRequest().mobile)
            },{
                if (verifiyOtpDialog!=null){
                    verifiyOtpDialog?.dismiss()
                    verifiyOtpDialog=null
                }
                finish()
            })
        } else {
            Utils.showAlert(getViewActivity(), response.message, "", View.OnClickListener {

            })
        }
    }

    private fun generateOtp(mobile: String) {
        doRetriveModel().getGenerateOtpReq().token = EasyPaisaApp.getLoggedInUser()?.apptoken!!
        doRetriveModel().getGenerateOtpReq().userId = EasyPaisaApp.getLoggedInUser()?.id.toString()
        doRetriveModel().getGenerateOtpReq().type = "generateOtp"
        doRetriveModel().getGenerateOtpReq().mobile = mobile
        presenter.sendOtp()
    }

    override fun getViewActivity(): Activity {
        return this@RegisterRemitterActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    override fun onGenerateOtp() {
        val response = doRetriveModel().getLoginDomain().generateOtpResponse
        if (response.status == Constants.ApiResponse.RES_TXNOTP) {
            val responsecode = response.transdata.transid

            verifiyOtpDialog = DialogOtpVerification(getViewActivity())
            verifiyOtpDialog?.showOTPDialog({
                doRetriveModel().getVerifyRemitterReq().token =
                    EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
                doRetriveModel().getVerifyRemitterReq().userId =
                    EasyPaisaApp.getLoggedInUser()?.id.toString()
                doRetriveModel().getVerifyRemitterReq().mobile =
                    doRetriveModel().getRegisterRemitterRequest().mobile
                doRetriveModel().getVerifyRemitterReq().otp = it
                doRetriveModel().getVerifyRemitterReq().type = "customerverification"
                doRetriveModel().getVerifyRemitterReq().responsecode = responsecode.toString()
                presenter.doVerifyRemitter()
            }, {
                if (verifiyOtpDialog!=null){
                    verifiyOtpDialog?.dismiss()
                    verifiyOtpDialog=null
                }
                generateOtp(doRetriveModel().getRegisterRemitterRequest().mobile)
            },{
                if (verifiyOtpDialog!=null){
                    verifiyOtpDialog?.dismiss()
                    verifiyOtpDialog=null
                }
                finish()
            })

        } else {
            Utils.showAlert(getViewActivity(), response.message, "", View.OnClickListener { })
        }
    }

    override fun onVerifyRemitter() {
        val response = doRetriveModel().getLoginDomain().verifyRemitterResponse
        if (response.message == Constants.ApiResponse.RES_SUCCESS) {
            Utils.showSuccessAlert(getViewActivity(), response.message, View.OnClickListener {

                finish()
            })
        } else {
            Utils.showAlert(getViewActivity(), response.message, "", View.OnClickListener { })
        }
    }
}