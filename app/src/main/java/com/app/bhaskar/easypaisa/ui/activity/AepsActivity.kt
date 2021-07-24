package com.app.bhaskar.easypaisa.ui.activity

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.AepsActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.AepsPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.AepsPresenterImpl
import com.app.bhaskar.easypaisa.response_model.UserRequiredDataResponse
import com.app.bhaskar.easypaisa.ui.listener.OnUserLocationListener
import com.app.bhaskar.easypaisa.ui.service.GoogleFuesedLocationService
import com.app.bhaskar.easypaisa.utils.Checksum
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.easypay.epmoney.epmoneylib.baseframework.model.PaisaNikalConfiguration
import com.easypay.epmoney.epmoneylib.baseframework.model.PaisaNikalRequest
import com.easypay.epmoney.epmoneylib.response_model.AepsTransactionResponse
import com.easypay.epmoney.epmoneylib.response_model.BankMiniStatementResponse
import com.easypay.epmoney.epmoneylib.response_model.PaisaNikalTransactionResponse
import com.easypay.epmoney.epmoneylib.ui.activity.IntermidiateActivity
import com.easypay.epmoney.epmoneylib.utils.PaisaNikalConfig
import com.google.android.material.tabs.TabLayout
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_aeps.*
import kotlinx.android.synthetic.main.activity_dth_recharge.view.*
import kotlinx.android.synthetic.main.toolbar_simple.*
import org.apache.commons.codec.DecoderException
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.inject.Inject


class AepsActivity : BaseActivity(), AepsPresenter.AepsView {

    companion object {
        const val TAG = "AepsActivity"
    }

    private var ipAddress: String = ""
    private var selectedBank: UserRequiredDataResponse.Aepsbank? = null
    private var selectedAadharBank: UserRequiredDataResponse.Aadharbank? = null
    private var selectedYesBank: UserRequiredDataResponse.Yesbank? = null
    private var aepsServiceFor: Int = 1
    private var isCustFound = Constants.ApiResponse.RES_CNF

    @Inject
    lateinit var presenter: AepsPresenterImpl
    lateinit var model: AepsActivityModel
    private var transactionFor = "ser_withdraw"
    private lateinit var googleFusedLocation: GoogleFuesedLocationService
    private val CONFIG_ENVIRONMENT = PaisaNikalConfig.Config.ENVIRONMENT_PRODUCTION
    private val CONFIG_AGENT_ID_CODE = ""//
    private val CONFIG_AGENT_NMAE = "Easy Paisa"
    private var config: PaisaNikalConfiguration? = null
    private var apiRequest: PaisaNikalRequest? = null

