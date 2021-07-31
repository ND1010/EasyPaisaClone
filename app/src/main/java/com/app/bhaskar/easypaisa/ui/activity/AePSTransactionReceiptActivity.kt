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
import androidx.appcompat.app.AppCompatActivity
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
import com.app.bhaskar.easypaisa.mvp.model.AePSTransactionReceiptModel
import com.app.bhaskar.easypaisa.mvp.presenter.AePSTransactionReceiptPresenterImpl
import com.app.bhaskar.easypaisa.mvp.presenter.AepsTransactionReceiptPresenter
import com.app.bhaskar.easypaisa.request_model.AepsRequest
import com.app.bhaskar.easypaisa.response_model.FingPayAepsTxnResponse
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.google.gson.reflect.TypeToken
import com.morpho.android.usb.USBManager
import com.pa.baseframework.baseview.BaseActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_ae_pstransaction_receipt.*
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject

class AePSTransactionReceiptActivity : BaseActivity(),
    AepsTransactionReceiptPresenter.AepsTransactionReceiptView {

    companion object {
        const val TAG = "AePSTransactionReceiptActivity"
        const val MY_PERMISSIONS_REQUEST_STORAGE = 1010
    }

    private var firstTimePermission = false
    private var aepsRequest: AepsRequest? = null
    private var aepsTxnResponse: FingPayAepsTxnResponse? = null

    @Inject
    lateinit var presenter: AePSTransactionReceiptPresenterImpl
    lateinit var model: AePSTransactionReceiptModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ae_pstransaction_receipt)
        model = AePSTransactionReceiptModel(getViewActivity())
        presenter = AePSTransactionReceiptPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        initView()

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

    private fun initView() {
        if (intent.hasExtra(Constants.UI.AEPS_REQUEST)) {
            val type = object : TypeToken<AepsRequest>() {}.type
            val jsonData = intent.getStringExtra(Constants.UI.AEPS_REQUEST)
            aepsRequest = EasyPaisaApp.getGson().fromJson(jsonData, type)
        } else {
            showToast(getString(R.string.some_thing_wants_wong))
            finish()
        }

        if (intent.hasExtra(Constants.UI.AEPS_RESPONSE)) {
            val type = object : TypeToken<FingPayAepsTxnResponse>() {}.type
            val jsonData = intent.getStringExtra(Constants.UI.AEPS_RESPONSE)
            aepsTxnResponse = EasyPaisaApp.getGson().fromJson(jsonData, type)
            fillAePSTxnDetails()
        } else {
            showToast(getString(R.string.some_thing_wants_wong))
            finish()
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
    }

    @SuppressLint("SetTextI18n")
    private fun fillAePSTxnDetails() {
        when (aepsTxnResponse?.status) {
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

        if (aepsRequest?.aepsServiceFor == Constants.AvailableService.SERVICE_ICICI_AEPS) {
            ivAepsSerType.setImageResource(R.drawable.ic_aeps_icici)
        } else {
            ivAepsSerType.setImageResource(R.drawable.ic_aeps_ybl)
        }
        when (aepsRequest?.transactionFor) {
            getString(R.string.ser_withdraw) -> {
                tvLabelTransactionDetail.text = "AePS Cash Withdrawal"
            }
            getString(R.string.ser_mini_statement) -> {
                tvLabelTransactionDetail.text = "AePS Mini Statement"
            }
            getString(R.string.ser_aadhar_pay) -> {
                tvLabelTransactionDetail.text = "AePS Aadhaar Pay"
            }
            getString(R.string.ser_balance) -> {
                tvLabelTransactionDetail.text = "AePS Balance Inquiry"
            }
            getString(R.string.ser_deposit) -> {
                tvLabelTransactionDetail.text = "AePS Cash Deposit"
            }
            else -> tvLabelTransactionDetail.text = "Transaction Details"

        }

        if (aepsRequest?.transactionFor == getString(R.string.ser_balance)) {
            if (!aepsTxnResponse?.amount.isNullOrEmpty()) {
                tvTxnAmount.text = Utils.formatAmount(
                    String.format(
                        Locale.ENGLISH,
                        "%.2f",
                        aepsTxnResponse?.balance?.toDouble()
                    ).toDouble()
                )
            } else {
                tvTxnAmount.text = "NA"
            }
        } else {
            if (!aepsTxnResponse?.amount.isNullOrEmpty()) {
                tvTxnAmount.text = Utils.formatAmount(aepsTxnResponse?.amount!!.toDouble())
            } else {
                tvTxnAmount.text = "NA"
            }
        }

        if (!aepsTxnResponse?.message.isNullOrEmpty()) {
            tvTxnMessage.text = aepsTxnResponse?.message
        } else {
            tvTxnMessage.text = "NA"
        }

        if (!aepsTxnResponse?.txnid.isNullOrEmpty()) {
            tvTxnOrderId.text = aepsTxnResponse?.txnid
        } else {
            tvTxnOrderId.text = "NA"
        }

        if (!aepsTxnResponse?.balance.isNullOrEmpty()) {
            tvTxnAvailableBal.text = Utils.formatAmount(
                String.format(
                    Locale.ENGLISH,
                    "%.2f",
                    aepsTxnResponse?.balance?.toDouble()
                ).toDouble()
            )
        } else {
            tvTxnAvailableBal.text = "NA"
        }
        if (!aepsTxnResponse?.rrn.isNullOrEmpty()) {
            tvTxnRRN.text = aepsTxnResponse?.rrn
        } else {
            tvTxnRRN.text = "NA"
        }

        if (aepsRequest != null) {
            tvTxnAadhaarNo.text = "XXXXXXXX${aepsRequest?.aadharNo?.substring(8)}"
        } else {
            tvTxnAadhaarNo.text = "NA"
        }

        if (aepsRequest != null) {
            tvTxnBankName.text = aepsRequest?.bank?.bankName
        } else {
            tvTxnBankName.text = "NA"
        }
    }

    override fun getViewActivity(): Activity {
        return this@AePSTransactionReceiptActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    override fun doRetriveModel(): AePSTransactionReceiptModel {
        return model
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
        val folder = Utils.getExternalFilesAccessDir(this@AePSTransactionReceiptActivity, Constants.MediaType.IMAGE)
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
                .subscribe { mFile->
                    hideProgress()
                    if (mFile!!.exists()) {
                        shareImage(mFile)
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
