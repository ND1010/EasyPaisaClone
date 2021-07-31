package com.app.bhaskar.easypaisa.ui.activity

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.DmtTransactionActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.DmtTransactionActivityPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.DmtTransactionActivityPresenterImpl
import com.app.bhaskar.easypaisa.request_model.DmtTransactionResponse
import com.app.bhaskar.easypaisa.response_model.SearchRemitterResponse
import com.app.bhaskar.easypaisa.ui.listener.OnUserLocationListener
import com.app.bhaskar.easypaisa.ui.service.GoogleFuesedLocationService
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.google.gson.reflect.TypeToken
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_dmt_transaction.*
import kotlinx.android.synthetic.main.toolbar_simple.*
import javax.inject.Inject

class DmtTransactionActivity : BaseActivity(), DmtTransactionActivityPresenter.DmtTransactionView {

    companion object {
        const val TAG = "DmtTransactionActivity"
        const val SELECT_BENE = 1010
    }

    private var ipAddress: String = ""
    private lateinit var remitterResponse: SearchRemitterResponse
    private lateinit var model: DmtTransactionActivityModel
    private var availbleLimit = "0"
    private var rotationAngle = 0
    private var selectedBene: SearchRemitterResponse.Userdata.Benelist? = null
    private lateinit var googleFusedLocation: GoogleFuesedLocationService

