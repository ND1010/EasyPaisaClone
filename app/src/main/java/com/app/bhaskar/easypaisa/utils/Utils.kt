package com.app.bhaskar.easypaisa.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.ui.dialog.DialogAlert
import com.app.bhaskar.easypaisa.ui.dialog.DialogAlertDevice
import com.app.bhaskar.easypaisa.ui.dialog.DialogSuccessAlert
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.InetAddress
import java.net.NetworkInterface
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class Utils {

    companion object {
        var PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        private var successAletD: DialogSuccessAlert? = null
        private val indianLocal = Locale("en", "IN")
        private val cache = Hashtable<String, Typeface>()
        val EMAIL_ADDRESS_PATTERN =
            Pattern.compile("[a-zA-Z0-9\\+\\._%\\-\\+]{1,256}" + "@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+")
        private var progressDialog: Dialog? = null

        fun isValidEmail(email: String): Boolean {
            return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
        }

        fun formatAmount(amount: Double): String {
            //return AppConstants.RUPEE_SYMBOL + String.format(" %.2f", Amount);
            val formatter = NumberFormat.getCurrencyInstance(indianLocal)
            return formatter.format(amount).trim { it <= ' ' }
        }

        fun isAppInstalled(context: Context, packageName: String): Boolean {
            return try {
                context.packageManager.getApplicationInfo(packageName, 0)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                redirectToPlayStore(context, packageName)
                false
            }
        }



        fun getImageFile(context: Context?): File? {
            if (context != null) {
                val pathname =
                    "${context.getExternalFilesDir(Environment.DIRECTORY_DCIM)}"

                Log.e("Utils","ImageFilePath: $pathname")
                val folder = File(pathname)
                folder.mkdirs()
                val file = File(folder, Utils.getDateString() + "_" + "Image.jpg")
                Log.d("Utils","ImageFile: $file")
                file.createNewFile()
                return file
            }

            return null
        }

        /**
         * Get IP address from first non-localhost interface
         * @param useIPv4   true=return ipv4, false=return ipv6
         * @return  address or empty string
         */
        fun getIPAddress(useIPv4: Boolean): String {
            try {
                val interfaces: List<NetworkInterface> =
                    Collections.list(NetworkInterface.getNetworkInterfaces())
                for (intf in interfaces) {
                    val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                    for (addr in addrs) {
                        if (!addr.isLoopbackAddress) {
                            val sAddr: String = addr.hostAddress
                            //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                            val isIPv4 = sAddr.indexOf(':') < 0
                            if (useIPv4) {
                                if (isIPv4) return sAddr
                            } else {
                                if (!isIPv4) {
                                    val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                    return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(
                                        0,
                                        delim
                                    ).toUpperCase()
                                }
                            }
                        }
                    }
                }
            } catch (ignored: java.lang.Exception) {
            } // for now eat exceptions
            return ""
        }

        fun getAppVersionName(mContext: Context): String {
            val manager = mContext.packageManager
            val info = manager.getPackageInfo(mContext.packageName, PackageManager.GET_ACTIVITIES)
            return info.versionName
        }

        fun isValidPanNo(panNo: String): Boolean {
            val pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
            val matcher = pattern.matcher(panNo)
            if (panNo.length != 10 || !matcher.matches())
                return false
            return true
        }

        fun getExternalFilesAccessDir(mContext: Context,mediaType: Int): File {
            return when(mediaType){
                Constants.MediaType.IMAGE ->{
                    File("${mContext.getExternalFilesDir(Environment.DIRECTORY_DCIM)}")
                }
                Constants.MediaType.DOWNLOAD ->{
                    File("${mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)}")
                }else ->{
                    File("${mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)}")
                }
            }
        }

        private fun redirectToPlayStore(context: Context, packageName: String) {
            try {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packageName")
                    )
                )
            } catch (e: android.content.ActivityNotFoundException) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
        }

        fun hideKeyboard(aContext: Activity?) {
            if (aContext != null) {
                val im =
                    aContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (im != null) {
                    im.hideSoftInputFromWindow(
                        aContext.window.decorView.windowToken,
                        HIDE_NOT_ALWAYS
                    )
                }
            }
        }

        /*
     * For Set Current Timestamp Name
     * */
        fun getDateString(): String {
            return SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        }

        fun getRootDir(Directory: String, subDirectory: String): File {

            val root = Environment.getExternalStorageDirectory().toString()
            val myDir = File(
                root + "/"
                        + Constants.DIRECTORY.ROOT_DIR + "/" + Directory + "/" + subDirectory
            )
            myDir.mkdirs()
            return myDir
        }

        fun getPath(context: Context, uri: Uri): String {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(uri, projection, null, null, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            } finally {
                if (cursor != null) {
                    cursor.close()
                }
            }
            return ""
        }

        // url = file path or whatever suitable URL you want.
        fun getMimeType(url: String): String? {
            var type: String? = null
            val extension: String? = MimeTypeMap.getFileExtensionFromUrl(url)
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
            return type
        }

        fun getRequest(reqString: String): RequestBody {
            val mediaType = Constants.ApiHeaders.API_TYPE_JSON.toMediaTypeOrNull()
            return RequestBody.create(mediaType, reqString)
        }

        fun showAlert(
            context: Context,
            message: String,
            title: String,
            yesClick: View.OnClickListener
        ) {
            try {
                DialogAlert(context).setMessage(message)
                    .setPositiveButton(context.getString(R.string.ok), yesClick).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun showAlert(
            context: Context,
            message: String,
            title: String,
            yesClick: View.OnClickListener,
            noClick: View.OnClickListener
        ) {
            try {
                DialogAlert(context).setMessage(message)
                    .setPositiveButton(context.getString(R.string.yes), yesClick)
                    .setNegativeButton(context.getString(R.string.no), noClick)
                    .show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        fun showAlert(
            context: Context,
            message: String,
            stryes: String,
            strno: String,
            yesClick: View.OnClickListener,
            noClick: View.OnClickListener
        ) {
            try {
                DialogAlert(context).setMessage(message)
                    .setPositiveButton(stryes, yesClick)
                    .setNegativeButton(strno, noClick)
                    .show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        fun showAlertDevice(
            context: Context,
            message: String,
            stryes: String,
            strno: String,
            yesClick: View.OnClickListener,
            noClick: View.OnClickListener
        ) {
            try {
                DialogAlertDevice(context).setMessage(message)
                    .setPositiveButton(stryes, yesClick)
                    .setNegativeButton(strno, noClick)
                    .show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun showAlert(
            context: Context,
            message: String,
            yesClick: View.OnClickListener
        ) {
            try {
                val d = DialogAlert(context)
                    .setMessage(message)
                    .setPositiveButton(context.getString(R.string.ok_btn), yesClick)
                d.setCancelable(false)
                d.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun convertDateTimeAeps(date: String, dateFormate: String): String {
            val simpledateformate = SimpleDateFormat(dateFormate, Locale.ENGLISH)
            val newDate = simpledateformate.parse(date)
            val spf = SimpleDateFormat("EEE, dd MMM yyyy, hh:mm aa", Locale.ENGLISH)
            val dateFinal = spf.format(newDate)
            Log.e("TAG", "FormatedDate: $dateFinal")

            return dateFinal
        }

        fun showSuccessAlert(context: Context, message: String, yesClick: View.OnClickListener) {
            try {
                DialogSuccessAlert(context).setMessage(message)
                    .setPositiveButton(context.getString(R.string.ok), yesClick)
                    .show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun showSuccessAlertOnly(context: Context, message: String) : DialogSuccessAlert{
            return DialogSuccessAlert(context).setMessage(message)
                .setMessageOnlyDialog(message)
        }

        fun showSuccessAlert(context: Context, message: String) {
            try {
                if (successAletD == null) {
                    successAletD = DialogSuccessAlert(context)
                }
                successAletD?.setMessage(message)
                successAletD?.setPositiveButton()
                successAletD?.show()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun dismissSuccessAlert() {
            try {
                if (successAletD!=null){
                    successAletD?.dismissDialog()
                    successAletD=null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

//        @SuppressLint("SimpleDateFormat")
//        fun isValideFutureTime(fromTime: String, toTime: String): Boolean {
//            val sdf = SimpleDateFormat("kk:mm:ss")
//            val datefrom = sdf.parse(fromTime)
//            val toTime = sdf.parse(toTime)
//            return !datefrom.before(toTime)
//        }
//
//        @SuppressLint("SimpleDateFormat")
//        fun isValideFutureDate(fromDate: String, toDate: String): Boolean {
//            val sdf = SimpleDateFormat("yyyy/MM/dd")
//            try {
//                if (sdf.parse(fromDate).before(sdf.parse(toDate))) {
//                    return true//If start date is before end date
//                } else if (sdf.parse(fromDate).equals(sdf.parse(toDate))) {
//                    return true//If two dates are equal
//                } else {
//                    return false //If start date is after the end date
//                }
//            } catch (e: ParseException) {
//                // TODO Auto-generated catch block
//                e.printStackTrace()
//            }
//            return false
//        }

        @SuppressLint("SimpleDateFormat")
        fun isSameDate(fromDate: String, toDate: String): Boolean {
            fromDate.replace("/", "-")
            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

            try {
                val toTaskDate = sdf.parse(toDate.replace("/", "-"))
                val fromTaskDate = sdf.parse(fromDate.replace("/", "-"))
                val miliToDate = toTaskDate.time
                val miliFromDate = fromTaskDate.time
                return miliFromDate < miliToDate
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return false
        }

        @SuppressLint("SimpleDateFormat")
        fun isSameDateYYYYMMDD(fromDate: String, toDate: String): Boolean {
            val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
            try {
                return if (sdf.parse(fromDate).before(sdf.parse(toDate))) {
                    true
                } else {
                    sdf.parse(fromDate) == sdf.parse(toDate)
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return false
        }

        fun hasReadFilePermission(mContest: Context): Boolean {
            return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                mContest,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED)
        }

        fun hasCameraPermission(mContest: Context): Boolean {
            return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                mContest,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED)
        }

        fun hasLocaionPermission(mContest: Context): Boolean {
            return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                mContest,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        }


        fun showSnackBar(
            view: View,
            message: String,
            time: Int,
            isTypeError: Boolean,
            context: Context
        ) {

            val snackbar = Snackbar.make(view, message, time)
            val snackBarView = snackbar.view
            val snackTextView =
                snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            snackTextView.maxLines = 4
            if (isTypeError) {
                snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        android.R.color.holo_red_dark
                    )
                )
            } else {
                snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        android.R.color.holo_green_dark
                    )
                )
            }
            snackbar.show()
        }

        fun isMyServiceRunning(mContext: Context, serviceClass: Class<*>): Boolean {
            val manager = mContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
            return false
        }

        fun showToast(message: CharSequence, context: Context) =
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        fun msToString(ms: Long): String {
            val totalSecs = ms / 1000
            val hours = totalSecs / 3600
            val mins = totalSecs / 60 % 60
            val secs = totalSecs % 60
            val minsString = if (mins == 0L)
                "00"
            else
                if (mins < 10)
                    "0$mins"
                else
                    "" + mins
            val secsString = if ((secs == 0L))
                "00"
            else
                (if ((secs < 10))
                    "0$secs"
                else
                    "" + secs)
            if (hours > 0)
                return (hours).toString() + ":" + minsString + ":" + secsString
            else return if (mins > 0)
                (mins).toString() + ":" + secsString
            else
                ":$secsString"
        }

        fun isNetworkConnected(context: Context): Boolean {
            var result = false
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm?.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        result = when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                            else -> false
                        }
                    }
                }
            } else {
                cm?.run {
                    cm.activeNetworkInfo?.run {
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            result = true
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            result = true
                        }
                    }
                }
            }
            return result
        }


        fun getTypeFace(aContext: Context, assetFile: String?): Typeface? {
            synchronized(cache) {
                if (!cache.containsKey(assetFile)) {
                    try {
                        val t = Typeface.createFromAsset(
                            aContext.assets,
                            assetFile
                        )
                        cache.put(assetFile, t)
                    } catch (e: Exception) {
                        Log.e(
                            TAG, "Could not get1 typeface '" + assetFile
                                    + "' because " + e.message
                        )
                        return null
                    }

                }
                return cache.get(assetFile)
            }
        }

        fun getDate(milliSeconds: Long, dateFormat: String): String {
            // Create a DateFormatter object for displaying date in specified format.
            val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = milliSeconds
            return formatter.format(calendar.time)
        }

        /**
         * This method is used to show progress indicator dialog with message when
         * web service is called.
         */
        fun showProgressDialog(context: Context?, message: String) {

            if (context != null && !(context as Activity).isFinishing) {
                if (progressDialog == null || !progressDialog!!.isShowing) {
                    progressDialog = Dialog(context)
                    progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    try {
                        val dividerId = progressDialog!!.context
                            .resources.getIdentifier("android:id/titleDivider", null, null)
                        val divider = progressDialog!!.findViewById<View>(dividerId)
                        divider?.setBackgroundColor(context.resources.getColor(android.R.color.transparent))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    progressDialog!!.setContentView(R.layout.custom_progress)
                    val tvMessage = progressDialog!!.findViewById<View>(R.id.txtMessage) as TextView
                    if (message != "") {
                        tvMessage.visibility = View.VISIBLE
                        tvMessage.text = message
                    }
                    progressDialog!!.setCancelable(false)
                    if (!context.isFinishing)
                        progressDialog!!.show()
                }
            }
        }

        fun hideProgressDialog() {
            try {
                if (progressDialog != null && progressDialog!!.isShowing) {
                    progressDialog!!.dismiss()
                }
            } catch (throwable: Throwable) {

            } finally {
                progressDialog = null
            }
        }

        fun getFileTypeFromUri(context: Context, uri: Uri): String? {
            val cR = context.contentResolver
            val mime = MimeTypeMap.getSingleton()
            return mime.getExtensionFromMimeType(cR.getType(uri))
        }

        fun getImageUri(viewActivity: Context, bitmap: Bitmap?): Uri? {
            val bytes = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(
                viewActivity.contentResolver,
                bitmap,
                "Title",
                null
            )
            return Uri.parse(path)

        }

        fun getFormattedDate(oldFormat: String, newFormat: String, date: String): String {
            val format1 = SimpleDateFormat(oldFormat)
            val format2 = SimpleDateFormat(newFormat)
            val startDate = format1.parse(date)
            return format2.format(startDate)
        }

        fun subStringlastString(text: String, char: String): String {
            var s = ""
            if (text.contains(char)) {
                val splitedText = text.split(char)
                if (splitedText.isNotEmpty()) {
                    s = splitedText[splitedText.size - 1]
                    return s
                }
            }
            return s
        }

    }
}