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
import com.app.bhaskar.easypaisa.request_model.DmtTransactionResponse
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.google.gson.reflect.TypeToken
import com.morpho.android.usb.USBManager
import com.pa.baseframework.baseview.BaseActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_dmt_transaction_receipt.*
import java.io.File
import java.io.FileOutputStream

class DmtTransactionReceiptActivity : BaseActivity() {

    private var dmtResponse: DmtTransactionResponse? = null
    private var firstTimePermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dmt_transaction_receipt)
        initview()
    }

    override fun getViewActivity(): Activity {
        return this@DmtTransactionReceiptActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    private fun initview() {
        val type = object : TypeToken<DmtTransactionResponse>() {}.type
        dmtResponse = EasyPaisaApp.getGson()
            .fromJson(intent.getStringExtra(Constants.UI.DMT_TXN_RESPONSE), type)
        if (dmtResponse == null) {
            showToast(getString(R.string.some_thing_wants_wong))
            finish()
        }

        fillDmtTxnDetails()

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

    private fun fillDmtTxnDetails() {
        when (dmtResponse?.status) {
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

        tvTxnMessage.text = dmtResponse?.message
        if (!dmtResponse?.amount.isNullOrEmpty()) {
            tvTxnAmount.text = Utils.formatAmount(dmtResponse?.amount!!.toDouble())
        }else
        { tvTxnAmount.text = "NA"
        }
        if (!dmtResponse?.id.isNullOrEmpty()){
            tvTxnOrderId.text = dmtResponse?.id
        }else{
            tvTxnOrderId.text = "NA"
        }

        if (!dmtResponse?.name.isNullOrEmpty()){
            tvTxnUsername.text = dmtResponse?.name
        }else{
            tvTxnUsername.text = "NA"
        }

        if (!dmtResponse?.mobile.isNullOrEmpty()){
            tvTxnMobileNo.text = dmtResponse?.mobile
        }else{
            tvTxnMobileNo.text = "NA"
        }

        if (!dmtResponse?.benename.isNullOrEmpty()){
            tvTxnBaneName.text = dmtResponse?.benename
        }else{
            tvTxnBaneName.text = "NA"
        }
        if (!dmtResponse?.beneaccount.isNullOrEmpty()){
            tvTxnBaneAcc.text = dmtResponse?.beneaccount
        }else{
            tvTxnBaneAcc.text = "NA"
        }
        if (!dmtResponse?.benebank.isNullOrEmpty()){
            tvTxnBaneBank.text = dmtResponse?.benebank
        }else{
            tvTxnBaneBank.text = "NA"
        }
        if (!dmtResponse?.beneifsc.isNullOrEmpty()){
            tvTxnBaneIfsc.text = dmtResponse?.beneifsc
        }else{
            tvTxnBaneIfsc.text = "NA"
        }
        if (!dmtResponse?.date.isNullOrEmpty()){
            tvTxnBaneDate.text = dmtResponse?.date
        }else{
            tvTxnBaneDate.text = "NA"
        }

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
        val folder = Utils.getExternalFilesAccessDir(this@DmtTransactionReceiptActivity, Constants.MediaType.IMAGE)
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