    @Inject
    lateinit var presenter: DmtTransactionActivityPresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dmt_transaction)
        ipAddress = Utils.getIPAddress(true)
        initViews()
        Log.e(TAG, "onCreate")
    }

    private fun initViews() {

        ivHomeBack.setOnClickListener { onBackPressed() }
        tvToolbarTitle.text = "DMT Transaction"
        model = DmtTransactionActivityModel(getViewActivity())
        presenter = DmtTransactionActivityPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        val type = object : TypeToken<SearchRemitterResponse>() {}.type
        remitterResponse = EasyPaisaApp.getGson()
            .fromJson(intent.getStringExtra(Constants.UI.REMITTER_RESPONSE), type)

        remitterResponse.let {
            tvUserName.text = remitterResponse.userdata.name
            tvUserMobileNumber.text = remitterResponse.userdata.mobile
            tvTotalLimit.text = remitterResponse.userdata.totallimit
            tvAvailableLimit.text = remitterResponse.userdata.availlimit
            availbleLimit = remitterResponse.userdata.availlimit
        }
        googleFusedLocation = GoogleFuesedLocationService(getViewActivity(), object :
            OnUserLocationListener {
            override fun onUserLocationSuccess(location: Location) {
                if (EasyPaisaApp.getUserLatLng() == null) {
                    showToast("User location not found, please enable GPS")
                    return
                }
                doTransfer()
            }

            override fun onUserLocationFail(failMessage: String) {
            }

        }, true)

        btn_submit.setOnClickListener {
            if (selectedBene == null) {
                showToast("Please select beneficiary.")
            } else if (edtAmount.text.toString().trim().isEmpty()) {
                edtAmount.error = "Enter Transfer Amount"
                edtAmount.requestFocus()
            } else if (edtAmount.text.toString().trim().isNotEmpty() &&
                edtAmount.text.toString().trim().toDouble() <= 0
            ) {
                edtAmount.error = "Invalid Transfer Amount"
                edtAmount.requestFocus()
            } else if (edtAmount.text.toString().trim().isNotEmpty() &&
                edtAmount.text.toString().trim().toDouble() < 100
            ) {
                edtAmount.error = "Amount should be at least(>=) 100"
                edtAmount.requestFocus()
            } else if ((availbleLimit.isNotEmpty() && availbleLimit.toDouble() > 0) && edtAmount.text.toString()
                    .trim().isNotEmpty()
                && edtAmount.text.toString().trim().toDouble() > availbleLimit.toDouble()
            ) {
                edtAmount.error = "Amount should between 100 to $availbleLimit"
                edtAmount.requestFocus()
            } else {
                if (EasyPaisaApp.getUserLatLng() == null) {
                    googleFusedLocation.needToShowDialog = true
                    googleFusedLocation.accessUserCurrentLocation()
                    return@setOnClickListener
                }
                doTransfer()
            }
        }

        linearHeader.setOnClickListener {
            rotationAngle = if (rotationAngle == 0) 180 else 0
            ivArrow.animate().rotation(rotationAngle.toFloat()).setDuration(500).start()
            if (constraint.visibility == View.GONE) {
                constraint.visibility = View.VISIBLE
            } else {
                constraint.visibility = View.GONE
            }
        }

        cardviewSelectBene.setOnClickListener {
            val intent = Intent(getViewActivity(), BeneficiaryActivity::class.java)
            val type = object : TypeToken<SearchRemitterResponse>() {}.type
            intent.putExtra(
                Constants.UI.REMITTER_RESPONSE,
                EasyPaisaApp.getGson().toJson(remitterResponse, type)
            )
            startActivityForResult(intent, SELECT_BENE)
        }
    }

    private fun doTransfer() {
        Utils.showAlert(
            getViewActivity(),
            "Are you sure you wants to transfer ${
                Utils.formatAmount(
                    edtAmount.text.toString().trim().toDouble()
                )
            } to ${selectedBene!!.benename}?",
            "",
            View.OnClickListener {
                doRetriveModel().getDmtTransactionRequest().benename =
                    selectedBene!!.benename
                doRetriveModel().getDmtTransactionRequest().benebank =
                    selectedBene!!.benebank
                doRetriveModel().getDmtTransactionRequest().beneaccount =
                    selectedBene!!.beneaccount
                doRetriveModel().getDmtTransactionRequest().beneifsc =
                    selectedBene!!.beneifsc
                doRetriveModel().getDmtTransactionRequest().ip = ipAddress
                doRetriveModel().getDmtTransactionRequest().latitude =
                    EasyPaisaApp.getUserLatLng()?.latitude.toString()
                doRetriveModel().getDmtTransactionRequest().longitude =
                    EasyPaisaApp.getUserLatLng()?.longitude.toString()
                doRetriveModel().getDmtTransactionRequest().token =
                    EasyPaisaApp.getLoggedInUser()?.apptoken!!
                doRetriveModel().getDmtTransactionRequest().userId =
                    EasyPaisaApp.getLoggedInUser()?.id.toString()
                doRetriveModel().getDmtTransactionRequest().type = "transfer"
                doRetriveModel().getDmtTransactionRequest().name =
                    remitterResponse.userdata.name
                doRetriveModel().getDmtTransactionRequest().amount =
                    edtAmount.text.toString().trim()
                doRetriveModel().getDmtTransactionRequest().mobile =
                    remitterResponse.userdata.mobile

                presenter.doTransferAmount()
            },
            View.OnClickListener {

            })
    }

    override fun onDmtTransactionDone() {
        val response = doRetriveModel().getDomain().dmtTransactionResponse
        when (response.status) {
            Constants.ApiResponse.RES_SUCCESS -> {
                Utils.showSuccessAlert(getViewActivity(), response.message)
                Handler(Looper.getMainLooper()).postDelayed({

                    Utils.dismissSuccessAlert()
                    val intent =
                        Intent(getViewActivity(), DmtTransactionReceiptActivity::class.java)
                    val type = object : TypeToken<DmtTransactionResponse>() {}.type
                    intent.putExtra(
                        Constants.UI.DMT_TXN_RESPONSE,
                        EasyPaisaApp.getGson().toJson(response, type)
                    )
                    getViewActivity().startActivity(intent)
                    finish()

                }, 1500)
            }
            Constants.ApiResponse.RES_PENDING -> {
                val intent = Intent(getViewActivity(), DmtTransactionReceiptActivity::class.java)
                val type = object : TypeToken<DmtTransactionResponse>() {}.type
                intent.putExtra(
                    Constants.UI.DMT_TXN_RESPONSE,
                    EasyPaisaApp.getGson().toJson(response, type)
                )
                getViewActivity().startActivity(intent)
                finish()
            }
            else -> {
                Utils.showAlert(
                    getViewActivity(),
                    response.message,
                    "Easy Paisa",
                    View.OnClickListener {
                    })
            }
        }
    }

    override fun doRetriveModel(): DmtTransactionActivityModel {
        return model
    }


    override fun getViewActivity(): Activity {
        return this@DmtTransactionActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SELECT_BENE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val type =
                        object : TypeToken<SearchRemitterResponse.Userdata.Benelist>() {}.type
                    selectedBene = EasyPaisaApp.getGson()
                        .fromJson(data?.getStringExtra(Constants.UI.SELECT_BENE), type)
                    if (selectedBene != null) {
                        updateBeneDetail()
                    }
                }
            }

            3030 -> {
                if (resultCode == Activity.RESULT_OK) {
                    googleFusedLocation.getUserLocation()
                }
            }
        }
    }

    private fun updateBeneDetail() {
        constraintBeneDetails.visibility = View.VISIBLE
        tv_payment_title.text = "Change Beneficiary"
        tvBeneName.text = selectedBene?.benename
        tvBankName.text = selectedBene?.benebank
        tvBankAccNo.text = selectedBene?.beneaccount
        tvBankIfsc.text = selectedBene?.beneifsc
    }
}