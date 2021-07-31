package com.app.bhaskar.easypaisa.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.LoadWalletActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.LoadWalletPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.LoadWalletPresenterImpl
import com.app.bhaskar.easypaisa.request_model.WalletLoadRequest
import com.app.bhaskar.easypaisa.response_model.WalletRequiredDataResponse
import com.app.bhaskar.easypaisa.ui.listener.OnUserLocationListener
import com.app.bhaskar.easypaisa.ui.service.GoogleFuesedLocationService
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_load_wallet.*
import kotlinx.android.synthetic.main.toolbar_simple.*
import javax.inject.Inject

class LoadWalletActivity : BaseActivity(), LoadWalletPresenter.LoadWalletView {

    companion object {
        const val TAG = "LoadWalletActivity"
    }

    private var responseWalletLoad: WalletRequiredDataResponse? = null
    private lateinit var model: LoadWalletActivityModel
    private var arrayListBank = ArrayList<WalletRequiredDataResponse.Message.Bank>()
    private var arrayListPaymode = ArrayList<WalletRequiredDataResponse.Message.Paymode>()
    private var fundbankId = ""
    private var paymode = ""
    private var payDate = ""
    private lateinit var googleFusedLocation: GoogleFuesedLocationService

    @Inject
    lateinit var presenter: LoadWalletPresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_wallet)
        model = LoadWalletActivityModel(getViewActivity())
        presenter = LoadWalletPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        presenter.getRequiredData()
        ivHomeBack.setOnClickListener { onBackPressed() }
        tvToolbarTitle.text = "Wallet Load Request"

        googleFusedLocation = GoogleFuesedLocationService(getViewActivity(), object :
            OnUserLocationListener {
            override fun onUserLocationSuccess(location: Location) {
                if (EasyPaisaApp.getUserLatLng() == null) {
                    showToast("User location not found, please enable GPS")
                    return
                }
                doLoadWalletReq()
            }

            override fun onUserLocationFail(failMessage: String) {
            }

        }, true)

        edtSelectPayDate.setOnClickListener {
            presenter.openDatePicker()
        }

        btnProceedWalletLoad.setOnClickListener {
            if (isValide) {
                if (EasyPaisaApp.getUserLatLng() == null) {
                    googleFusedLocation.needToShowDialog = true
                    googleFusedLocation.accessUserCurrentLocation()
                    return@setOnClickListener
                }
                doLoadWalletReq()
            }
        }

        spinnerSelectDepositeBank.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(adapterView: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    fundbankId = arrayListBank[pos].id.toString()
                    if (pos>0)
                    {
                        constraintLayoutAccountDetail.visibility =View.VISIBLE
                    }else{
                        constraintLayoutAccountDetail.visibility =View.GONE
                    }
                    tvBankName.text = arrayListBank[pos].name
                    tvCustomerDueDate.text = arrayListBank[pos].account
                    tvCustomerBillDate.text = arrayListBank[pos].ifsc
                }
            }

        spinnerSelectPaymentMode.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(adapterView: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    paymode = arrayListPaymode[pos].id.toString()
                }
            }
    }

    private fun doLoadWalletReq() {
        val walletLoadReq = WalletLoadRequest(
            textInputLayoutWalletAmount.editText!!.text.toString().trim(),
            fundbankId,
            edtSelectPayDate.text.toString().trim(),
            paymode,
            textInputLayoutWalletRefNo.editText!!.text.toString().trim(),
            EasyPaisaApp.getLoggedInUser()?.apptoken!!,
            "request",
            EasyPaisaApp.getLoggedInUser()?.id!!.toString(),
            EasyPaisaApp.getUserLatLng()?.latitude.toString(),
            EasyPaisaApp.getUserLatLng()?.longitude.toString()
        )
        presenter.loadWalletRequest(walletLoadReq)
    }

    val isValide: Boolean
        get() {
            if (spinnerSelectDepositeBank.selectedItemPosition == 0) {
                showToast("Please select deposit bank")
                return false
            }
            if (textInputLayoutWalletAmount.editText!!.text.toString().trim().isEmpty() ||
                (textInputLayoutWalletAmount.editText!!.text.toString().trim()
                    .isNotEmpty() && textInputLayoutWalletAmount.editText!!.text.toString().trim()
                    .toDouble() <= 0)
            ) {
                showToast("Please enter request amount")
                return false
            }
            if (spinnerSelectPaymentMode.selectedItemPosition == 0) {
                showToast("Please select payment mode")
                return false
            }
            if (edtSelectPayDate.text.toString().trim().isEmpty()) {
                showToast("Please select Pay Date")
                return false
            }

            if (textInputLayoutWalletRefNo.editText?.text.toString().trim().isEmpty()) {
                showToast("Please enter transaction reference number")
                return false
            }

            return true
        }

    override fun doRetriveModel(): LoadWalletActivityModel {
        return model
    }


    override fun onWalletLoadData() {
        responseWalletLoad = doRetriveModel().getDomain().walletRequiredDataResponse
        if (responseWalletLoad?.status == Constants.ApiResponse.RES_SUCCESS) {
            arrayListBank.clear()
            arrayListBank.add(
                WalletRequiredDataResponse.Message.Bank(
                    "",
                    "",
                    "",
                    0,
                    "",
                    "Select Deposit Bank",
                    "0",
                    "",
                    0
                )
            )
            arrayListBank.addAll(responseWalletLoad?.message?.banks!!)
            arrayListPaymode.clear()
            arrayListPaymode.add(
                WalletRequiredDataResponse.Message.Paymode(
                    0,
                    "Select Payment Mode",
                    ""
                )
            )
            arrayListPaymode.addAll(responseWalletLoad?.message?.paymodes!!)

            val bankArrayAdapter = object :
                ArrayAdapter<WalletRequiredDataResponse.Message.Bank>(
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                    arrayListBank
                ) {
                override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val tv = view as TextView
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY)
                    } else {
                        tv.setTextColor(Color.BLACK)
                    }
                    return view
                }
            }
            bankArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerSelectDepositeBank.adapter = bankArrayAdapter

            val paymentModeArrayAdapter = object :
                ArrayAdapter<WalletRequiredDataResponse.Message.Paymode>(
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                    arrayListPaymode
                ) {
                override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val tv = view as TextView
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY)
                    } else {
                        tv.setTextColor(Color.BLACK)
                    }
                    return view
                }
            }
            paymentModeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerSelectPaymentMode.adapter = paymentModeArrayAdapter
        }
    }

    override fun getViewActivity(): Activity {
        return this@LoadWalletActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    override fun onWalletLoadSuccess() {
        val response = doRetriveModel().getDomain().walletLoadRequestResponse
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            Utils.showSuccessAlert(getViewActivity(),
                response.message,View.OnClickListener {
                    finish()
                })
        } else {
            Utils.showAlert(
                getViewActivity(),
                response.message,
                "Easy Paisa",
                View.OnClickListener {
                })
        }
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