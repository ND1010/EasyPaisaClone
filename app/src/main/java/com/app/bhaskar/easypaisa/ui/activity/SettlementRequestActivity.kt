package com.app.bhaskar.easypaisa.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.SettlementRequestActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.SettlementRequestPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.SettlementRequestPresenterImpl
import com.app.bhaskar.easypaisa.request_model.MoveToBankWalletRequest
import com.app.bhaskar.easypaisa.response_model.SearchRemitterResponse
import com.app.bhaskar.easypaisa.response_model.WalletRequiredDataResponse
import com.app.bhaskar.easypaisa.ui.listener.OnUserLocationListener
import com.app.bhaskar.easypaisa.ui.service.GoogleFuesedLocationService
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.google.gson.reflect.TypeToken
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_settlement_request.*
import kotlinx.android.synthetic.main.toolbar_simple.*
import javax.inject.Inject

class SettlementRequestActivity : BaseActivity(), SettlementRequestPresenter.SettlementRequestView {

    companion object {
        const val TAG = "SettlementRequestActivity"
    }

    private lateinit var bankSattArrayAdapter: ArrayAdapter<WalletRequiredDataResponse.Message.Account>
    private var responseWalletLoad: WalletRequiredDataResponse? = null
    private var selectedType: Int = -1
    private lateinit var model: SettlementRequestActivityModel
    private lateinit var googleFusedLocation: GoogleFuesedLocationService

