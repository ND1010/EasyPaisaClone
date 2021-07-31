package com.app.bhaskar.easypaisa.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.AddNewBeneActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.AddNewBeneActivityPresenterImpl
import com.app.bhaskar.easypaisa.mvp.presenter.AddNewBeneficiaryPresenter
import com.app.bhaskar.easypaisa.response_model.SearchRemitterResponse
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.google.gson.reflect.TypeToken
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_add_new_bene.*
import kotlinx.android.synthetic.main.toolbar_simple.*
import javax.inject.Inject

class AddNewBeneActivity : BaseActivity(), AddNewBeneficiaryPresenter.AddNewBeneficiaryView {

    companion object {
        const val TAG = "AddNewBeneActivity"
    }

    private lateinit var model: AddNewBeneActivityModel

    @Inject
    lateinit var presenter: AddNewBeneActivityPresenterImpl
    var remitterResponse: SearchRemitterResponse? = null
    private var selectedBene: SearchRemitterResponse.Userdata.Benelist? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_bene)
        ivHomeBack.setOnClickListener { onBackPressed() }
        tvToolbarTitle.text = "Add New Beneficiary"
        model = AddNewBeneActivityModel(getViewActivity())
        presenter = AddNewBeneActivityPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        if (intent.hasExtra(Constants.UI.REMITTER_RESPONSE)) {
            val type = object : TypeToken<SearchRemitterResponse>() {}.type
            remitterResponse = EasyPaisaApp.getGson()
                .fromJson(intent.getStringExtra(Constants.UI.REMITTER_RESPONSE), type)
        } else {
            showToast(getString(R.string.some_thing_wants_wong))
            finish()
        }
        initView()
    }

    private fun initView() {
        btnProceed.setOnClickListener {
            if (isValide) {
                doRetriveModel().getAddNewBeneRequest().token =
                    EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
                doRetriveModel().getAddNewBeneRequest().userId =
                    EasyPaisaApp.getLoggedInUser()?.id.toString()
                doRetriveModel().getAddNewBeneRequest().type = "addbeneficiary"
                doRetriveModel().getAddNewBeneRequest().mobile = remitterResponse?.userdata!!.mobile
                doRetriveModel().getAddNewBeneRequest().benename =
                    textInputLayoutBeneName.editText!!.text.toString().trim()
                doRetriveModel().getAddNewBeneRequest().beneaccount =
                    textInputLayoutBankAccount.editText!!.text.toString().trim()
                doRetriveModel().getAddNewBeneRequest().beneifsc =
                    textInputLayoutBankIfsc.editText!!.text.toString().trim()
                doRetriveModel().getAddNewBeneRequest().bankname =
                    textInputLayoutBankName.editText!!.text.toString().trim()
                presenter.doAddNewBeneficiary()
            }
        }
    }

    override fun doRetriveModel(): AddNewBeneActivityModel {
        return model
    }

    override fun getViewActivity(): Activity {
        return this@AddNewBeneActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    val isValide: Boolean
        get() {
            if (textInputLayoutBankAccount.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutBankAccount.editText!!.error = "Enter Account Number"
                return false
            }else{
                textInputLayoutBankAccount.isErrorEnabled =false
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

            if (textInputLayoutBankName.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutBankName.error = "Enter Bank Name"
                return false
            } else {
                textInputLayoutBankName.isErrorEnabled = false

            }

            if (textInputLayoutBeneName.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutBeneName.error = "Enter Beneficiary Name"
                return false
            } else {
                textInputLayoutBeneName.isErrorEnabled = false
            }

            return true
        }


    override fun onBenAdded() {
        val response  = doRetriveModel().getDomain().addNewBenRe
        if (response.status == Constants.ApiResponse.RES_SUCCESS){
            selectedBene = SearchRemitterResponse.Userdata.Benelist()
            selectedBene?.beneaccount = doRetriveModel().getAddNewBeneRequest().beneaccount
            selectedBene?.benebank = doRetriveModel().getAddNewBeneRequest().bankname
            selectedBene?.beneifsc = doRetriveModel().getAddNewBeneRequest().beneifsc
            selectedBene?.beneid =123
            selectedBene?.benemobile = remitterResponse?.userdata!!.mobile
            selectedBene?.benename = doRetriveModel().getAddNewBeneRequest().benename

            Utils.showSuccessAlert(getViewActivity(),"Beneficiary Added successfully.",View.OnClickListener {
                val returnIntent = Intent()
                val type = object : TypeToken<SearchRemitterResponse.Userdata.Benelist>() {}.type
                val json  =  EasyPaisaApp.getGson().toJson(selectedBene,type)
                returnIntent.putExtra(Constants.UI.NEW_BENE, json)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            })
        }else{
            Utils.showAlert(getViewActivity(),response.message,"Easy Paisa", View.OnClickListener {
            })
        }
    }
}