package com.app.bhaskar.easypaisa.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.app.bhaskar.easypaisa.BuildConfig
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.response_model.DTHRechargeresponse
import com.app.bhaskar.easypaisa.response_model.ElectricityBillPaymentResponse
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.google.gson.reflect.TypeToken
import com.morpho.android.usb.USBManager
import com.pa.baseframework.baseview.BaseActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_electricity_bill_receipt.*
import java.io.File
import java.io.FileOutputStream

class ElectricityBillPaymentReceiptActivity : BaseActivity() {


    companion object {
        const val TAG = "MobileRechargeReceipt"
        const val MY_PERMISSIONS_REQUEST_STORAGE = 1010
    }

    private var state: String = ""
    private var firstTimePermission = false
    private var billElectricityDetail: ElectricityBillPaymentResponse? = null
    private var billFor = Constants.UI.ELECTRICITY_BILL_RESPONSE
//    private var isElectricityBillResponse = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_electricity_bill_receipt)

        val type = object : TypeToken<ElectricityBillPaymentResponse>() {}.type
        //isElectricityBillResponse = intent.hasExtra(Constants.UI.ELECTRICITY_BILL_RESPONSE)
        billElectricityDetail = EasyPaisaApp.getGson().fromJson<ElectricityBillPaymentResponse>(
                when {
                    intent.hasExtra(Constants.UI.DTH_RESPONSE) -> {
                        billFor = Constants.UI.DTH_RESPONSE
                        tvLabelTransactionDetail.text = "DTH Recharge"
                        tvTxnState.visibility = View.GONE
                        tvLabelTxnRefNo.visibility = View.GONE
                        ivAepsSerType.setImageResource(R.drawable.ic_dth)
                        intent.getStringExtra(Constants.UI.DTH_RESPONSE)
                    }
                    intent.hasExtra(Constants.UI.ELECTRICITY_BILL_RESPONSE) -> {
                        billFor = Constants.UI.ELECTRICITY_BILL_RESPONSE
                        ivAepsSerType.setImageResource(R.drawable.ic_ele)
                        intent.getStringExtra(Constants.UI.ELECTRICITY_BILL_RESPONSE)
                    }
                    intent.hasExtra(Constants.UI.GAS_RESPONSE) -> {
                        tvLabelTransactionDetail.text = "Piped Gas Bill Payment"
                        billFor = Constants.UI.GAS_RESPONSE
                        ivAepsSerType.setImageResource(R.drawable.ic_pipe_gas)
                        intent.getStringExtra(Constants.UI.GAS_RESPONSE)
                    }
                    intent.hasExtra(Constants.UI.LPG_RESPONSE) -> {
                        tvLabelTransactionDetail.text = "LPG Gas Payment"
                        billFor = Constants.UI.LPG_RESPONSE
                        ivAepsSerType.setImageResource(R.drawable.ic_gas)
                        intent.getStringExtra(Constants.UI.LPG_RESPONSE)
                    }
                    intent.hasExtra(Constants.UI.POSTPAID_RESPONSE) -> {
                        tvLabelTransactionDetail.text = "Postpaid Bill Payment"
                        billFor = Constants.UI.POSTPAID_RESPONSE
                        ivAepsSerType.setImageResource(R.drawable.ic_postpaid_bill)
                        intent.getStringExtra(Constants.UI.POSTPAID_RESPONSE)
                    }
                    intent.hasExtra(Constants.UI.WATER_RESPONSE) -> {
                        tvLabelTransactionDetail.text = "Water Bill Payment"
                        billFor = Constants.UI.WATER_RESPONSE
                        ivAepsSerType.setImageResource(R.drawable.ic_water)
                        intent.getStringExtra(Constants.UI.WATER_RESPONSE)
                    }
                    else -> {
                        tvLabelTxnRefNo.visibility = View.GONE
                        tvTxnState.visibility = View.GONE
                        tvLabelTransactionDetail.text = "Insurance Payment"
                        billFor = Constants.UI.INSURANCE
                        ivAepsSerType.setImageResource(R.drawable.ic_insurance)
                        intent.getStringExtra(Constants.UI.INSURANCE)
                    }
                },
                type
        )


