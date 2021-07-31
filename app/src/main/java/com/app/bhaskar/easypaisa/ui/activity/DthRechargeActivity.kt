package com.app.bhaskar.easypaisa.ui.activity

import aepsapp.easypay.com.aepsandroid.fragments.BottomsheetDthProvider
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.DthRechargeActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.DthRechargeActivityPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.DthRechargePresenterImpl
import com.app.bhaskar.easypaisa.response_model.ElectricityBillPaymentResponse
import com.app.bhaskar.easypaisa.response_model.RechargeProviderResponse
import com.app.bhaskar.easypaisa.response_model.SearchRemitterResponse
import com.app.bhaskar.easypaisa.ui.listener.OnUserLocationListener
import com.app.bhaskar.easypaisa.ui.service.GoogleFuesedLocationService
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.google.gson.reflect.TypeToken
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_dth_recharge.*
import kotlinx.android.synthetic.main.activity_dth_recharge.btnProceedToPay
import kotlinx.android.synthetic.main.activity_electricity_bill_payment.*
import kotlinx.android.synthetic.main.toolbar_simple.*
import javax.inject.Inject

class DthRechargeActivity : BaseActivity(), DthRechargeActivityPresenter.DthRechargeView {

    companion object {
        private const val TAG = "DthRechargeActivity"
    }

    private lateinit var model: DthRechargeActivityModel
    private lateinit var googleFusedLocation: GoogleFuesedLocationService

    @Inject
    lateinit var presenter: DthRechargePresenterImpl
    private var arrayListOperator = ArrayList<RechargeProviderResponse.Message.Provider>()
    private var providerHasClicked = false
    private var selectedProvider: RechargeProviderResponse.Message.Provider? = null
    private lateinit var bottomsheetProvider: BottomsheetDthProvider
    private var stateHasClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dth_recharge)
        model = DthRechargeActivityModel(getViewActivity())
        presenter = DthRechargePresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        tvToolbarTitle.text = "DTH Recharges"
        ivHomeBack.setOnClickListener {
            onBackPressed()
        }
        initViews()
    }

    private fun initViews() {
        presenter.getRechargeProvider()
        edtSelectOperator.setOnClickListener {
            if (arrayListOperator.isNotEmpty()) {
                openProviderDialog()
            } else {
                stateHasClicked = true
                presenter.getRechargeProvider()
            }
        }

        googleFusedLocation = GoogleFuesedLocationService(getViewActivity(), object :
            OnUserLocationListener {
            override fun onUserLocationSuccess(location: Location) {
                if (EasyPaisaApp.getUserLatLng() == null) {
                    showToast("User location not found, please enable GPS")
                    return
                }
                doRecharge()
            }

            override fun onUserLocationFail(failMessage: String) {
            }

        }, true)

        edtEnterCustomerAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    btnProceedToPay.text =
                        "Proceed"
                } else {
                    btnProceedToPay.text =
                        "Proceed to Pay ${Utils.formatAmount(p0.toString().toDouble())}"
                }
                enableDisableButton()
            }
        })
        btnProceedToPay.setOnClickListener {
            if (EasyPaisaApp.getUserLatLng() == null) {
                googleFusedLocation.needToShowDialog = true
                googleFusedLocation.accessUserCurrentLocation()
                return@setOnClickListener
            }

            doRecharge()
        }
    }

    private fun doRecharge() {
        doRetriveModel().getElectricityBillPaymentRequest().amount =
            edtEnterCustomerAmount.text.toString().trim()
        doRetriveModel().getElectricityBillPaymentRequest().token =
            EasyPaisaApp.getLoggedInUser()?.apptoken!!
        doRetriveModel().getElectricityBillPaymentRequest().userId =
            EasyPaisaApp.getLoggedInUser()?.id!!.toString()
        doRetriveModel().getElectricityBillPaymentRequest().provider =
            selectedProvider?.id.toString()
        doRetriveModel().getElectricityBillPaymentRequest().transactionType = "pay"
        doRetriveModel().getElectricityBillPaymentRequest().number =
            edtEnterCustomerId.text.toString().trim()
        presenter.rechargeDth()
    }

    private fun enableDisableButton() {
        if (edtEnterCustomerAmount.text.isNullOrEmpty()
            || edtEnterCustomerId.text.isNullOrEmpty()
        ) {
            btnProceedToPay.isEnabled = false
            btnProceedToPay.alpha = 0.5F
        } else {
            btnProceedToPay.isEnabled = true
            btnProceedToPay.alpha = 1F
        }
    }

    private fun openProviderDialog() {
        bottomsheetProvider =
            BottomsheetDthProvider(getViewActivity(), arrayListOperator) { provider ->
                Log.e(TAG, "Selected Provider ${provider.name}")
                if (selectedProvider != null && selectedProvider == provider) {
                    return@BottomsheetDthProvider
                }

                selectedProvider = provider
                edtSelectOperator.setText(selectedProvider?.name)
                updateUI()

                if (!Utils.isNetworkConnected(getViewActivity())) {
                    showToast(getString(R.string.no_internet_connection_please_check_internet_connection))
                    return@BottomsheetDthProvider
                }
            }

        bottomsheetProvider.show(supportFragmentManager, "provider")
    }

    private fun updateUI() {
        if (selectedProvider != null) {
            gpDthEntry.visibility = View.VISIBLE
            edtEnterCustomerAmount.setText("")
            edtEnterCustomerId.setText("")
        } else {
            gpDthEntry.visibility = View.GONE
        }
    }

    override fun doRetriveModel(): DthRechargeActivityModel {
        return model
    }

    override fun getViewActivity(): Activity {
        return this@DthRechargeActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
        if (!isConnect) {
            showToast(getString(R.string.no_internet_connection_please_check_internet_connection))
        }
    }

    override fun onProviderResponse() {
        val res = doRetriveModel().getDomain().providerResponse
        if (res.status == Constants.ApiResponse.RES_SUCCESS) {
            arrayListOperator.clear()
            arrayListOperator.addAll(res.message.providers)
            if (stateHasClicked) {
                openProviderDialog()
                stateHasClicked = false
            }
        }
    }

    override fun onDthRecharge() {
        val response  = doRetriveModel().getDomain().electricityBillPaymentResponse

        val intent = Intent(getViewActivity(), ElectricityBillPaymentReceiptActivity::class.java)
        val type = object : TypeToken<ElectricityBillPaymentResponse>() {}.type
        val json = EasyPaisaApp.getGson().toJson(response, type)
        intent.putExtra(Constants.UI.DTH_RESPONSE, json)
        intent.putExtra("state", "")
        startActivity(intent)
        /*if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            finish()
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            3030 -> {
                if (resultCode == Activity.RESULT_OK) {
                    googleFusedLocation.getUserLocation()
                }
            }
        }
    }
}