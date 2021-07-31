package com.app.bhaskar.easypaisa.ui.activity

import aepsapp.easypay.com.aepsandroid.fragments.BottomsheetElecricityBoard
import aepsapp.easypay.com.aepsandroid.fragments.BottomsheetElecricityState
import android.annotation.SuppressLint
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
import com.app.bhaskar.easypaisa.mvp.model.ElectricityBillPaymentModel
import com.app.bhaskar.easypaisa.mvp.presenter.ElectricityBillPaymentPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.ElectricityBillPaymentPresenterImpl
import com.app.bhaskar.easypaisa.response_model.*
import com.app.bhaskar.easypaisa.ui.listener.OnUserLocationListener
import com.app.bhaskar.easypaisa.ui.service.GoogleFuesedLocationService
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.google.gson.reflect.TypeToken
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_electricity_bill_payment.*
import kotlinx.android.synthetic.main.toolbar_simple.*
import javax.inject.Inject

class ElectricityBillPaymentActivity : BaseActivity(),
    ElectricityBillPaymentPresenter.ElecricityBillPaymentView {


    private var selectedElBoard: ElectricityBoardResponse.Message.Provider? = null
    private var selectedState: EelectricityStateResponse.Message.State? = null
    private var stateHasClicked = false
    private var boardHasClicked = false
    private var arrayListStates = ArrayList<EelectricityStateResponse.Message.State>()
    private var arrayListBoard = ArrayList<ElectricityBoardResponse.Message.Provider>()
    private lateinit var bottomsheetElecricityState: BottomsheetElecricityState
    private lateinit var bottomsheetElecricityBoard: BottomsheetElecricityBoard
    private var billDetailResponse: FetchElBillDetailResponse? = null
    private var serviceFor = Constants.AvailableService.SERVICE_BILL_ELECTRICITY
    private lateinit var googleFusedLocation: GoogleFuesedLocationService

    override fun doRetriveModel(): ElectricityBillPaymentModel {
        return model
    }

    companion object {
        const val TAG = "ElectricityBillPaymentActivity"
    }

    private lateinit var model: ElectricityBillPaymentModel

    @Inject
    lateinit var presenter: ElectricityBillPaymentPresenterImpl

    override fun getViewActivity(): Activity {
        return this@ElectricityBillPaymentActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
        if (!isConnect) {
            showToast(getString(R.string.no_internet_connection_please_check_internet_connection))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_electricity_bill_payment)
        tvToolbarTitle.text = "Bill payment"
        ivHomeBack.setOnClickListener {
            onBackPressed()
        }
        model = ElectricityBillPaymentModel(getViewActivity())
        presenter = ElectricityBillPaymentPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        initViews()
    }

    @SuppressLint("LongLogTag")
    private fun initViews() {
        if (intent.hasExtra(Constants.UI.SERVICE_FOR)) {
            serviceFor = intent.getIntExtra(
                Constants.UI.SERVICE_FOR,
                Constants.AvailableService.SERVICE_BILL_ELECTRICITY
            )
        }
        if (!Utils.isNetworkConnected(getViewActivity())) {
            showToast(getString(R.string.no_internet_connection_please_check_internet_connection))
            finish()
        }
        googleFusedLocation = GoogleFuesedLocationService(getViewActivity(), object :
            OnUserLocationListener {
            override fun onUserLocationSuccess(location: Location) {
                if (EasyPaisaApp.getUserLatLng() == null) {
                    showToast("User location not found, please enable GPS")
                    return
                }
                doTransaction()
            }

            override fun onUserLocationFail(failMessage: String) {
            }

        }, true)

        tvSelectElectricityBoard.text =
            when (serviceFor) {
                Constants.AvailableService.SERVICE_BILL_ELECTRICITY -> "Electricity Board"
                Constants.AvailableService.SERVICE_WATER -> "Water Board"
                Constants.AvailableService.SERVICE_LPG_GAS -> "Lpg Gas Board"
                Constants.AvailableService.SERVICE_PIPED_GAS -> "Piped Gas Board"
                Constants.AvailableService.SERVICE_INSURANCE -> "Insurance Operator"
                Constants.AvailableService.SERVICE_POSTPAID -> "Postpaid Operator"
                else -> "Electricity Board"
            }
        edtSelectElectricityBoard.hint =
            when (serviceFor) {
                Constants.AvailableService.SERVICE_BILL_ELECTRICITY -> "Select Electricity Board"
                Constants.AvailableService.SERVICE_WATER -> "Select Water Board"
                Constants.AvailableService.SERVICE_LPG_GAS -> "Select Lpg Gas Board"
                Constants.AvailableService.SERVICE_PIPED_GAS -> "Select Piped Gas Board"
                Constants.AvailableService.SERVICE_INSURANCE -> "Select Insurance Operator"
                Constants.AvailableService.SERVICE_POSTPAID -> "Select Postpaid Operator"
                else -> "Select Electricity Board"
            }

        when (serviceFor) {
            Constants.AvailableService.SERVICE_BILL_ELECTRICITY,
            Constants.AvailableService.SERVICE_WATER -> {
                presenter.getElStates()
            }
            else -> {
                tvSelectState.visibility = View.GONE
                edtSelectState.visibility = View.GONE
                groupBoard.visibility = View.VISIBLE
                presenter.getBillBoard("no")
            }
        }

        edtSelectState.setOnClickListener {
            if (arrayListStates.isNotEmpty()) {
                openStateDialog()
            } else {
                stateHasClicked = true
                presenter.getElStates()
            }
        }

        edtSelectElectricityBoard.setOnClickListener {
            if (arrayListBoard.isNotEmpty()) {
                openBoardDialog()
            } else {
                /*boardHasClicked = true
                when (serviceFor) {
                    Constants.AvailableService.SERVICE_BILL_ELECTRICITY, Constants.AvailableService.SERVICE_WATER -> {
                        presenter.getBillBoard(selectedState?.id!!.toString())
                    }
                    else -> {
                        presenter.getBillBoard("no")
                    }
                }*/
                Utils.showAlert(getViewActivity(),"Provider not found","Easy Paisa",View.OnClickListener {  })
            }
        }

        textInputLayoutCustomerId.editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                constraintLayoutBillDetail.visibility = View.GONE
                billDetailResponse = null
                if (selectedElBoard != null && selectedElBoard?.billfetch == "yes") {
                    btnProceedToPay.text = "Fetch Bill"
                    if (p0.toString().isNotEmpty()) {
                        btnProceedToPay.isEnabled = true
                        btnProceedToPay.alpha = 1F
                    } else {
                        btnProceedToPay.isEnabled = false
                        btnProceedToPay.alpha = 0.5F
                    }
                } else if (selectedElBoard != null && selectedElBoard?.billfetch != "yes") {
                    enableDisableButton()
                }
            }
        })

        textInputLayoutEnterAmount.editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    btnProceedToPay.text =
                        "Proceed to Pay"
                } else {
                    btnProceedToPay.text =
                        "Proceed to Pay ${Utils.formatAmount(p0.toString().toDouble())}"
                }
                enableDisableButton()
            }
        })

        btnProceedToPay.setOnClickListener {
            if (textInputLayoutExtraField.visibility == View.VISIBLE && textInputLayoutExtraField.editText?.text.toString()
                    .trim().isEmpty()
            ) {
                textInputLayoutExtraField.error = "Please enter the value."
                return@setOnClickListener
            }

            if (EasyPaisaApp.getUserLatLng() == null) {
                googleFusedLocation.needToShowDialog = true
                googleFusedLocation.accessUserCurrentLocation()
                return@setOnClickListener
            }
            doTransaction()
        }
    }

    private fun doTransaction() {
        textInputLayoutExtraField.isErrorEnabled = false
        if (billDetailResponse != null && selectedElBoard?.billfetch == "yes") {
            //Proceed to pay by using fetched bill details
            doRetriveModel().getElectricityBillPaymentRequest().amount =
                billDetailResponse?.data?.dueamount!!
            doRetriveModel().getElectricityBillPaymentRequest().token =
                EasyPaisaApp.getLoggedInUser()?.apptoken!!
            doRetriveModel().getElectricityBillPaymentRequest().userId =
                EasyPaisaApp.getLoggedInUser()?.id!!.toString()
            doRetriveModel().getElectricityBillPaymentRequest().provider =
                selectedElBoard?.id.toString()
            doRetriveModel().getElectricityBillPaymentRequest().transactionType = "pay"
            doRetriveModel().getElectricityBillPaymentRequest().number =
                textInputLayoutCustomerId.editText!!.text.toString().trim()
            doRetriveModel().getElectricityBillPaymentRequest().mobile =
                textInputLayoutExtraField.editText!!.text.toString().trim()
            doRetriveModel().getElectricityBillPaymentRequest().latitude =
                EasyPaisaApp.getUserLatLng()?.latitude.toString()
            doRetriveModel().getElectricityBillPaymentRequest().longitude =
                EasyPaisaApp.getUserLatLng()?.longitude.toString()
            presenter.payElectricityBill()
        } else if (selectedElBoard?.billfetch == "yes" && billDetailResponse == null) {
            //First Fetch the bill payment
            doRetriveModel().getFetchElBillDetailRequest().token =
                EasyPaisaApp.getLoggedInUser()?.apptoken!!
            doRetriveModel().getFetchElBillDetailRequest().userId =
                EasyPaisaApp.getLoggedInUser()?.id!!.toString()
            doRetriveModel().getFetchElBillDetailRequest().provider =
                selectedElBoard?.id.toString()
            doRetriveModel().getFetchElBillDetailRequest().transactionType = "getbilldetails"
            doRetriveModel().getFetchElBillDetailRequest().number =
                textInputLayoutCustomerId.editText!!.text.toString().trim()
            doRetriveModel().getFetchElBillDetailRequest().mobile =
                textInputLayoutExtraField.editText!!.text.toString().trim()
            doRetriveModel().getElectricityBillPaymentRequest().latitude =
                EasyPaisaApp.getUserLatLng()?.latitude.toString()
            doRetriveModel().getElectricityBillPaymentRequest().longitude =
                EasyPaisaApp.getUserLatLng()?.longitude.toString()
            presenter.getchBillDetails()
        } else {
            doRetriveModel().getElectricityBillPaymentRequest().amount =
                textInputLayoutEnterAmount.editText!!.text.toString().trim()
            doRetriveModel().getElectricityBillPaymentRequest().token =
                EasyPaisaApp.getLoggedInUser()?.apptoken!!
            doRetriveModel().getElectricityBillPaymentRequest().userId =
                EasyPaisaApp.getLoggedInUser()?.id!!.toString()
            doRetriveModel().getElectricityBillPaymentRequest().provider =
                selectedElBoard?.id.toString()
            doRetriveModel().getElectricityBillPaymentRequest().transactionType = "pay"
            doRetriveModel().getElectricityBillPaymentRequest().number =
                textInputLayoutCustomerId.editText!!.text.toString().trim()
            doRetriveModel().getElectricityBillPaymentRequest().latitude =
                EasyPaisaApp.getUserLatLng()?.latitude.toString()
            doRetriveModel().getElectricityBillPaymentRequest().longitude =
                EasyPaisaApp.getUserLatLng()?.longitude.toString()
            presenter.payElectricityBill()
        }
    }


    private fun enableDisableButton() {
        btnProceedToPay.text = "Proceed to Pay"
        if (textInputLayoutCustomerId.editText!!.text.isNullOrEmpty()
            || textInputLayoutEnterAmount.editText!!.text.isNullOrEmpty()
        ) {
            btnProceedToPay.isEnabled = false
            btnProceedToPay.alpha = 0.5F
        } else {
            btnProceedToPay.isEnabled = true
            btnProceedToPay.alpha = 1F
        }
    }

    @SuppressLint("LongLogTag")
    private fun openBoardDialog() {
        bottomsheetElecricityBoard =
            BottomsheetElecricityBoard(getViewActivity(), arrayListBoard) { board ->
                Log.e(TAG, "BoardFor ${board.name}")
                if (!Utils.isNetworkConnected(getViewActivity())) {
                    showToast(getString(R.string.no_internet_connection_please_check_internet_connection))
                    return@BottomsheetElecricityBoard
                }
                if (selectedElBoard != null && selectedElBoard == board) {
                    //Already has selected that state
                    return@BottomsheetElecricityBoard
                }
                selectedElBoard = board
                updateUI("change_board")
             }
        bottomsheetElecricityBoard.show(supportFragmentManager, "elboard")
    }

    @SuppressLint("LongLogTag")
    private fun openStateDialog() {
        bottomsheetElecricityState =
            BottomsheetElecricityState(getViewActivity(), arrayListStates) { state ->
                Log.e(TAG, "StateFor ${state.state}")
                if (!Utils.isNetworkConnected(getViewActivity())) {
                    showToast(getString(R.string.no_internet_connection_please_check_internet_connection))
                    return@BottomsheetElecricityState
                }
                if (selectedState != null && selectedState == state) {
                    //Already has selected that state
                    return@BottomsheetElecricityState
                }
                selectedState = state
                updateUI("change_state")
                presenter.getBillBoard(state.id.toString())
            }

        bottomsheetElecricityState.show(supportFragmentManager, "elstate")
    }

    private fun updateUI(action: String) {
        when (action) {
            "change_state" -> {
                selectedElBoard = null
                billDetailResponse = null
                edtSelectState.setText(selectedState?.state)
                edtSelectElectricityBoard.setText("")
                groupBoard.visibility = View.VISIBLE
                textInputLayoutCustomerId.visibility = View.GONE
                textInputLayoutEnterAmount.visibility = View.GONE
                constraintLayoutBillDetail.visibility = View.GONE
                groupButtonProceed.visibility = View.GONE
            }
            "change_board" -> {
                billDetailResponse = null
                edtSelectElectricityBoard.setText(selectedElBoard?.name)

                //Customer id Field
                textInputLayoutCustomerId.editText!!.setText("")
                textInputLayoutCustomerId.visibility = View.VISIBLE
                textInputLayoutCustomerId.hint = selectedElBoard?.paramname
                textInputLayoutEnterAmount.editText?.setText("")
                groupButtonProceed.visibility = View.VISIBLE

                if (selectedElBoard?.billfetch == "yes") {
                    textInputLayoutEnterAmount.visibility = View.GONE
                    btnProceedToPay.text = getString(R.string.fetch_bill)
                } else {
                    //Amount Field
                    textInputLayoutEnterAmount.visibility = View.VISIBLE
                    btnProceedToPay.text = "Proceed to Pay"
                }

                if (selectedElBoard?.extraparamname != "none") {
                    textInputLayoutExtraField.visibility = View.VISIBLE
                    textInputLayoutExtraField.hint = selectedElBoard?.extraparamname
                } else {
                    textInputLayoutExtraField.visibility = View.GONE
                }
                constraintLayoutBillDetail.visibility = View.GONE
            }
        }
    }

    override fun onState() {
        val res = doRetriveModel().getDomain().electricityStateResponse
        if (res.status == Constants.ApiResponse.RES_SUCCESS) {
            arrayListStates.clear()
            arrayListStates.addAll(res.message.state)
            if (stateHasClicked) {
                openStateDialog()
                stateHasClicked = false
            }
        }
    }

    override fun onBoard() {
        val response = doRetriveModel().getDomain().electricityBoardResponse
        if (response.message.providers.isEmpty()){
            Utils.showAlert(getViewActivity(),"Provider not found","Easy Paisa",View.OnClickListener {  })
        }
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            arrayListBoard.clear()
            arrayListBoard.addAll(response.message.providers)
        }
    }

    override fun onBillPaid() {
        val response = doRetriveModel().getDomain().electricityBillPaymentResponse

        if (response.status == Constants.ApiResponse.RES_SUCCESS)
        {
            val intent = Intent(getViewActivity(), ElectricityBillPaymentReceiptActivity::class.java)
            val type = object : TypeToken<ElectricityBillPaymentResponse>() {}.type
            val json = EasyPaisaApp.getGson().toJson(response, type)
            when (doGetProvider()){
                "electricity" -> {
                    intent.putExtra(Constants.UI.ELECTRICITY_BILL_RESPONSE, json)
                }
                "gas" -> {
                    intent.putExtra(Constants.UI.GAS_RESPONSE, json)
                }
                "lpg" -> {
                    intent.putExtra(Constants.UI.LPG_RESPONSE, json)
                }
                "postpaid" -> {
                    intent.putExtra(Constants.UI.POSTPAID_RESPONSE, json)
                }
                "water" -> {
                    intent.putExtra(Constants.UI.WATER_RESPONSE, json)
                }
                "insurance" -> {
                    intent.putExtra(Constants.UI.INSURANCE, json)
                }
                else -> {
                }
            }

            if (selectedState != null){
                intent.putExtra("state", selectedState?.state)
            }else{
                intent.putExtra("state", "")
            }

            startActivity(intent)
        }else{
            Utils.showAlert(getViewActivity(),response.message,"Easy Paisa",View.OnClickListener {

            })
        }
    }

    override fun onFetchBill() {
        val response = doRetriveModel().getDomain().fetchElBillDetailResponse
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            billDetailResponse = response
            constraintLayoutBillDetail.visibility = View.VISIBLE
            tvCustomerName.text = response.data.customername
            tvCustomerBillDate.text = "-"
            tvCustomerDueDate.text = response.data.duedate
            tvCustomerBillAcount.text = Utils.formatAmount(response.data.dueamount.toDouble())
            btnProceedToPay.text =
                "Proceed to Pay ${Utils.formatAmount(response.data.dueamount.toDouble())}"
        } else {
            billDetailResponse = null
            constraintLayoutBillDetail.visibility = View.GONE
            showError(response.message)
        }
    }

    override fun doGetProvider(): String {
        when (serviceFor) {
            Constants.AvailableService.SERVICE_BILL_ELECTRICITY -> {
                return "electricity"
            }
            Constants.AvailableService.SERVICE_LPG_GAS -> {
                return "lpg"
            }
            Constants.AvailableService.SERVICE_PIPED_GAS -> {
                return "gas"
            }
            Constants.AvailableService.SERVICE_INSURANCE -> {
                return "insurance"
            }
            Constants.AvailableService.SERVICE_POSTPAID -> {
                return "postpaid"
            }
            Constants.AvailableService.SERVICE_WATER -> {
                return "water"
            }
            else -> {
                return "electricity"
            }
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
