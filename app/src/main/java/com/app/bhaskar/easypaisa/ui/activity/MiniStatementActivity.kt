package com.app.bhaskar.easypaisa.ui.activity

import aepsapp.easypay.com.aepsandroid.adapters.MiniStatementAdapter
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
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
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.bhaskar.easypaisa.BuildConfig
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.response_model.FingpayMiniStatementResponse
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.FileUtils
import com.app.bhaskar.easypaisa.utils.Utils
import com.google.gson.reflect.TypeToken
import com.pa.baseframework.baseview.BaseActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_mini_statement.*
import java.io.File
import java.io.FileOutputStream

class MiniStatementActivity : BaseActivity() {

    private var firstTimePermission = false
    private var aadhaarNo: String = ""
    private val TAG = "MiniStatement"
    private val MY_PERMISSIONS_REQUEST_STORAGE = 1010
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var miniBankStatementAdapter: MiniStatementAdapter
    private var arrayListMiniStatement = ArrayList<FingpayMiniStatementResponse.Statement>()
    private var fingpayMiniStatementResponse: FingpayMiniStatementResponse? = null
    private var shareUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mini_statement)
        initView()

    }

    override fun getViewActivity(): Activity {
        return this@MiniStatementActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    private fun initView() {
        if (intent.hasExtra(Constants.UI.AEPS_MINI_RESPONSE)) {
            val type = object : TypeToken<FingpayMiniStatementResponse>() {}.type
            val json = intent.getStringExtra(Constants.UI.AEPS_MINI_RESPONSE)
            fingpayMiniStatementResponse = EasyPaisaApp.getGson().fromJson(json, type)
            if (fingpayMiniStatementResponse != null) {
                arrayListMiniStatement.addAll(fingpayMiniStatementResponse?.statement!!)
            }
        }

        if (fingpayMiniStatementResponse != null) {
            tvMainWalletBalance.text = if (fingpayMiniStatementResponse?.amount != null && fingpayMiniStatementResponse!!.amount.isNotEmpty()) Utils.formatAmount(fingpayMiniStatementResponse!!.amount.toDouble()) else "NA"
            tvAgentNameValue.text = fingpayMiniStatementResponse!!.rrn

            tvAgentAadharNoValue.text = fingpayMiniStatementResponse!!.bank
                /*if (miniStatementResponse?.dATA?.txnDate != null && miniStatementResponse?.dATA?.txnDate!!.isNotEmpty() && miniStatementResponse?.dATA?.txnDate!!.contains("IST"))
                    Utils.convertDateTime(miniStatementResponse?.dATA?.txnDate!!)
                else if (miniStatementResponse?.dATA?.txnDate != null && miniStatementResponse?.dATA?.txnDate!!.isNotEmpty())
                    Utils.formateTxnDateString("dd/MM/yyyy hh:mm:ss", "EEE, dd MMM yyyy, hh:mm aa", miniStatementResponse?.dATA?.txnDate!!)
                else "NA"*/

            layoutManager = LinearLayoutManager(getViewActivity(), RecyclerView.VERTICAL, false)
            miniBankStatementAdapter = MiniStatementAdapter(getViewActivity(), arrayListMiniStatement) {
            }
            recyclerviewStatement.setHasFixedSize(true)
            recyclerviewStatement.layoutManager = layoutManager
            recyclerviewStatement.adapter = miniBankStatementAdapter
        }

        ivHomeBack.setOnClickListener {
            onBackPressed()
        }

        ivDownload.setOnClickListener {
            firstTimePermission = false
            if (!hasReadWritePermission()) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getViewActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Utils.showAlert(getViewActivity(), getString(R.string.provide_permission_storage), "Storage Permission", View.OnClickListener {
                        //Yes
                        ActivityCompat.requestPermissions(getViewActivity(),
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_STORAGE)
                    }, View.OnClickListener {
                        //No
                    })
                } else {
                    ActivityCompat.requestPermissions(getViewActivity(),
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_STORAGE)
                }
            } else {
                openSharableIntnet()
            }
        }

        /*if (!hasReadWritePermission()) {
            firstTimePermission = true
            if (ActivityCompat.shouldShowRequestPermissionRationale(getViewActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Utils.showAlert(getViewActivity(), getString(R.string.provide_permission_storage), "Storage Permission", View.OnClickListener {
                    //Yes
                    ActivityCompat.requestPermissions(getViewActivity(),
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_STORAGE)
                }, View.OnClickListener {
                    //No
                })
            } else {
                ActivityCompat.requestPermissions(getViewActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_STORAGE)
            }
        }*/
    }

    @SuppressLint("CheckResult")
    private fun openSharableIntnet() {
        if (shareUri != null) {
            shareImage()
            return
        }

        if (recyclerviewStatement != null) {
            var dir: File? = null
            showProgress()
            ivDownload.visibility = View.GONE
            Observable.fromCallable {
                store(takeScreenShot(recyclerviewStatement!!))
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    Log.e(TAG, it.message ?: "")
                    hideProgress()
                }
                .subscribe {
                    hideProgress()
                    if (it!!.exists()) {
                        shareUri = FileProvider.getUriForFile(getViewActivity(), BuildConfig.APPLICATION_ID + ".provider", it!!)
                        ivDownload.visibility = View.VISIBLE
                        shareImage()
                    }
                }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_STORAGE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && !firstTimePermission) {
            //Permission granted
            openSharableIntnet()
        } else if (requestCode == MY_PERMISSIONS_REQUEST_STORAGE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            //Permission not granted
            Utils.showAlert(getViewActivity(), getString(R.string.provide_permission_storage), "Storage Permission", View.OnClickListener {
                //Yes
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)

            }, View.OnClickListener {
                //No
            })
        }
    }

    fun takeScreenShot(rootView: View): Bitmap {
        val bitmapDetail = Bitmap.createBitmap(constraintStatementDetails.width, constraintStatementDetails.height, Bitmap.Config.ARGB_8888)
        val bitmapList = Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)

        val cs: Bitmap
        var width = 0
        var height = 0
        width = bitmapDetail.width
        height = bitmapDetail.height + bitmapList.height

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val comboImage = Canvas(cs)
        comboImage.drawBitmap(bitmapDetail, 0f, 0f, null);
        comboImage.drawBitmap(bitmapList, width.toFloat(), 0f, null)

        /*val canvas = Canvas(bitmap)
        rootView.draw(canvas)*/
        return cs
    }


    private fun shareImage() {
        showToast("Statement has been downloaded...")
        FileUtils.openFile(getViewActivity(), File(shareUri?.path!!), shareUri!!)
    }

    fun store(bm: Bitmap):File? {
        val shareImage = "BANK_RECEIPT_IMG_${System.currentTimeMillis()}.jpg"
        val folder = Utils.getExternalFilesAccessDir(this@MiniStatementActivity, Constants.MediaType.IMAGE)
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

    private fun hasReadWritePermission(): Boolean {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getViewActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
    }

    override fun onBackPressed() {
        Utils.showAlert(getViewActivity(), getString(R.string.are_you_sure_wants_to_close_mini_statement), "", View.OnClickListener {
            super.onBackPressed()
        }, View.OnClickListener {

        })
    }
}