    @Inject
    lateinit var presenter: SettlementRequestPresenterImpl
    private var arrayListSettlemntType = ArrayList<String>()
    private var arrayListSettlemntBank = ArrayList<WalletRequiredDataResponse.Message.Account>()
    private var useraccount = ""
    private var userbank = ""
    private var userifsc = ""
    private var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settlement_request)
        ivHomeBack.setOnClickListener { onBackPressed() }
        tvToolbarTitle.text = "Settlement Request"
        model = SettlementRequestActivityModel(getViewActivity())
        presenter = SettlementRequestPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        presenter.getRequiredData()

        textInputLayoutAccountNo.visibility = View.GONE
        textInputLayoutIfscCode.visibility = View.GONE
        textInputLayoutBankName.visibility = View.GONE
        constraintLayoutAccountDetail.visibility = View.GONE
        googleFusedLocation = GoogleFuesedLocationService(getViewActivity(), object :
            OnUserLocationListener {
            override fun onUserLocationSuccess(location: Location) {
                if (EasyPaisaApp.getUserLatLng() == null) {
                    showToast("User location not found, please enable GPS")
                    return
                }
                doMoveWalletBank()
            }

            override fun onUserLocationFail(failMessage: String) {
            }

        }, true)

        arrayListSettlemntType.add("Select Settlement Type")
        arrayListSettlemntType.add("Wallet to Bank")
        arrayListSettlemntType.add("Wallet to Wallet")

        val bankArrayAdapter = object :
            ArrayAdapter<String>(
                applicationContext,
                android.R.layout.simple_spinner_item,
                arrayListSettlemntType
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

        spinnerSettlementType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(adapterView: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    type = when (pos) {
                        1 -> {
                            "bank"
                        }
                        2 -> {
                            "wallet"
                        }
                        else -> ""
                    }
                }
            }

        bankArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSettlementType.adapter = bankArrayAdapter

        val accountBlank = WalletRequiredDataResponse.Message.Account()
        accountBlank.bank = "Select Bank"
        arrayListSettlemntBank.add(accountBlank)

        bankSattArrayAdapter = object :
            ArrayAdapter<WalletRequiredDataResponse.Message.Account>(
                getViewActivity(),
                android.R.layout.simple_spinner_item,
                arrayListSettlemntBank
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

        spinnerSettlementBank.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(adapterView: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    if (pos !=0){
                        constraintLayoutAccountDetail.visibility = View.VISIBLE
                        useraccount = arrayListSettlemntBank[pos].id.toString()
                        userbank = arrayListSettlemntBank[pos].bank
                        userifsc = arrayListSettlemntBank[pos].ifsc
                        tvBankName.text = userbank
                        tvCustomerDueDate.text = arrayListSettlemntBank[pos].account
                        tvCustomerBillDate.text = userifsc
                    }
                }
            }

        bankSattArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSettlementBank.adapter = bankSattArrayAdapter

        btnProceedSettlement.setOnClickListener {
            if (isValide) {
                if (EasyPaisaApp.getUserLatLng() == null) {
                    googleFusedLocation.needToShowDialog = true
                    googleFusedLocation.accessUserCurrentLocation()
                    return@setOnClickListener
                }
                doMoveWalletBank()
            }
        }
        tvAddNewAccount.setOnClickListener {
            startActivityForResult(Intent(getViewActivity(),AddNewBankActivity::class.java),Constants.UI.ADD_BANk)
        }
    }

    private fun doMoveWalletBank() {



        val requst = MoveToBankWalletRequest(
            account = useraccount,
            bank = userbank,
            ifsc = userifsc,
            type = type,
            token = EasyPaisaApp.getLoggedInUser()?.apptoken.toString(),
            userId = EasyPaisaApp.getLoggedInUser()?.id.toString(),
            amount = textInputLayoutSettlementAmount.editText!!.text.toString().trim(),
            latitude = EasyPaisaApp.getUserLatLng()?.latitude.toString(),
            longitude = EasyPaisaApp.getUserLatLng()?.longitude.toString()
        )
        presenter.requestForMoveWalletBank(requst)
    }

    override fun doRetriveModel(): SettlementRequestActivityModel {
        return model
    }

    override fun getViewActivity(): Activity {
        return this@SettlementRequestActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    override fun onWalletLoadData() {
        responseWalletLoad = doRetriveModel().getDomain().walletRequiredDataResponse
        if (responseWalletLoad?.status == Constants.ApiResponse.RES_SUCCESS) {
            if(responseWalletLoad?.message?.accounts.isNullOrEmpty()){
                textInputLayoutAccountNo.visibility = View.GONE
                textInputLayoutIfscCode.visibility = View.GONE
                textInputLayoutBankName.visibility = View.GONE
                constraintLayoutAccountDetail.visibility = View.GONE
                tvBankName.text = "-"
                tvCustomerDueDate.text = "-"
                tvCustomerBillDate.text = "-"
                startActivityForResult(Intent(getViewActivity(),AddNewBankActivity::class.java),Constants.UI.ADD_BANk)
                return
            }

            arrayListSettlemntBank.clear()
            val accountBlank = WalletRequiredDataResponse.Message.Account()
            accountBlank.bank = "Select Bank"
            arrayListSettlemntBank.add(accountBlank)
            responseWalletLoad?.message?.accounts?.let { arrayListSettlemntBank.addAll(it) }
            if(this::bankSattArrayAdapter.isInitialized) bankSattArrayAdapter.notifyDataSetChanged()


            /*if (responseWalletLoad?.message!!.useraccount.isNullOrEmpty()) {
                constraintLayoutAccountDetail.visibility = View.GONE
                textInputLayoutAccountNo.visibility = View.VISIBLE
                textInputLayoutIfscCode.visibility = View.VISIBLE
                textInputLayoutBankName.visibility = View.VISIBLE
            } else {
                textInputLayoutAccountNo.visibility = View.GONE
                textInputLayoutIfscCode.visibility = View.GONE
                textInputLayoutBankName.visibility = View.GONE
                constraintLayoutAccountDetail.visibility = View.VISIBLE

                useraccount = responseWalletLoad?.message!!.useraccount
                userbank = responseWalletLoad?.message!!.userbank
                userifsc = responseWalletLoad?.message!!.userifsc
                tvBankName.text = userbank
                tvCustomerDueDate.text = useraccount
                tvCustomerBillDate.text = userifsc
            }*/

        } else {
            showToast(getString(R.string.some_thing_wants_wong))
            finish()
        }
    }

    override fun showProgress() {
        progressBarSettlement.visibility = View.VISIBLE
        scrollviewSettlement.visibility = View.GONE
    }

    override fun hideProgress() {
        super.hideProgress()
        progressBarSettlement.visibility = View.GONE
        scrollviewSettlement.visibility = View.VISIBLE
    }

    val isValide: Boolean
        get() {
            if (useraccount.isEmpty()) {
                showToast("Please select bank account")
                return false
            }
            if (type.isEmpty()) {
                showToast("Please select settlement type")
                return false
            }

            if (textInputLayoutSettlementAmount.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutSettlementAmount.error = "Please enter amount"
                return false
            } else {
                textInputLayoutSettlementAmount.isErrorEnabled = false
            }

            if (textInputLayoutSettlementAmount.editText!!.text.toString().trim().isNotEmpty()
                && textInputLayoutSettlementAmount.editText!!.text.toString().trim().toDouble() <= 0
            ) {
                textInputLayoutSettlementAmount.error = "Invalid amount"
                return false
            } else {
                textInputLayoutSettlementAmount.isErrorEnabled = false
            }

            return true
        }


    override fun onMoveWalletOrBank() {
        val response = doRetriveModel().getDomain().walletLoadRequestResponse
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            Utils.showSuccessAlert(getViewActivity(),
                response.message, View.OnClickListener {
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
            Constants.UI.ADD_BANk -> {
                if (resultCode == Activity.RESULT_OK) {
                    presenter.getRequiredData()
                }
            }
        }
    }
}