        state = intent.getStringExtra("state")?:""
        if (billElectricityDetail == null) {
            finish()
            showToast(getString(R.string.some_thing_wants_wong))
        }
        initViews()
    }

    private fun initViews() {

        fillAePSTxnDetails()
        ivShareReceipt.setOnClickListener {
            firstTimePermission = false
            if (!hasStoragePermission()) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                                getViewActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                ) {
                    Utils.showAlert(
                            getViewActivity(),
                            getString(R.string.provide_permission_storage),
                            "Storage Permission",
                            View.OnClickListener {
                                //Yes
                                ActivityCompat.requestPermissions(
                                        getViewActivity(),
                                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                        MY_PERMISSIONS_REQUEST_STORAGE
                                )
                            })
                } else {
                    ActivityCompat.requestPermissions(
                            getViewActivity(),
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_STORAGE
                    )
                }
            } else {
                openSharableIntnet()
            }
        }
        if (!hasStoragePermission()) {
            firstTimePermission = true
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                            getViewActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
            ) {
                Utils.showAlert(
                        getViewActivity(),
                        getString(R.string.provide_permission_storage),
                        "Storage Permission",
                        View.OnClickListener {
                            //Yes
                            ActivityCompat.requestPermissions(
                                    getViewActivity(),
                                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    MY_PERMISSIONS_REQUEST_STORAGE
                            )
                        })
            } else {
                ActivityCompat.requestPermissions(
                        getViewActivity(),
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_STORAGE
                )
            }
        }
    }

    private fun fillAePSTxnDetails() {
        when (billElectricityDetail?.status) {
            Constants.ApiResponse.RES_SUCCESS -> {
                ivTxnStatus.setImageResource(R.drawable.ic_success_tnx)
                tvTxnStatus.text = getString(R.string.success)
                tvTxnStatus.setTextColor(
                        ContextCompat.getColor(
                                getViewActivity(),
                                R.color.colorGreen
                        )
                )

            }
            Constants.ApiResponse.RES_PENDING -> {
                ivTxnStatus.setImageResource(R.drawable.ic_pending_txn)
                tvTxnStatus.text = getString(R.string.pending)
                tvTxnStatus.setTextColor(
                        ContextCompat.getColor(
                                getViewActivity(),
                                R.color.colorYellow
                        )
                )
            }
            Constants.ApiResponse.RES_ERROR -> {
                ivTxnStatus.setImageResource(R.drawable.ic_failed_txn)
                tvTxnStatus.text = getString(R.string.failed)
                tvTxnStatus.setTextColor(
                        ContextCompat.getColor(
                                getViewActivity(),
                                R.color.colorRed
                        )
                )
            }
            else -> {
                ivTxnStatus.setImageResource(R.drawable.ic_failed_txn)
                tvTxnStatus.text = getString(R.string.failed)
                tvTxnStatus.setTextColor(
                        ContextCompat.getColor(
                                getViewActivity(),
                                R.color.colorRed
                        )
                )
            }
        }

        tvTxnAmount.text = if (billElectricityDetail?.amount!!.isNotEmpty()) {
            Utils.formatAmount(billElectricityDetail?.amount!!.toDouble())
        } else {
            "NA"
        }
        tvTxnMessage.text = billElectricityDetail?.message
        tvTxnOrderId.text =
                if (billElectricityDetail?.txnid!!.isNotEmpty()) billElectricityDetail?.txnid else "NA"
        tvTxnCustNO.text =
                if (billElectricityDetail?.number!!.isNotEmpty()) billElectricityDetail?.number else "NA"
        if (state.isEmpty()){
            tvLabelTxnRefNo.visibility =View.GONE
            tvTxnState.visibility =View.GONE
        }else{
            tvLabelTxnRefNo.visibility =View.VISIBLE
            tvTxnState.visibility =View.VISIBLE
            tvTxnState.text = state
        }
        tvTxnProviderName.text =
                when {
                    billElectricityDetail?.provider!!.isNotEmpty() -> billElectricityDetail?.provider!!
                    billElectricityDetail?.providername!!.isNotEmpty() -> billElectricityDetail?.providername!!
                    else -> "NA"
                }
        tvTxnDate.text =
                if (billElectricityDetail?.date!!.isNotEmpty()) billElectricityDetail?.date!! else "NA"
    }

    override fun getViewActivity(): Activity {
        return this@ElectricityBillPaymentReceiptActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    fun hasStoragePermission(): Boolean {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                getViewActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED)
    }


    fun getRootViev(): View? {
        return constraintLayoutDetails
    }

    fun store(bm: Bitmap):File? {
        val shareImage = "RECEIPT_IMG_${System.currentTimeMillis()}.jpg"
        val folder = Utils.getExternalFilesAccessDir(this@ElectricityBillPaymentReceiptActivity, Constants.MediaType.IMAGE)
        if (!folder.exists())
            folder.mkdirs()
        val mfile = File(folder, shareImage)
        try {
            val fOut = FileOutputStream(mfile)
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return mfile
    }

    @SuppressLint("CheckResult")
    private fun openSharableIntnet() {
        if (getRootViev() != null) {
            showProgress()
            Observable.fromCallable {
                store(takeScreenShot(getRootViev()!!))
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (it!!.exists()) {
                            shareImage(it)
                        }
                    }
        }
    }

    fun takeScreenShot(rootView: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
                rootView.getWidth(),
                rootView.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        rootView.draw(canvas)
        return bitmap
    }


    private fun shareImage(file: File) {
        val uri = FileProvider.getUriForFile(
                getViewActivity(),
                BuildConfig.APPLICATION_ID + ".provider",
                file
        )
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"

        intent.putExtra(Intent.EXTRA_SUBJECT, "")
        intent.putExtra(Intent.EXTRA_TEXT, "")
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        try {
            startActivity(Intent.createChooser(intent, "Share Receipt"))
            hideProgress()
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(USBManager.context, "No App Available", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_STORAGE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && !firstTimePermission) {
            //Permission granted
            openSharableIntnet()
        } else if (requestCode == MY_PERMISSIONS_REQUEST_STORAGE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            //Permission not granted
            Utils.showAlert(
                    getViewActivity(),
                    getString(R.string.provide_permission_storage),
                    "Storage Permission",
                    View.OnClickListener {
                        //Yes
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)

                    })
        }
    }
}
