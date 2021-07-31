package com.app.bhaskar.easypaisa.ui.activity

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.AddNewBankActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.AddNewAccountPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.AddNewBankActivityPresenterImpl
import com.app.bhaskar.easypaisa.response_model.ValidateBankDetailResponse
import com.app.bhaskar.easypaisa.ui.listener.OnUserLocationListener
import com.app.bhaskar.easypaisa.ui.service.GoogleFuesedLocationService
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_add_new_bank.*
import kotlinx.android.synthetic.main.toolbar_simple.*
import java.util.*
import javax.inject.Inject


class AddNewBankActivity : BaseActivity(), AddNewAccountPresenter.AddNewAccountView {

    companion object {
        const val TAG = "AddNewBankActivity"
    }

    enum class ApiType {
        getname,
        accounts,
        success
    }

    private var txnid: String=""
    private lateinit var model: AddNewBankActivityModel

    @Inject
    lateinit var presenter: AddNewBankActivityPresenterImpl
    private var apitype = ApiType.getname
    private var accountDetail: ValidateBankDetailResponse? = null
    private lateinit var googleFusedLocation: GoogleFuesedLocationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_bank)
        model = AddNewBankActivityModel(getViewActivity())
        presenter = AddNewBankActivityPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        tvToolbarTitle.text = "Add New Bank"
        ivHomeBack.setOnClickListener { onBackPressed() }
        updateUi(false)
        googleFusedLocation = GoogleFuesedLocationService(getViewActivity(), object :
            OnUserLocationListener {
            override fun onUserLocationSuccess(location: Location) {
                if (EasyPaisaApp.getUserLatLng() == null) {
                    showToast("User location not found, please enable GPS")
                    return
                }
            }

            override fun onUserLocationFail(failMessage: String) {
            }

        }, true)


        btnGetBank.setOnClickListener {
            Log.e(TAG, "ApiType ${ApiType.getname.name}")
            when (apitype) {
                ApiType.getname -> {
                    if (isValide) {
                        doRetriveModel().getValidateBankDetailReq().userId =
                            EasyPaisaApp.getLoggedInUser()?.id.toString()
                        doRetriveModel().getValidateBankDetailReq().token =
                            EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
                        doRetriveModel().getValidateBankDetailReq().type = ApiType.getname.name
                        doRetriveModel().getValidateBankDetailReq().account =
                            inputEdtAccountNo.text.toString().trim()
                        doRetriveModel().getValidateBankDetailReq().ifsc =
                            inputEdtIfscCode.text.toString().trim()
                        presenter.validateAccountDetail()
                    }
                }
                ApiType.accounts -> {
                    updateUi(false)
                }
            }
        }

        btnProceedAddNewAccount.setOnClickListener {
            if (EasyPaisaApp.getUserLatLng() == null) {
                googleFusedLocation.needToShowDialog = true
                googleFusedLocation.accessUserCurrentLocation()
                return@setOnClickListener
            }
            doAddNewAccount()
        }
    }

    private fun doAddNewAccount() {
        doRetriveModel().getAddNewBankReq().account = inputEdtAccountNo.text.toString().trim()
        doRetriveModel().getAddNewBankReq().ifsc = inputEdtIfscCode.text.toString().trim()
        doRetriveModel().getAddNewBankReq().type = ApiType.accounts.name
        doRetriveModel().getAddNewBankReq().accounttype =
            if (rbtnCurrent.isChecked) rbtnCurrent.text.toString()
                .toLowerCase(Locale.ENGLISH) else rbtnSaving.text.toString()
                .toLowerCase(Locale.ENGLISH)
        doRetriveModel().getAddNewBankReq().name = EasyPaisaApp.getLoggedInUser()?.name.toString()
        doRetriveModel().getAddNewBankReq().bank = inputEdtBankName.text.toString().trim()
        doRetriveModel().getAddNewBankReq().bankname = accountDetail?.benename.toString()
        doRetriveModel().getAddNewBankReq().token =
            EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
        doRetriveModel().getAddNewBankReq().userId = EasyPaisaApp.getLoggedInUser()?.id.toString()
        doRetriveModel().getAddNewBankReq().txnid = txnid
        presenter.addNewAccount()
    }

    private fun updateUi(b: Boolean) {
        if (!b) {
            apitype = ApiType.getname
            btnProceedAddNewAccount.alpha = 0.5f
            btnProceedAddNewAccount.isEnabled = false
            inputEdtBankingName.setText("")
            btnGetBank.text = "Get Bank"
            accountDetail = null
            inputEdtAccountNo.isEnabled = true
            inputEdtIfscCode.isEnabled = true
            inputEdtBankName.isEnabled = true
            rbtnCurrent.isEnabled = true
            rbtnSaving.isEnabled = true
        } else {
            apitype = ApiType.accounts
            btnProceedAddNewAccount.alpha = 1f
            btnProceedAddNewAccount.isEnabled = true
            accountDetail?.let {
                inputEdtBankingName.setText(it.benename)
            }
            btnGetBank.text = "Clear Bank"
            inputEdtAccountNo.isEnabled = false
            inputEdtIfscCode.isEnabled = false
            inputEdtBankName.isEnabled = false
            rbtnCurrent.isEnabled = false
            rbtnSaving.isEnabled = false
        }
    }

    override fun doRetriveModel(): AddNewBankActivityModel {
        return model
    }

    override fun onAccountValidate() {
        val response = doRetriveModel().getDomain().validateBankDetailResponse
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            txnid = response.txnid.toString()
            accountDetail = response
            updateUi(response.status == Constants.ApiResponse.RES_SUCCESS)
        } else {
            Utils.showAlert(getViewActivity(), response.message, View.OnClickListener { })
            updateUi(false)
        }
    }

    override fun onAccountAdded() {
        val response = doRetriveModel().getDomain().addNewBankRespones
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            apitype = ApiType.success
            Utils.showSuccessAlert(getViewActivity(), response.message, View.OnClickListener {
                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()
            })
        } else {
            Utils.showAlert(
                getViewActivity(),
                response.message,
                View.OnClickListener { })
        }
    }

    override fun getViewActivity(): Activity {
        return this@AddNewBankActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    val isValide: Boolean
        get() {
            if (textInputLayoutAccountNo.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutAccountNo.error = "Please enter account number"
                return false
            } else {
                textInputLayoutAccountNo.isErrorEnabled = false
            }

            if (textInputLayoutAccountNo.editText!!.text.toString().trim().isNotEmpty()
                && textInputLayoutAccountNo.editText!!.text.toString().trim().toDouble() <= 0
            ) {
                textInputLayoutAccountNo.error = "Invalid account number"
                return false
            } else {
                textInputLayoutAccountNo.isErrorEnabled = false
            }

            if (textInputLayoutIfscCode.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutIfscCode.error = "Please enter ifsc code"
                return false
            } else {
                textInputLayoutIfscCode.isErrorEnabled = false
            }

            if (textInputLayoutIfscCode.editText!!.text.toString().trim().length != 11) {
                textInputLayoutIfscCode.editText!!.requestFocus()
                textInputLayoutIfscCode.error = getString(R.string.please_enter_valid_IFSC_code)
                return false
            }

            if (textInputLayoutBankName.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutBankName.error = "Please enter bank name"
                return false
            } else {
                textInputLayoutBankName.isErrorEnabled = false
            }
            return true
        }

    override fun onBackPressed() {
        if (apitype== ApiType.success){
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }else{
            super.onBackPressed()
        }
    }
}