    private val CODE_AEPS_TRANSACTION = 1010
    private val CODE_MICRO_TRANSACTION = 1011
    private val CODE_MINI_STATEMENT = 1012

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aeps)
        setupWindowAnimations()
        model = AepsActivityModel(getViewActivity())
        presenter = AepsPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        initView()
        setupTabs()
        ipAddress = Utils.getIPAddress(true)
    }

    private fun setupTabs() {
        val tabWithdrwal = tablayoutAeps.newTab()
        tabWithdrwal.text = getString(R.string.withdraw)
        tabWithdrwal.icon = ContextCompat.getDrawable(this@AepsActivity, R.drawable.ic_aeps_tab)

        val tabChkBalance = tablayoutAeps.newTab()
        tabChkBalance.text = getString(R.string.balance_info)
        tabChkBalance.icon =
            ContextCompat.getDrawable(this@AepsActivity, R.drawable.ic_balanceinfo_tab)

        val tabChkMiniStatement = tablayoutAeps.newTab()
        tabChkMiniStatement.text = getString(R.string.mini_statment)
        tabChkMiniStatement.icon =
            ContextCompat.getDrawable(this@AepsActivity, R.drawable.ic_mini_statement)

        val tabBankAadharPay = tablayoutAeps.newTab()
        tabBankAadharPay.text = getString(R.string.aadhaar_pay)
        tabBankAadharPay.icon =
            ContextCompat.getDrawable(this@AepsActivity, R.drawable.ic_aeps_aadhaarpay)

        if (!intent.hasExtra("transactionFor")) {
            tablayoutAeps.addTab(tabWithdrwal, true)
            tablayoutAeps.addTab(tabChkBalance, false)
            tablayoutAeps.tabMode = TabLayout.MODE_FIXED
            tablayoutAeps.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (aepsServiceFor == Constants.AvailableService.SERVICE_ICICI_AEPS) {
                        when (tab!!.position) {
                            /*0 -> {
                                transactionFor = getString(R.string.ser_withdraw)
                                updateAepsTnx()
                            }
                            1 -> {
                                transactionFor = getString(R.string.ser_aadhar_pay)
                                updateAepsTnx()
                            }
                            2 -> {
                                transactionFor = getString(R.string.ser_mini_statement)
                                updateAepsTnx()
                            }
                            3 -> {
                                transactionFor = getString(R.string.ser_balance)
                                updateAepsTnx()
                            }*/

                            0 -> {
                                transactionFor = getString(R.string.ser_withdraw)
                                updateAepsTnx()
                            }
                            1 -> {
                                transactionFor = getString(R.string.ser_balance)
                                updateAepsTnx()
                            }

                        }
                    } else {
                        when (tab!!.position) {
                            0 -> {
                                transactionFor = getString(R.string.ser_withdraw)
                                updateAepsTnx()
                            }
                            1 -> {
                                transactionFor = getString(R.string.ser_balance)
                                updateAepsTnx()
                            }
                        }
                    }

                }
            })
        }

        /*tablayoutAeps.addTab(tabWithdrwal, true)
        if (aepsServiceFor == Constants.AvailableService.SERVICE_ICICI_AEPS) {
            tablayoutAeps.addTab(tabBankAadharPay, false)
            tablayoutAeps.addTab(tabChkBalance, false)
            tablayoutAeps.addTab(tabChkMiniStatement, false)
        } else {
            tablayoutAeps.tabMode = TabLayout.MODE_FIXED
            tablayoutAeps.addTab(tabChkBalance, false)
        }*/

        tvAmount100.setOnClickListener {
            aepssearch_edtamount.setText("500")
        }
        tvAmount1000.setOnClickListener {
            aepssearch_edtamount.setText("1000")
        }
        tvAmount2000.setOnClickListener {
            aepssearch_edtamount.setText("2000")
        }
        tvAmount3000.setOnClickListener {
            aepssearch_edtamount.setText("3000")
        }

        btnProceed.setOnClickListener {
            doTransactionProcess()
        }
    }

    private fun doTransactionProcess() {
        if (aepsServiceFor == Constants.AvailableService.SERVICE_ICICI_AEPS
            || aepsServiceFor == Constants.AvailableService.SERVICE_EASYPAY_AEPS
        ) {
            if (isValide) {
                val aepsTxnService = doRetriveModel().getAepsRequest()
                aepsTxnService.mobile =
                    textInputLayoutAepsMobNo.editText!!.text.toString().trim()
                aepsTxnService.aadharNo =
                    textInputLayoutAepsAashaarNumber.editText!!.text.toString().trim()
                aepsTxnService.amount =
                    textInputLayoutEnterAmount.editText!!.text.toString().trim()
                if (isCustFound == Constants.ApiResponse.RES_SUCCESS) {
                    aepsTxnService.name = ""
                } else {
                    aepsTxnService.name =
                        textInputLayoutAepsUserName.editText!!.text.toString().trim()
                }
                when {
                    transactionFor == getString(R.string.ser_aadhar_pay) -> {
                        val bank = UserRequiredDataResponse.Aepsbank()
                        bank.id = selectedAadharBank!!.id
                        bank.bankName = selectedAadharBank!!.bankName
                        bank.iinno = selectedAadharBank!!.iinno
                        aepsTxnService.bank = bank
                    }
                    aepsServiceFor == Constants.AvailableService.SERVICE_EASYPAY_AEPS -> {
                        val bank = UserRequiredDataResponse.Aepsbank()
                        bank.id = selectedYesBank!!.id
                        bank.bankName = selectedYesBank!!.bankName
                        bank.iinno = selectedYesBank!!.bankIIN
                        aepsTxnService.bank = bank
                    }
                    else -> {
                        aepsTxnService.bank = selectedBank
                    }
                }
                aepsTxnService.transactionFor = transactionFor
                aepsTxnService.aepsServiceFor = aepsServiceFor
                presenter.goForTransactionAeps()
            }
            return
        }

        if (isValide) {
            if (EasyPaisaApp.getUserLatLng() == null) {
                googleFusedLocation.needToShowDialog =true
                googleFusedLocation.accessUserCurrentLocation()
                return
            }
            when (transactionFor) {
                getString(R.string.ser_withdraw) -> {
                    apiRequest = PaisaNikalRequest()
                    apiRequest?.amount = textInputLayoutEnterAmount.editText!!.text.toString()
                    var serviceCode = ""
                    when (aepsServiceFor) {
                        /*Constants.AvailableService.SERVICE_EASYPAY_AEPS -> {
                            //Call api for eaypay aeps withdrawal
                            doRetriveModel().getAepsTransactionRequest().aeps = "yes"
                            doRetriveModel().getAepsTransactionRequest().token =
                                EasyPaisaApp.getLoggedInUser()?.apptoken!!
                            doRetriveModel().getAepsTransactionRequest().userId =
                                EasyPaisaApp.getLoggedInUser()?.id.toString()
                            doRetriveModel().getAepsTransactionRequest().transactionType = "CW"
                            doRetriveModel().getAepsTransactionRequest().transactionAmount =
                                textInputLayoutEnterAmount.editText!!.text.toString().trim()
                            serviceCode =
                                PaisaNikalConfig.ApiServices.EASYPAY_AEPS_CASH_WITHDRAW.toString()
                        }*/
                        Constants.AvailableService.SERVICE_FINO_AEPS -> {
                            //Called api for fino aeps withdrawal
                            doRetriveModel().getAepsTransactionRequest().aeps = "fino"
                            doRetriveModel().getAepsTransactionRequest().token =
                                EasyPaisaApp.getLoggedInUser()?.apptoken!!
                            doRetriveModel().getAepsTransactionRequest().latitude =
                                EasyPaisaApp.getUserLatLng()?.latitude.toString()
                            doRetriveModel().getAepsTransactionRequest().longitude =
                                EasyPaisaApp.getUserLatLng()?.longitude.toString()
                            doRetriveModel().getAepsTransactionRequest().ip = ipAddress
                            doRetriveModel().getAepsTransactionRequest().userId =
                                EasyPaisaApp.getLoggedInUser()?.id.toString()
                            doRetriveModel().getAepsTransactionRequest().transactionType = "CW"
                            doRetriveModel().getAepsTransactionRequest().transactionAmount =
                                textInputLayoutEnterAmount.editText!!.text.toString().trim()
                            serviceCode =
                                PaisaNikalConfig.ApiServices.FINO_AEPS_CASH_WITHDRAW.toString()
                        }
                        /*Constants.AvailableService.SERVICE_MICRO_ATM -> {
                            //call api for micro atm withdrawal
                            doRetriveModel().getAepsTransactionRequest().aeps = "atm"
                            doRetriveModel().getAepsTransactionRequest().token =
                                EasyPaisaApp.getLoggedInUser()?.apptoken!!
                            doRetriveModel().getAepsTransactionRequest().userId =
                                EasyPaisaApp.getLoggedInUser()?.id.toString()
                            doRetriveModel().getAepsTransactionRequest().transactionType = "CW"
                            doRetriveModel().getAepsTransactionRequest().transactionAmount =
                                textInputLayoutEnterAmount.editText!!.text.toString().trim()

                            serviceCode =
                                PaisaNikalConfig.ApiServices.MATM_CREDIT_DEBIT_TRANSACTION.toString()
                        }*/
                    }
                    doRetriveModel().getAepsTransactionRequest().mobileNumber =
                        textInputLayoutAepsMobNo.editText!!.text.toString().trim()
                    apiRequest!!.mobileNumber =
                        textInputLayoutAepsMobNo.editText!!.text.toString().trim()
                    apiRequest!!.serviceCode = serviceCode
                    apiRequest!!.latitude = EasyPaisaApp.getUserLatLng()?.latitude.toString()
                    apiRequest!!.longitude = EasyPaisaApp.getUserLatLng()?.longitude.toString()
                    apiRequest!!.imei = EasyPaisaApp.getDeviceId()
                    presenter.doAepsTransaction()
                }
                getString(R.string.ser_balance) -> {
                    apiRequest = PaisaNikalRequest()
                    apiRequest?.amount = "0"

                    var serviceCode = ""
                    when (aepsServiceFor) {
                        /*Constants.AvailableService.SERVICE_EASYPAY_AEPS -> {
                            //called api for easy balance
                            doRetriveModel().getAepsTransactionRequest().aeps = "yes"
                            doRetriveModel().getAepsTransactionRequest().token =
                                EasyPaisaApp.getLoggedInUser()?.apptoken!!
                            doRetriveModel().getAepsTransactionRequest().userId =
                                EasyPaisaApp.getLoggedInUser()?.id.toString()
                            doRetriveModel().getAepsTransactionRequest().transactionType = "BE"
                            serviceCode =
                                PaisaNikalConfig.ApiServices.EASYPAY_AEPS_BALANCE_INQUIRY.toString()
                        }*/
                        Constants.AvailableService.SERVICE_FINO_AEPS -> {
                            //called api for fino balance
                            doRetriveModel().getAepsTransactionRequest().aeps = "fino"
                            doRetriveModel().getAepsTransactionRequest().latitude =
                                EasyPaisaApp.getUserLatLng()?.latitude.toString()
                            doRetriveModel().getAepsTransactionRequest().longitude =
                                EasyPaisaApp.getUserLatLng()?.longitude.toString()
                            doRetriveModel().getAepsTransactionRequest().ip = ipAddress
                            doRetriveModel().getAepsTransactionRequest().token =
                                EasyPaisaApp.getLoggedInUser()?.apptoken!!
                            doRetriveModel().getAepsTransactionRequest().userId =
                                EasyPaisaApp.getLoggedInUser()?.id.toString()
                            doRetriveModel().getAepsTransactionRequest().transactionType = "BE"

                            serviceCode =
                                PaisaNikalConfig.ApiServices.FINO_AEPS_BALANCE_INQUIRY.toString()

                        }
                        /*Constants.AvailableService.SERVICE_MICRO_ATM -> {

                            doRetriveModel().getAepsTransactionRequest().aeps = "atm"
                            doRetriveModel().getAepsTransactionRequest().token =
                                EasyPaisaApp.getLoggedInUser()?.apptoken!!
                            doRetriveModel().getAepsTransactionRequest().userId =
                                EasyPaisaApp.getLoggedInUser()?.id.toString()
                            doRetriveModel().getAepsTransactionRequest().transactionType = "BE"
                            serviceCode =
                                PaisaNikalConfig.ApiServices.MATM_CARD_BALANCE_INQUIRY.toString()

                        }*/
                    }
                    doRetriveModel().getAepsTransactionRequest().mobileNumber =
                        textInputLayoutAepsMobNo.editText!!.text.toString().trim()
                    apiRequest!!.mobileNumber =
                        textInputLayoutAepsMobNo.editText!!.text.toString()
                    apiRequest!!.serviceCode = serviceCode
                    apiRequest!!.latitude = EasyPaisaApp.getUserLatLng()?.latitude.toString()
                    apiRequest!!.longitude = EasyPaisaApp.getUserLatLng()?.longitude.toString()
                    apiRequest!!.imei = EasyPaisaApp.getDeviceId()
                    presenter.doAepsTransaction()
                }
                /*getString(R.string.ser_mini_statement) -> {
                    apiRequest = PaisaNikalRequest()
                    apiRequest?.amount = "0"

                    var serviceCode = ""
                    doRetriveModel().getAepsTransactionRequest().aeps = "yes"
                    doRetriveModel().getAepsTransactionRequest().token =
                        EasyPaisaApp.getLoggedInUser()?.apptoken!!
                    doRetriveModel().getAepsTransactionRequest().userId =
                        EasyPaisaApp.getLoggedInUser()?.id.toString()
                    doRetriveModel().getAepsTransactionRequest().transactionType = "MS"
                    doRetriveModel().getAepsTransactionRequest().mobileNumber =
                        textInputLayoutAepsMobNo.editText!!.text.toString().trim()
                    serviceCode = PaisaNikalConfig.ApiServices.ICICI_AEPS_MINI_STATEMENT.toString()
                    apiRequest!!.mobileNumber = textInputLayoutAepsMobNo.editText!!.text.toString()
                    apiRequest!!.serviceCode = serviceCode
                    apiRequest!!.latitude = "25.0961"
                    apiRequest!!.longitude = "85.3131"
                    apiRequest!!.imei = EasyPaisaApp.getDeviceId()
                    presenter.doAepsTransaction()
                }*/
            }
        }
    }

    private val isValide: Boolean
        get() {
            if (textInputLayoutAepsMobNo.editText!!.text.trim().isEmpty()) {
                textInputLayoutAepsMobNo.error = getString(R.string.enter_mobile)
                return false
            } else {
                textInputLayoutAepsMobNo.isErrorEnabled = false
            }
            if (textInputLayoutAepsMobNo.editText!!.text.length != 10) {
                textInputLayoutAepsMobNo.error = getString(R.string.invalid_monile_number)
                return false
            } else {
                textInputLayoutAepsMobNo.isErrorEnabled = false
            }

            if (aepsServiceFor == Constants.AvailableService.SERVICE_EASYPAY_AEPS) {
                if (textInputLayoutAepsUserName.visibility == View.VISIBLE
                    && textInputLayoutAepsUserName.editText!!.text.toString().trim().isEmpty()
                ) {
                    textInputLayoutAepsUserName.error = "Please enter your name"
                    return false
                } else {
                    textInputLayoutAepsUserName.isErrorEnabled = false
                }
            }

            if (aepsServiceFor == Constants.AvailableService.SERVICE_ICICI_AEPS
                || aepsServiceFor == Constants.AvailableService.SERVICE_EASYPAY_AEPS) {
                if (aepsServiceFor == Constants.AvailableService.SERVICE_EASYPAY_AEPS &&
                    transactionFor == getString(R.string.ser_aadhar_pay) && selectedAadharBank == null) {
                    showToast("Please select bank")
                    return false
                } else if (aepsServiceFor == Constants.AvailableService.SERVICE_EASYPAY_AEPS
                    && transactionFor != getString(R.string.ser_aadhar_pay)
                    && selectedYesBank == null
                ) {
                    showToast("Please select bank")
                    return false
                }
            }

            if ((aepsServiceFor == Constants.AvailableService.SERVICE_ICICI_AEPS
                        || aepsServiceFor == Constants.AvailableService.SERVICE_EASYPAY_AEPS)
                && textInputLayoutAepsAashaarNumber.editText!!.text.toString().isEmpty()
            ) {
                textInputLayoutAepsAashaarNumber.error = "Enter Aadhaar Number"
                return false
            } else {
                textInputLayoutAepsAashaarNumber.isErrorEnabled = false
            }

            if ((aepsServiceFor == Constants.AvailableService.SERVICE_ICICI_AEPS
                        || aepsServiceFor == Constants.AvailableService.SERVICE_EASYPAY_AEPS)
                && textInputLayoutAepsAashaarNumber.editText!!.text.toString().length != 12
            ) {
                textInputLayoutAepsAashaarNumber.error = "Invalid Aadhaar Number"
                return false
            } else {
                textInputLayoutAepsAashaarNumber.isErrorEnabled = false
            }

            if (transactionFor == getString(R.string.ser_withdraw)) {
                val amountText = textInputLayoutEnterAmount.editText!!.text.toString()
                if (amountText.trim().isEmpty()) {
                    textInputLayoutEnterAmount.error = getString(R.string.enter_amount)
                    return false
                } else {
                    textInputLayoutEnterAmount.isErrorEnabled = false
                }


                if (amountText.toDouble() !in 100.0..10000.0) {
                    textInputLayoutEnterAmount.error = getString(R.string.enter_min_amount_)
                    return false
                } else {
                    textInputLayoutEnterAmount.isErrorEnabled = false
                }

            }
            if (!chkTermsAnsCon.isChecked) {
                showToast("Please agree terms and conditions before proceeding")
                return false
            }

            textInputLayoutAepsMobNo.isErrorEnabled = false
            textInputLayoutEnterAmount.isErrorEnabled = false
            return true
        }

    private fun updateAepsTnx() {
        if (transactionFor == getString(R.string.ser_withdraw)
            || transactionFor == getString(R.string.ser_aadhar_pay)
            || transactionFor == getString(R.string.ser_deposit)
        ) {
            textInputLayoutEnterAmount.visibility = View.VISIBLE
            linearAmountShortCut.visibility = View.VISIBLE
        } else if (transactionFor == getString(R.string.ser_balance) || transactionFor == getString(
                R.string.ser_mini_statement
            )
        ) {
            textInputLayoutEnterAmount.visibility = View.GONE
            linearAmountShortCut.visibility = View.GONE
        }
    }

    private fun updateServiceType() {
        when (aepsServiceFor) {
            Constants.AvailableService.SERVICE_ICICI_AEPS, Constants.AvailableService.SERVICE_EASYPAY_AEPS -> {
                tvToolbarTitle.text = getString(R.string.aeps_trasactions)
                when (transactionFor) {
                    getString(R.string.ser_aadhar_pay) -> {
                        tvToolbarTitle.text = getString(R.string.aadhaar_pay)
                    }
                    getString(R.string.ser_deposit) -> {
                        tvToolbarTitle.text = getString(R.string.cash_deposit)
                    }
                    getString(R.string.ser_mini_statement) -> {
                        tvToolbarTitle.text = getString(R.string.mini_statment)
                    }
                }
            }

            /*Constants.AvailableService.SERVICE_EASYPAY_AEPS -> {
                tvToolbarTitle.text = getString(R.string.aeps_trasactions)
                edtSelectBank.visibility = View.GONE
                textInputLayoutAepsAashaarNumber.visibility = View.GONE
            }*/
            Constants.AvailableService.SERVICE_AEPS_ICICI_EP -> {
                tvToolbarTitle.text = getString(R.string.aeps_trasactions)
                edtSelectBank.visibility = View.GONE
                textInputLayoutAepsAashaarNumber.visibility = View.GONE
            }
            Constants.AvailableService.SERVICE_FINO_AEPS -> {
                tvToolbarTitle.text = getString(R.string.aeps_trasactions)
                edtSelectBank.visibility = View.GONE
                textInputLayoutAepsAashaarNumber.visibility = View.GONE
            }
            Constants.AvailableService.SERVICE_MICRO_ATM -> {
                tvToolbarTitle.text = getString(R.string.micro_atm)
                edtSelectBank.visibility = View.GONE
                textInputLayoutAepsAashaarNumber.visibility = View.GONE
            }
        }
    }

    private fun initView() {
        googleFusedLocation = GoogleFuesedLocationService(getViewActivity(), object :
            OnUserLocationListener {
            override fun onUserLocationSuccess(location: Location) {
                if (EasyPaisaApp.getUserLatLng() == null) {
                    showToast("User location not found, please enable GPS")
                    return
                }
                doTransactionProcess()
            }

            override fun onUserLocationFail(failMessage: String) {
            }

        }, true)
        tvToolbarTitle.text = getString(R.string.aeps)
        aepsServiceFor = intent.getIntExtra("selectedService", 1)
        if (intent.hasExtra("transactionFor")) {
            transactionFor = intent.getStringExtra("transactionFor") ?: "ser_withdraw"
            tablayoutAeps.visibility = View.GONE
        }
        updateServiceType()
        updateAepsTnx()

        config = PaisaNikalConfiguration()
        config?.agentId = CONFIG_AGENT_ID_CODE
        config?.agentName = CONFIG_AGENT_NMAE
        config?.environment = CONFIG_ENVIRONMENT

        ivHomeBack.setOnClickListener {
            onBackPressed()
        }

        edtSelectBank.setOnClickListener {
            when {
                transactionFor == getString(R.string.ser_aadhar_pay) -> {
                    startActivityForResult(
                        Intent(
                            getViewActivity(),
                            SelectBankActivty::class.java
                        ).putExtra("selectedAepsTransaction", transactionFor),
                        Constants.UI.SELECT_AADHAR_BANK
                    )
                }
                aepsServiceFor == Constants.AvailableService.SERVICE_EASYPAY_AEPS -> {
                    startActivityForResult(
                        Intent(
                            getViewActivity(),
                            SelectBankActivty::class.java
                        ).putExtra("selectedAepsTransaction", "yesaeps"), Constants.UI.SELECT_BANK_YES
                    )
                }
                else -> {
                    startActivityForResult(
                        Intent(
                            getViewActivity(),
                            SelectBankActivty::class.java
                        ).putExtra("selectedAepsTransaction", transactionFor), Constants.UI.SELECT_BANK
                    )
                }
            }

        }

        textInputLayoutAepsMobNo.editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (aepsServiceFor == Constants.AvailableService.SERVICE_EASYPAY_AEPS) {
                    if (char!!.isEmpty()) {
                        return
                    }
                    if (char.toString().trim().length == 10) {
                        presenter.doValidateCustomer(char.toString().trim())
                    } else {
                        textInputLayoutAepsUserName.visibility = View.GONE
                        textInputLayoutAepsUserName.editText!!.setText("")
                    }
                }
            }
        })
    }

    override fun getViewActivity(): Activity {
        return this@AepsActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    override fun doRetriveModel(): AepsActivityModel {
        return model
    }

    override fun onAepTxnData() {
        val response = doRetriveModel().getLoginDomain().aepsTxnDataResponse
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            try {
                apiRequest?.checkSum = Checksum.getCheckSum(response.agentcode)
                config?.agentId = response.agentcode
//                apiRequest?.checkSum = Checksum.getCheckSum(CONFIG_AGENT_ID_CODE)
            } catch (e: DecoderException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: InvalidKeyException) {
                e.printStackTrace()
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            apiRequest!!.orderId = response.txnid
            apiRequest!!.timeStemp = Checksum.currentTime.toString()
            apiRequest!!.token = Checksum.randomNumber

            /*if (transactionFor == getString(R.string.ser_mini_statement)){
                val intent = Intent(this, IntermidiateActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable(PaisaNikalConfig.Config.PAISA_NIKAL_CONFIG, config)
                bundle.putParcelable(PaisaNikalConfig.Config.PAISA_NIKAL_REQUEST, apiRequest)
                intent.putExtras(bundle)
                startActivityForResult(intent, CODE_MINI_STATEMENT)
                return
            }*/

            val intent = Intent(this, IntermidiateActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(PaisaNikalConfig.Config.PAISA_NIKAL_CONFIG, config)
            bundle.putParcelable(PaisaNikalConfig.Config.PAISA_NIKAL_REQUEST, apiRequest)
            intent.putExtras(bundle)

            if (aepsServiceFor == Constants.AvailableService.SERVICE_FINO_AEPS
                || aepsServiceFor == Constants.AvailableService.SERVICE_MICRO_ATM
            ) {
                startActivityForResult(intent, CODE_MICRO_TRANSACTION)
            } else if (transactionFor == getString(R.string.ser_mini_statement)) {
                startActivityForResult(intent, CODE_MINI_STATEMENT)
            } else {
                startActivityForResult(intent, CODE_AEPS_TRANSACTION)
            }

        } else {
            Utils.showAlert(
                getViewActivity(),
                if (response.message.isNotEmpty()) response.message else getString(R.string.some_thing_wants_wong),
                "",
                View.OnClickListener { })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3030 && resultCode == Activity.RESULT_OK) {
            googleFusedLocation.getUserLocation()
        }
        if (requestCode == Constants.UI.SELECT_BANK && resultCode == Activity.RESULT_OK) {
            selectedBank =
                data?.getSerializableExtra("selectedBank") as UserRequiredDataResponse.Aepsbank?
            selectedBank?.let {
                edtSelectBank.setText(it.bankName)
            }
        }
        if (requestCode == Constants.UI.SELECT_AADHAR_BANK && resultCode == Activity.RESULT_OK) {
            selectedAadharBank =
                data?.getSerializableExtra("selectedBank") as UserRequiredDataResponse.Aadharbank?
            selectedAadharBank?.let {
                edtSelectBank.setText(it.bankName)
            }
        }

        if (requestCode == Constants.UI.SELECT_BANK_YES && resultCode == Activity.RESULT_OK) {
            selectedYesBank =
                data?.getSerializableExtra("selectedBank") as UserRequiredDataResponse.Yesbank?
            selectedYesBank?.let {
                edtSelectBank.setText(it.bankName)
            }
        }

        if (requestCode == CODE_AEPS_TRANSACTION && resultCode == Activity.RESULT_OK) {
            if (data != null && data.extras != null) {
                val bundle = data.extras
                if (bundle != null) {
                    val apiResponse =
                        bundle.getParcelable<AepsTransactionResponse>(PaisaNikalConfig.UI.AEPS_RESPONSE)
                    Log.e("TAG", "Response: " + apiResponse!!)
                    if (apiResponse != null && apiResponse.rESPCODE == 300) {
                        //Success handler
                        //Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    } else {
                        //Fail handler
                        //Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else if (requestCode == CODE_AEPS_TRANSACTION && resultCode == Activity.RESULT_CANCELED) {
            //handler for user canceled
            Toast.makeText(this, "Request has been canceled by user", Toast.LENGTH_SHORT).show()
        }

        if (requestCode == CODE_MICRO_TRANSACTION && resultCode == Activity.RESULT_OK) {
            if (data != null && data.extras != null) {
                val bundle = data.extras
                if (bundle != null) {
                    val apiResponse =
                        bundle.getParcelable<PaisaNikalTransactionResponse>(PaisaNikalConfig.UI.FINO_TRANSACTION_RESPONSE)
                    if (apiResponse != null && apiResponse.rESPCODE == 200) {
                        //Success handler
                        Log.e("TAG", "Response: $apiResponse")
                        //Toast.makeText(this, "Micro Success", Toast.LENGTH_SHORT).show()
                    } else if (apiResponse != null && apiResponse.rESPCODE != 200) {
                        Utils.showAlert(
                            getViewActivity(),
                            apiResponse.rESPMSG ?: "Some error occurred",
                            "Error",
                            View.OnClickListener {

                            })
                    } else {
                        if (bundle.get(PaisaNikalConfig.UI.AEPS_RESPONSE) != null) {
                            val response =
                                bundle.getParcelable<AepsTransactionResponse>(PaisaNikalConfig.UI.AEPS_RESPONSE)
                            Utils.showAlert(
                                getViewActivity(),
                                response?.rESPMSG ?: "Some error occurred",
                                "Error",
                                View.OnClickListener {

                                })
                            return
                        }

                    }
                }
            }
        } else if (requestCode == CODE_MICRO_TRANSACTION && resultCode == Activity.RESULT_CANCELED) {
            //handler for user canceled
            Toast.makeText(this, "Request has been canceled by user", Toast.LENGTH_SHORT).show()
        }

        if (requestCode == CODE_MINI_STATEMENT && resultCode == Activity.RESULT_OK) {
            if (data != null && data.extras != null) {
                val bundle = data.extras
                if (bundle != null) {
                    val apiResponse: BankMiniStatementResponse? =
                        bundle.getParcelable(PaisaNikalConfig.UI.AEPS_MINI_STATEMENT_RESPONSE)
                    Log.e("TAG", "Bank_MiniStatement_Response: $apiResponse")
                    if (apiResponse != null && apiResponse.rESPCODE == "300") {
                        //Success handler
                        Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                    } else {
                        //Fail handler
                        Utils.showAlert(
                            getViewActivity(),
                            apiResponse?.rESPMSG!!,
                            "Easy Paisa",
                            View.OnClickListener {

                            })
                    }
                }
            }
        } else if (requestCode == CODE_MINI_STATEMENT && resultCode == Activity.RESULT_CANCELED) {
            //AEPS_MINI_STATEMENT_RESPONSE
            if (data != null && data.extras != null) {
                val bundle = data.extras
                val apiResponse: BankMiniStatementResponse? =
                    bundle?.getParcelable(PaisaNikalConfig.UI.AEPS_MINI_STATEMENT_RESPONSE)
                if (apiResponse != null) {
                    Utils.showAlert(
                        getViewActivity(),
                        apiResponse.rESPMSG ?: getString(R.string.some_thing_wants_wong),
                        "Easy Paisa",
                        View.OnClickListener {
                        })
                }
            } else {
                Toast.makeText(this, getString(R.string.some_thing_wants_wong), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onUserValidate() {
        val res = doRetriveModel().getLoginDomain().yesValidateUserResponse
        isCustFound = res.status
        when (res.status) {
            Constants.ApiResponse.RES_CNF -> {
                textInputLayoutAepsUserName.visibility = View.VISIBLE
                textInputLayoutAepsUserName.editText!!.isEnabled = true
            }
            Constants.ApiResponse.RES_SUCCESS -> {
                textInputLayoutAepsUserName.visibility = View.VISIBLE
                textInputLayoutAepsUserName.editText!!.isEnabled = false
                textInputLayoutAepsUserName.editText!!.setText(res.data.name)
            }
            else -> {
                textInputLayoutAepsUserName.visibility = View.VISIBLE
                textInputLayoutAepsUserName.editText!!.isEnabled = true
            }
        }
    }

}
