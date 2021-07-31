package com.app.bhaskar.easypaisa.ui.activity

import aepsapp.easypay.com.aepsandroid.fragments.BottomsheetElecricityBoard
import aepsapp.easypay.com.aepsandroid.fragments.BottomsheetRechargeProider
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.MobileRechargeModel
import com.app.bhaskar.easypaisa.mvp.presenter.MobileRechargePresenter
import com.app.bhaskar.easypaisa.mvp.presenter.MobileRechargePresenterImpl
import com.app.bhaskar.easypaisa.response_model.RechargeProviderResponse
import com.app.bhaskar.easypaisa.response_model.MobileRechargePlanResponse
import com.app.bhaskar.easypaisa.response_model.MobileRechargeResponse
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.google.gson.reflect.TypeToken
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_mobile_recharge.*
import kotlinx.android.synthetic.main.toolbar_simple.*
import javax.inject.Inject

class MobileRechargeActivity : BaseActivity(), MobileRechargePresenter.MobileRechargeView {

    companion object {
        const val TAG = "MobileRechargeActivity"
        const val SELECT_PLAN = 123
    }

    private var boardHasClicked = false
    private var selectedoperator: RechargeProviderResponse.Message.Provider? = null
    private var selectedMobilePlan: MobileRechargePlanResponse.Plan? = null

