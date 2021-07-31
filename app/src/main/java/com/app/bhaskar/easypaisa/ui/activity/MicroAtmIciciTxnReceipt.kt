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
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.morpho.android.usb.USBManager
import com.pa.baseframework.baseview.BaseActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_ae_pstransaction_receipt.*
import kotlinx.android.synthetic.main.activity_micro_atm_icici_txn_receipt.*
import kotlinx.android.synthetic.main.activity_micro_atm_icici_txn_receipt.constraintLayoutDetails
import kotlinx.android.synthetic.main.activity_micro_atm_icici_txn_receipt.ivShareReceipt
import kotlinx.android.synthetic.main.activity_micro_atm_icici_txn_receipt.ivTxnStatus
import kotlinx.android.synthetic.main.activity_micro_atm_icici_txn_receipt.tvLabelTransactionDetail
import kotlinx.android.synthetic.main.activity_micro_atm_icici_txn_receipt.tvTxnAmount
import kotlinx.android.synthetic.main.activity_micro_atm_icici_txn_receipt.tvTxnBankName
import kotlinx.android.synthetic.main.activity_micro_atm_icici_txn_receipt.tvTxnOrderId
import kotlinx.android.synthetic.main.activity_micro_atm_icici_txn_receipt.tvTxnRRN
import kotlinx.android.synthetic.main.activity_micro_atm_icici_txn_receipt.tvTxnStatus
import java.io.File
import java.io.FileOutputStream

class MicroAtmIciciTxnReceipt : BaseActivity() {
    companion object {
        const val TAG = "MicroAtmIciciTxnReceipt"
    }
    private var type:Int = 0
    private var status:Boolean = false
    private var response:String = ""
    private var transType:String = ""
    private var transAmount:Double = 0.0
    private var balAmount:Double = 0.0
    private var bankRrn:String = ""
    private var cardNum:String = ""
    private var bankName:String = ""
    private var cardType:String = ""
    private var txnId:String = ""
    private var firstTimePermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_micro_atm_icici_txn_receipt)
        initView()
    }

    private fun initView() {
        if (intent.extras!=null){
            type  =  intent.getIntExtra("type",0)
            status  =  intent.getBooleanExtra("status",false)
            response  =  intent.getStringExtra("response") ?: ""
            transType  =  intent.getStringExtra("transType") ?: ""
            transAmount  =  intent.getDoubleExtra("transAmount",0.0)
            balAmount  =  intent.getDoubleExtra("balAmount",0.0)
            bankRrn  =  intent.getStringExtra("bankRrn") ?: ""
            cardNum  =  intent.getStringExtra("cardNum") ?: ""
            bankName  =  intent.getStringExtra("bankName") ?: ""
            cardType  =  intent.getStringExtra("cardType") ?: ""
            txnId  =  intent.getStringExtra("txnId") ?: ""
        }
        fillDetails()

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
                            AePSTransactionReceiptActivity.MY_PERMISSIONS_REQUEST_STORAGE
                        )
                    })
            } else {
                ActivityCompat.requestPermissions(
                    getViewActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    AePSTransactionReceiptActivity.MY_PERMISSIONS_REQUEST_STORAGE
                )
            }
        }

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
                                AePSTransactionReceiptActivity.MY_PERMISSIONS_REQUEST_STORAGE
                            )
                        })
                } else {
                    ActivityCompat.requestPermissions(
                        getViewActivity(),
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        AePSTransactionReceiptActivity.MY_PERMISSIONS_REQUEST_STORAGE
                    )
                }
            } else {
                openSharableIntnet()
            }

        }
    }

    private fun fillDetails() {
        if (type  == Constants.MicroAtm.MICRO_WITHDRWAL){
            tvLabelTransactionDetail.text = "Micro ATM Transaction for Cash Withdrawal"
        }else if(type  == Constants.MicroAtm.MICRO_DEPOSIT){
            tvLabelTransactionDetail.text = "Micro ATM Transaction for Cash Deposit"
        }else if(type  == Constants.MicroAtm.MICRO_BALANCE){
            tvLabelTransactionDetail.text = "Micro ATM Balance Info"
        }

        if (status) {
            ivTxnStatus.setImageResource(R.drawable.ic_success_tnx)
            tvTxnStatus.text = response
            tvTxnStatus.setTextColor(
                ContextCompat.getColor(
                    getViewActivity(),
                    R.color.colorGreen
                )
            )
        }else{
            ivTxnStatus.setImageResource(R.drawable.ic_failed_txn)
            tvTxnStatus.text = response
            tvTxnStatus.setTextColor(
                ContextCompat.getColor(
                    getViewActivity(),
                    R.color.colorRed
                )
            )
        }

        if (type  == Constants.MicroAtm.MICRO_BALANCE){
            tvTxnAmount.text  = Utils.formatAmount(balAmount)
        }else{
            tvTxnAmount.text  = Utils.formatAmount(transAmount)
        }
        tvTxnBalance.text  = Utils.formatAmount(balAmount)
        tvTxnOrderId.text  = if (txnId.isEmpty()) "NA" else txnId
        tvTxnRRN.text  = if(txnId.isEmpty()) "NA" else txnId
        tvTxnBankName.text  = bankName
        tvTxnCardNo.text  = cardNum
        tvTxnCardType.text  = cardType
        tvTxnAgentName.text  = EasyPaisaApp.getLoggedInUser()?.name
        tvTxnBaneDate.text  = Utils.getDate(System.currentTimeMillis(),"dd MMM yyyy HH:mm a")
    }

    override fun getViewActivity(): Activity {
        return this@MicroAtmIciciTxnReceipt
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
        val folder = Utils.getExternalFilesAccessDir(this@MicroAtmIciciTxnReceipt, Constants.MediaType.IMAGE)
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
                    hideProgress()
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
        if (requestCode == AePSTransactionReceiptActivity.MY_PERMISSIONS_REQUEST_STORAGE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && !firstTimePermission) {
            //Permission granted
            openSharableIntnet()
        } else if (requestCode == AePSTransactionReceiptActivity.MY_PERMISSIONS_REQUEST_STORAGE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
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