    @Inject
    lateinit var presenter: MobileRechargePresenterImpl
    private lateinit var model: MobileRechargeModel
    private lateinit var providerAdapter: ArrayAdapter<RechargeProviderResponse.Message.Provider>
    private var arrayListProvider = ArrayList<RechargeProviderResponse.Message.Provider>()
    private lateinit var bottomsheetRechargeProvider: BottomsheetRechargeProider


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_recharge)
        model = MobileRechargeModel(this@MobileRechargeActivity)
        presenter = MobileRechargePresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        initView()
    }

    private fun initView() {

        ivHomeBack.setOnClickListener {
            onBackPressed()
        }
        tvToolbarTitle.text = getString(R.string.mobile_recharge)

        /*val provider = RechargeProviderResponse.Message.Provider(-1, "Select Operator", "")
        arrayListProvider.add(provider)*/
        providerAdapter =
            ArrayAdapter(getViewActivity(), R.layout.spinner_item_black, arrayListProvider)
        providerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        /*spinnerSelectOperator.adapter = providerAdapter
        spinnerSelectOperator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                selectedoperator = arrayListProvider[pos]
                disableEnableSelectPlan()
            }
        }*/

        edtSelectElectricityBoard.setOnClickListener {
            if (arrayListProvider.isNotEmpty()) {
                openBoardDialog()
            } else {
                boardHasClicked = true
                presenter.getProviderList()
            }
        }

        tvBrowsPlan.setOnClickListener {

            if (textInputLayoutRechargeMobNo.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutRechargeMobNo.error = "Enter mobile number"
                textInputLayoutRechargeMobNo.editText!!.requestFocus()
                return@setOnClickListener
            } else {
                textInputLayoutRechargeMobNo.isErrorEnabled = false
            }

            if (textInputLayoutRechargeMobNo.editText!!.text.toString().trim().length != 10) {
                textInputLayoutRechargeMobNo.error = "Invalid mobile number"
                textInputLayoutRechargeMobNo.editText!!.requestFocus()
                return@setOnClickListener
            } else {
                textInputLayoutRechargeMobNo.isErrorEnabled = false
            }

            val type = object : TypeToken<RechargeProviderResponse.Message.Provider>() {}.type
            val intent = Intent(getViewActivity(), MobileRechargePlansActivity::class.java)
            intent.putExtra("operator", EasyPaisaApp.getGson().toJson(selectedoperator, type))
            intent.putExtra("mobile", textInputLayoutRechargeMobNo.editText!!.text.toString())
            startActivityForResult(intent, SELECT_PLAN)
        }

        textInputLayoutRechargeMobNo.editText!!.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    disableEnableSelectPlan()
                    if (textInputLayoutRechargeMobNo.editText!!.text.toString()
                            .trim().length == 10
                    ) {
                        doRetriveModel().getMonileOperatorFromMobile().number =
                            textInputLayoutRechargeMobNo.editText!!.text.toString().trim()
                        doRetriveModel().getMonileOperatorFromMobile().token =
                            EasyPaisaApp.getLoggedInUser()?.apptoken!!
                        doRetriveModel().getMonileOperatorFromMobile().userId =
                            EasyPaisaApp.getLoggedInUser()?.id.toString()
                        doRetriveModel().getMonileOperatorFromMobile().transactionType = "finder"
                        presenter.getOperatorFromMobile()
                    }
                }
            }
        )


        textInputLayoutEnterAmount.editText!!.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().isNotEmpty()) {
                        btnMobileRecharge.text =
                            "Proceed to Pay ${Utils.formatAmount(p0.toString().toDouble())}"
                    } else {
                        btnMobileRecharge.text =
                            "Proceed to Pay"
                    }
                }
            }
        )
        presenter.getProviderList()

        btnMobileRecharge.setOnClickListener {
            if (isValide) {
                val mobileRechargeReq = doRetriveModel().getMobileRechargeRequest()
                mobileRechargeReq.token = EasyPaisaApp.getLoggedInUser()?.apptoken!!
                mobileRechargeReq.userId = EasyPaisaApp.getLoggedInUser()?.id!!.toString()
                mobileRechargeReq.amount = textInputLayoutEnterAmount.editText!!.text.toString()
                mobileRechargeReq.number = textInputLayoutRechargeMobNo.editText!!.text.toString()
                mobileRechargeReq.provider = selectedoperator?.id.toString()
                mobileRechargeReq.transactionType = "pay"
                presenter.doMobileRecharge(mobileRechargeReq)
            }
        }
    }

    private fun disableEnableSelectPlan() {
        if (selectedoperator?.id != -1 && textInputLayoutRechargeMobNo.editText!!.text.toString()
                .trim().length == 10
        ) {
            tvBrowsPlan.isEnabled = true
            tvBrowsPlan.alpha = 1F
        } else {
            tvBrowsPlan.isEnabled = false
            tvBrowsPlan.alpha = 0.5F
        }
    }

    val isValide: Boolean
        get() {
            if (textInputLayoutRechargeMobNo.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutRechargeMobNo.error = "Enter mobile number"
                textInputLayoutRechargeMobNo.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutRechargeMobNo.isErrorEnabled = false
            }

            if (textInputLayoutRechargeMobNo.editText!!.text.toString().trim().length != 10) {
                textInputLayoutRechargeMobNo.error = "Invalid mobile number"
                textInputLayoutRechargeMobNo.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutRechargeMobNo.isErrorEnabled = false
            }

            if (selectedoperator == null || (selectedoperator != null && selectedoperator?.id == -1)) {
                return false
                showError("Select Operator")
            }

            if (textInputLayoutEnterAmount.editText!!.text.toString()
                    .isEmpty() || (textInputLayoutEnterAmount.editText!!.text.toString()
                    .isNotEmpty()
                        && textInputLayoutEnterAmount.editText!!.text.toString().toInt() <= 0)
            ) {
                textInputLayoutEnterAmount.error = "Enter Recharge Amount"
                textInputLayoutEnterAmount.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutEnterAmount.isErrorEnabled = false
            }

            return true
        }

    override fun getViewActivity(): Activity {
        return this@MobileRechargeActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
        if (!isConnect)
            showToast(getString(R.string.no_internet_connection_please_check_internet_connection))
    }

    override fun doRetriveModel(): MobileRechargeModel {
        return model
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SELECT_PLAN -> {
                if (resultCode == Activity.RESULT_OK) {
                    val type = object : TypeToken<MobileRechargePlanResponse.Plan>() {}.type
                    val resultData = data?.getStringExtra("result")
                    selectedMobilePlan = EasyPaisaApp.getGson()
                        .fromJson<MobileRechargePlanResponse.Plan>(resultData, type)
                    selectedMobilePlan.let {
                        textInputLayoutEnterAmount.editText?.setText(it?.amount)
                        btnMobileRecharge.text =
                            "Proceed to Pay ${Utils.formatAmount(it?.amount!!.toDouble())}"
                    }
                }
            }
        }
    }

    override fun onMobileOperator() {
        val response = doRetriveModel().getDomain().mobileProviderResponse
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            arrayListProvider.addAll(response.message.providers)
            providerAdapter.notifyDataSetChanged()
        }
    }

    override fun onMobileOperatorByMobile() {
        val response = doRetriveModel().getDomain().mobileOperatorFromMobile
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            selectedoperator = null
            arrayListProvider.forEach {
                if (it.id == response.provider) {
                    selectedoperator = it
                    return@forEach
                }
            }
            selectedoperator?.let {
                if (it.id != 0) {
                    edtSelectElectricityBoard.setText(it.name)
                }
            }
        }
    }

    override fun onMobileRechargeDone() {
        val response = doRetriveModel().getDomain().mobileRechargeResponse
        val intent = Intent(getViewActivity(), MobileRechargeReceipt::class.java)
        val type = object : TypeToken<MobileRechargeResponse>() {}.type
        val json = EasyPaisaApp.getGson().toJson(response, type)
        intent.putExtra(Constants.UI.MOBILE_RESPONSE, json)
        startActivity(intent)
        /*if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            finish()
        }*/
    }

    @SuppressLint("LongLogTag")
    private fun openBoardDialog() {
        bottomsheetRechargeProvider =
            BottomsheetRechargeProider(getViewActivity(), arrayListProvider) { board ->
                Log.e(TAG, "BoardFor ${board.name}")
                if (selectedoperator != null && selectedoperator == board) {
                    //Already has selected that operator
                    return@BottomsheetRechargeProider
                }
                selectedoperator = board
                edtSelectElectricityBoard.setText(selectedoperator?.name)
                disableEnableSelectPlan()
            }
        bottomsheetRechargeProvider.show(supportFragmentManager, "mobileRecharge")
    }
}
