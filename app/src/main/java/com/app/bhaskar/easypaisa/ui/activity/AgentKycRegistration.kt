package com.app.bhaskar.easypaisa.ui.activity

import aepsapp.easypay.com.aepsandroid.camera.CameraActivity
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.app.bhaskar.easypaisa.BuildConfig
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.model.AgentKycActivityModel
import com.app.bhaskar.easypaisa.mvp.presenter.AgentKycPresenter
import com.app.bhaskar.easypaisa.mvp.presenter.AgentKycPresenterImpl
import com.app.bhaskar.easypaisa.request_model.OkycUserDataRequest
import com.app.bhaskar.easypaisa.request_model.SelectedImage
import com.app.bhaskar.easypaisa.response_model.GenerateTokenFaceResponse
import com.app.bhaskar.easypaisa.response_model.RegOtpResponse
import com.app.bhaskar.easypaisa.response_model.UserRequiredDataResponse
import com.app.bhaskar.easypaisa.response_model.VerifyPancardResponse
import com.app.bhaskar.easypaisa.ui.dialog.DialogOkycAadhaarDetail
import com.app.bhaskar.easypaisa.ui.fragment.HomeFragment
import com.app.bhaskar.easypaisa.ui.listener.OnUserLocationListener
import com.app.bhaskar.easypaisa.ui.listener.PhotoSelectionListeners
import com.app.bhaskar.easypaisa.ui.service.GoogleFuesedLocationService
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.FileUtils
import com.app.bhaskar.easypaisa.utils.ImageCompress
import com.app.bhaskar.easypaisa.utils.Utils
import com.example.android.camera.utils.decodeExifOrientation
import com.morpho.android.usb.USBManager.context
import com.pa.baseframework.baseview.BaseActivity
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_agent_kyc_registration.*
import kotlinx.coroutines.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.collections.ArrayList


class AgentKycRegistration : BaseActivity(), AgentKycPresenter.AgentKycView, View.OnClickListener,
    PhotoSelectionListeners {

    private lateinit var bitmapTransformation: Matrix
    private var filePath: String = ""
    private var isDepth: Boolean = false
    private val USER_PHOTO: Int = 656

    //    private var isValidPanNumber: Boolean = false
    private var panNumber: String = ""
    private var okycUserDataRequest: OkycUserDataRequest? = null
    private var isOkycAadhaarVerified = false
    private val PERMISSIONS_REQUEST_CODE = 10
    private var firstTimePermission = false
    var PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )


    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.constraintLayoutSelectPanCard -> {
                SelectedImageView = view.id
                /*val photoSelectionAsk =
                    PhotoSelectionAsk(getViewActivity(), "Capture Pan card", this)
                photoSelectionAsk.show()*/
                chooseImage()
            }
            R.id.constraintLayoutSelectAadhaarCard -> {
                SelectedImageView = view.id
                /*val photoSelectionAsk =
                    PhotoSelectionAsk(getViewActivity(), "Capture Aadhaar Card Front", this)
                photoSelectionAsk.show()*/
                chooseImage()
            }
            R.id.constraintLayoutSelectAadhaarCardBack -> {
                SelectedImageView = view.id
                /*val photoSelectionAsk =
                    PhotoSelectionAsk(getViewActivity(), "Capture Aadhaar Card Back", this)
                photoSelectionAsk.show()*/
                chooseImage()
            }
            R.id.btnProceedKyc -> {
                if (isValide) {
                    if (EasyPaisaApp.getUserLatLng() == null) {
                        googleFusedLocation.needToShowDialog = true
                        googleFusedLocation.accessUserCurrentLocation()
                        return
                    }
                    doRegisterAgent()
                }
            }
        }
    }

    private fun doRegisterAgent() {
        val request = doRetriveModel().getAgentKycRequest()
        request.merchantAadhar = textInputLayoutRegAadhaarNo.editText!!.text.toString()
        request.userPan = textInputLayoutRegPanCardNo.editText!!.text.toString()
        request.merchantCityName = textInputLayoutRegCity.editText!!.text.toString()
        request.merchantAddress = textInputLayoutRegAddress.editText!!.text.toString()
        request.merchantState =
            arrayListState[spinnerState.selectedItemPosition].state
        request.merchantPinCode = textInputLayoutRegPinCode.editText!!.text.toString()
        request.pancardPics = selectedImage.photo_pancard!!
        request.aadharPics = selectedImage.photo_aadhaarcard!!
        request.aadharcardbackpics = selectedImage.photo_aadhaarcard_back!!
        request.userSelfieImages = selectedImage.userSelfieImages!!
        request.latitude = EasyPaisaApp.getUserLatLng()?.latitude.toString()
        request.longitude = EasyPaisaApp.getUserLatLng()?.longitude.toString()
        presenter.doAgentKyc()
    }

    companion object {
        const val TAG = "AgentKycRegistration"
    }

    private var karzaToken: String = ""
    private var mediaType: Int = 0
    lateinit var matcher: Matcher
    private var SelectedImageView: Int = 0

    @Inject
    lateinit var presenter: AgentKycPresenterImpl
    lateinit var model: AgentKycActivityModel
    private var arrayListState = ArrayList<UserRequiredDataResponse.State>()
    private lateinit var stateAdapter: ArrayAdapter<UserRequiredDataResponse.State>
    private var destination: File? = null
    private var imageToUploadUri: Uri? = null
    private var selectedImage: SelectedImage = SelectedImage()
    private lateinit var googleFusedLocation: GoogleFuesedLocationService

    val isValide: Boolean
        get() {
            if (selectedImage.userSelfieImages.isNullOrEmpty()) {
                showToast("Please capture your Selfie photo for verification")
                return false
            }

            if (textInputLayoutRegFullName.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutRegFullName.error = "Please enter your full name"
                textInputLayoutRegFullName.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutRegFullName.isErrorEnabled = false
            }

            if (textInputLayoutRegMobileNo.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutRegMobileNo.error = "Please enter mobile number"
                textInputLayoutRegMobileNo.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutRegMobileNo.isErrorEnabled = false
            }

            if (textInputLayoutRegMobileNo.editText!!.text.toString().trim().length != 10) {
                textInputLayoutRegMobileNo.error = "Invalid mobile number"
                textInputLayoutRegMobileNo.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutRegMobileNo.isErrorEnabled = false
            }

            if (textInputLayoutRegAadhaarNo.editText!!.text.toString().trim().length != 12) {
                textInputLayoutRegAadhaarNo.error = "Invalid Aadhaar Number"
                textInputLayoutRegAadhaarNo.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutRegAadhaarNo.isErrorEnabled = false
            }

            if (textInputLayoutRegPanCardNo.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutRegPanCardNo.error = "Please enter PAN card number"
                textInputLayoutRegPanCardNo.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutRegPanCardNo.isErrorEnabled = false
            }

            val pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
            matcher = pattern.matcher(textInputLayoutRegPanCardNo.editText!!.text.toString().trim())
            if (textInputLayoutRegPanCardNo.editText!!.text.toString()
                    .trim().length != 10 || !matcher.matches()
            ) {
                textInputLayoutRegPanCardNo.error = "Enter valid PAN card number"
                textInputLayoutRegPanCardNo.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutRegPanCardNo.isErrorEnabled = false
            }

            if (textInputLayoutRegCity.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutRegCity.error = "Please enter city"
                textInputLayoutRegCity.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutRegCity.isErrorEnabled = false
            }

            if (spinnerState.selectedItemPosition == 0) {
                showToast("Please select state")
                return false
            }

            if (textInputLayoutRegAddress.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutRegAddress.error = "Please enter address"
                textInputLayoutRegAddress.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutRegAddress.isErrorEnabled = false
            }

            if (textInputLayoutRegAddress.editText!!.text.toString().trim().length <= 10) {
                textInputLayoutRegAddress.error = "Address should be more then 10 characters"
                textInputLayoutRegAddress.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutRegAddress.isErrorEnabled = false
            }

            if (textInputLayoutRegPinCode.editText!!.text.toString().trim().isEmpty()) {
                textInputLayoutRegPinCode.error = "Please enter Pincode"
                textInputLayoutRegPinCode.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutRegPinCode.isErrorEnabled = false
            }

            if (textInputLayoutRegPinCode.editText!!.text.toString().trim().length != 6) {
                textInputLayoutRegPinCode.error = "Invalid Pincode"
                textInputLayoutRegPinCode.editText!!.requestFocus()
                return false
            } else {
                textInputLayoutRegPinCode.isErrorEnabled = false
            }

            if (selectedImage.photo_pancard.isNullOrEmpty()) {
                showToast("Please capture or select Pan card photo")
                return false
            }

            if (selectedImage.photo_aadhaarcard.isNullOrEmpty()) {
                showToast("Please capture or select Pan Aadhaar card Front photo")
                return false
            }
            if (selectedImage.photo_aadhaarcard_back.isNullOrEmpty()) {
                showToast("Please capture or select Pan Aadhaar card Back photo")
                return false
            }
            return true
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agent_kyc_registration)
        model = AgentKycActivityModel(getViewActivity())
        presenter = AgentKycPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        initView()
//        presenter.doGetKarzaToken()
    }

    private fun initView() {
        constraintLayoutSelectPanCard.setOnClickListener(this)
        constraintLayoutSelectAadhaarCard.setOnClickListener(this)
        constraintLayoutSelectAadhaarCardBack.setOnClickListener(this)
        btnProceedKyc.setOnClickListener(this)
        textInputLayoutRegFullName.editText!!.setText(EasyPaisaApp.getLoggedInUser()?.name)
        textInputLayoutRegMobileNo.editText!!.setText(EasyPaisaApp.getLoggedInUser()?.mobile)

        val selectState = UserRequiredDataResponse.State(-1, "Select State", "-1", "-1")
        arrayListState.add(selectState)
        if (EasyPaisaApp.getUserRequiredData() != null) {
            arrayListState.addAll(EasyPaisaApp.getUserRequiredData()?.state as ArrayList<UserRequiredDataResponse.State>)
        }
        stateAdapter =
            ArrayAdapter(getViewActivity(), R.layout.spinner_item, arrayListState)
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerState.adapter = stateAdapter
        spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {

            }
        }

        /*textInputLayoutRegPanCardNo.editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                panNumber = s.toString().trim()
                if (tvUserPanName.visibility == View.VISIBLE) {
                    tvUserPanName.visibility = View.GONE
                }
                isValidPanNumber = false
                if (panNumber.isEmpty()) {
                    textInputLayoutRegPanCardNo.error =
                        getString(R.string.please_enter_pan_card_number)
                } else if (!Utils.isValidPanNo(panNumber)) {
                    textInputLayoutRegPanCardNo.error =
                        getString(R.string.please_enter_valid_PAN_number)
                } else {
                    textInputLayoutRegPanCardNo.isErrorEnabled = false
                    if (!isValidPanNumber)
                        presenter.verifyPanCard(panNumber)
                }
            }
        })*/

        ivUserFrontPic.setOnClickListener {
            SelectedImageView = it.id
            openFrontCamera()
        }

        if (SDK_INT >= Build.VERSION_CODES.M &&
            !hasPermissions(*PERMISSIONS)
        ) {
            firstTimePermission = true
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@AgentKycRegistration,
                    Manifest.permission.CAMERA
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@AgentKycRegistration,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                Utils.showAlert(
                    this@AgentKycRegistration,
                    "Please provide Camera permission to take photo.",
                    "Camera Permission",
                    View.OnClickListener {
                        //Yes
                        ActivityCompat.requestPermissions(
                            this,
                            PERMISSIONS,
                            PERMISSIONS_REQUEST_CODE
                        )
                    },
                    View.OnClickListener {
                        //No
                    })
            } else {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST_CODE)
            }
        }

        googleFusedLocation = GoogleFuesedLocationService(getViewActivity(), object :
            OnUserLocationListener {
            override fun onUserLocationSuccess(location: Location) {
                if (EasyPaisaApp.getUserLatLng() == null) {
                    showToast("User location not found, please enable GPS")
                    return
                }
                doRegisterAgent()
            }

            override fun onUserLocationFail(failMessage: String) {
            }

        }, true)

    }

    private fun openFrontCamera() {
        firstTimePermission = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            !hasPermissions(*PERMISSIONS)
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@AgentKycRegistration,
                    Manifest.permission.CAMERA
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@AgentKycRegistration,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                Utils.showAlert(
                    this@AgentKycRegistration,
                    "Please provide Camera permission to take photo.",
                    "Camera Permission",
                    View.OnClickListener {
                        //Yes
                        ActivityCompat.requestPermissions(
                            this,
                            PERMISSIONS,
                            PERMISSIONS_REQUEST_CODE
                        )
                    },
                    View.OnClickListener {
                        //No
                    })
            } else {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST_CODE)
            }
        } else {
            /*removeCapturedPhoto()
            openCamera()*/

            val intentCamera = Intent(this@AgentKycRegistration, CameraActivity::class.java)
            startActivityForResult(intentCamera, USER_PHOTO)
        }
    }

    override fun getViewActivity(): Activity {
        return this@AgentKycRegistration
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    override fun doRetriveModel(): AgentKycActivityModel {
        return model
    }

    fun hasPermissions(vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(
            this@AgentKycRegistration,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun chooseImage() {
        if (SDK_INT >= Build.VERSION_CODES.M &&
            !hasPermissions(*Utils.PERMISSIONS)
        ) {
            presenter.requestForCameraPermission()
        } else {
            startActivityForResult(getPickImageIntent(), Constants.UI.TYPE_PHOTO_PICK_FROM_CAMERA)
        }
    }

    private fun getImageUri(): Uri {
        val folder = File("${getExternalFilesDir(Environment.DIRECTORY_DCIM)}")
        if (!folder.exists())
        folder.mkdirs()

        destination = File(folder, setImageName())
        if (!destination!!.exists())
            destination!!.createNewFile()

        imageToUploadUri = FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID + getString(R.string.file_provider_name),
            destination!!
        )
        return imageToUploadUri!!
    }

    private fun addIntentsToList(
        context: Context,
        list: MutableList<Intent>,
        intent: Intent
    ): MutableList<Intent> {
        val resInfo = context.packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in resInfo) {
            val packageName = resolveInfo.activityInfo.packageName
            val targetedIntent = Intent(intent)
            targetedIntent.setPackage(packageName)
            list.add(targetedIntent)
        }
        return list
    }

    private fun getPickImageIntent(): Intent? {
        var chooserIntent: Intent? = null

        var intentList: MutableList<Intent> = ArrayList()

        val pickIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri())

        intentList = addIntentsToList(this, intentList, pickIntent)
        intentList = addIntentsToList(this, intentList, takePhotoIntent)

        if (intentList.size > 0) {
            chooserIntent = Intent.createChooser(
                intentList.removeAt(intentList.size - 1),
                getString(R.string.select_capture_image)
            )

            chooserIntent?.putExtra(
                Intent.EXTRA_INITIAL_INTENTS,
                intentList.toTypedArray<Parcelable>()
            )
        }
        return chooserIntent
    }

    override fun onCameraClick() {
        mediaType = 0
        if (SDK_INT >= Build.VERSION_CODES.M &&
            !hasPermissions(*Utils.PERMISSIONS)
        ) {
            presenter.requestForCameraPermission()
        } else {
            captureImageFromCamera()
        }
    }

    override fun onGalleryClick() {
        mediaType = 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            !hasPermissions(*Utils.PERMISSIONS)
        ) {
            presenter.requestForCameraPermission()
        } else {
            selectImageFromGallery()
        }
    }

    override fun onRemoveClick() {
    }

    private fun setImageName(): String {

        var name = ""
        val imgExt = ".jpg"
        name = when (SelectedImageView) {
            R.id.constraintLayoutSelectAadhaarCard -> Utils.getDateString() + "_" + "AadharCard_Image_front" + imgExt
            R.id.constraintLayoutSelectAadhaarCardBack -> Utils.getDateString() + "_" + "AadharCard_Image_back" + imgExt
            R.id.constraintLayoutSelectPanCard -> Utils.getDateString() + "_" + "pancard_image" + imgExt
            R.id.ivUserFrontPic -> Utils.getDateString() + "_" + "userface_image" + imgExt
            else -> Utils.getDateString() + imgExt
        }
        return name
    }

    private fun captureImageFromCamera() {
        val chooserIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val directory =
            Utils.getRootDir(Constants.DIRECTORY.PROFILE_DIR, Constants.DIRECTORY.IMAGE_DIR)
        destination = File(directory, setImageName())
        if (!destination!!.exists())
            destination!!.createNewFile()
        imageToUploadUri = FileProvider.getUriForFile(
            getViewActivity(),
            getViewActivity().packageName + ".provider",
            destination!!
        )
        chooserIntent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            imageToUploadUri
        )
        startActivityForResult(chooserIntent, Constants.UI.TYPE_PHOTO_PICK_FROM_CAMERA)
    }

    // Select Image from Gallery
    private fun selectImageFromGallery() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(
            Intent.createChooser(intent, "Complete action using"),
            Constants.UI.TYPE_PHOTO_PICK_FROM_FILE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED || resultCode != RESULT_OK)
            return
        when (requestCode) {
            2296 -> {
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        // perform action when allow permission success
                    } else {
                        showToast("Allow permission for storage access!")
                    }
                }
            }
            3030 -> {
                if (resultCode == Activity.RESULT_OK) {
                    googleFusedLocation.getUserLocation()
                }
            }
            100 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val bundle = data.extras
                    if (bundle != null) {
                        val isError = bundle["isError"] as String?
                        val exitByUser = bundle["exitByUser"] as String?
                        val message = bundle["message"] as String?
                        val code = bundle["code"] as String?
                        Log.e(HomeFragment.TAG, "isError: $isError")
                        if (isError == "false" && exitByUser != null && exitByUser == "true") {
                            Utils.showAlert(
                                getViewActivity(),
                                message ?: "Canceled by user",
                                "Easy Paisa",
                                View.OnClickListener { })
                            return
                        }
                        if (isError == "true" && exitByUser != null && exitByUser == "true") {
                            Utils.showAlert(
                                getViewActivity(),
                                message ?: "Cancel by user",
                                "Easy Paisa",
                                View.OnClickListener { })
                            return
                        }
                        if (isError == "true" && exitByUser != null && exitByUser == "false") {
                            Utils.showAlert(
                                getViewActivity(),
                                message ?: "Cancel by user",
                                "Easy Paisa",
                                View.OnClickListener { })
                            return
                        }

                        isError?.let {
                            if (it == "false") {
                                isOkycAadhaarVerified = true
                                val address = bundle["address"] as String?
                                val dob = bundle["dob"] as String?
                                val emailHash = bundle["emailHash"] as String?
                                val genDate = bundle["genDate"] as String?
                                val gender = bundle["gender"] as String?
                                val imagebase64 = bundle["imagebase64"] as String?
                                val isEmailVerified = bundle["isEmailVerified"] as String?
                                val isMobileVerified = bundle["isMobileVerified"] as String?
                                val isXmlVerify = bundle["isXmlVerify"] as String?
                                val maskedAadhaarNumber = bundle["maskedAadhaarNumber"] as String?
                                val mobileHash = bundle["mobileHash"] as String?
                                val name = bundle["name"] as String?
                                val sharecode = bundle["sharecode"] as String?
                                val zipFileBase64 = bundle["zipFileBase64"] as String?
                                val aadhaarNumber = bundle["aadhaarNumber"] as String?
                                val address_careof = bundle["address_careof"] as String?
                                val address_house = bundle["address_house"] as String?
                                val address_loc = bundle["address_loc"] as String?
                                val address_street = bundle["address_street"] as String?
                                val address_landmark = bundle["address_landmark"] as String?
                                val address_vtc = bundle["address_vtc"] as String?
                                val address_pc = bundle["address_pc"] as String?
                                val address_po = bundle["address_po"] as String?
                                val address_subdist = bundle["address_subdist"] as String?
                                val address_dist = bundle["address_dist"] as String?
                                val address_state = bundle["address_state"] as String?
                                val address_country = bundle["address_country"] as String?
                                val code = bundle["code"] as String?

                                Log.d("OKyc", isError.toString())
                                Log.d("address", address.toString())
                                Log.d("dob", dob.toString())
                                Log.d("emailHash", emailHash.toString())
                                Log.d("genDate", genDate.toString())
                                Log.d("gender", gender.toString())
                                Log.d("imagebase64", imagebase64.toString())
                                Log.d("isEmailVerified", isEmailVerified.toString())
                                Log.d("isError", isError.toString())
                                Log.d("isMobileVerified", isMobileVerified.toString())
                                Log.d("isXmlVerify", isXmlVerify.toString())
                                Log.d("mobileHash", mobileHash.toString())
                                Log.d("name", name.toString())
                                Log.d("sharecode", sharecode.toString())
                                Log.d("zipFileBase64", zipFileBase64.toString())
                                Log.d("code", code.toString())
                                Log.d("aadhaarNumber", aadhaarNumber.toString())
                                Log.d("address_careof", address_careof.toString())
                                Log.d("address_house", address_house.toString())
                                Log.d("address_loc", address_loc.toString())
                                Log.d("address_street", address_street.toString())
                                Log.d("address_landmark", address_landmark.toString())
                                Log.d("address_vtc", address_vtc.toString())
                                Log.d("address_pc", address_pc.toString())
                                Log.d("address_po", address_po.toString())
                                Log.d("address_subdist", address_subdist.toString())
                                Log.d("address_dist", address_dist.toString())
                                Log.d("address_state", address_state.toString())
                                Log.d("address_country", address_country.toString())
                                Log.d("code", code.toString())
                                okycUserDataRequest = OkycUserDataRequest()
                                okycUserDataRequest?.apptoken =
                                    EasyPaisaApp.getLoggedInUser()?.apptoken.toString()
                                okycUserDataRequest?.userId =
                                    EasyPaisaApp.getLoggedInUser()?.id.toString()

                                okycUserDataRequest?.data?.aadhaarNumber = aadhaarNumber.toString()
                                okycUserDataRequest?.data?.address = address.toString()
                                okycUserDataRequest?.data?.addressCareof = address_careof.toString()
                                okycUserDataRequest?.data?.addressCountry =
                                    address_country.toString()
                                okycUserDataRequest?.data?.addressDist = address_dist.toString()
                                okycUserDataRequest?.data?.addressHouse = address_house.toString()
                                okycUserDataRequest?.data?.addressLandmark =
                                    address_landmark.toString()
                                okycUserDataRequest?.data?.addressLoc = address_loc.toString()
                                okycUserDataRequest?.data?.addressPc = address_pc.toString()
                                okycUserDataRequest?.data?.addressPo = address_po.toString()
                                okycUserDataRequest?.data?.addressState = address_state.toString()
                                okycUserDataRequest?.data?.addressStreet = address_street.toString()
                                okycUserDataRequest?.data?.addressSubdist =
                                    address_subdist.toString()
                                okycUserDataRequest?.data?.addressVtc = address_vtc.toString()
                                okycUserDataRequest?.data?.code = code.toString()
                                okycUserDataRequest?.data?.dob = dob.toString()
                                okycUserDataRequest?.data?.emailHash = emailHash.toString()
                                okycUserDataRequest?.data?.genDate = genDate.toString()
                                okycUserDataRequest?.data?.gender = gender.toString()
                                okycUserDataRequest?.data?.imagebase64 = imagebase64.toString()
                                okycUserDataRequest?.data?.isEmailVerified =
                                    isEmailVerified.toString()
                                okycUserDataRequest?.data?.isError = isError.toString()
                                okycUserDataRequest?.data?.isMobileVerified =
                                    isMobileVerified.toString()
                                okycUserDataRequest?.data?.isXmlVerify = isXmlVerify.toString()
                                setUserData()

                            } else {
                                isOkycAadhaarVerified = false
                            }
                        }
                    }
                }

                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
            }

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val uriPath = CropImage.getActivityResult(data).uri.path
                if (uriPath == null) {
                    Toast.makeText(getViewActivity(), getString(R.string.some_thing_wants_wong), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "CropImage_URI_NULL")
                    return
                }
                val mTempFile = File(uriPath)
                val file_size = Integer.parseInt((mTempFile.length() / 1024).toString())
                Log.e(TAG, "file_size B4 $file_size")

                if (file_size > 1048) {
                    val exceptionHandler = CoroutineExceptionHandler { _, t ->
                        t.printStackTrace()
                        hideProgress()
                        Toast.makeText(
                            this,
                            t.localizedMessage
                                ?: getString(R.string.some_thing_wants_wong),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    GlobalScope.launch(Dispatchers.Main + exceptionHandler) {
                        showProgress()
                        imageToUploadUri = compressFile(mTempFile)
                        imageToUploadUri?.let{
                            addPathInRequest(File(it.path!!))
                        }
                        hideProgress()
//                        onGettingBitmapForImageView(MediaStore.Images.Media.getBitmap(contentResolver, uriFile))
//                        Utils.hideProgressDialogApp()
                    }
                } else {
                    imageToUploadUri = CropImage.getActivityResult(data).uri
                    imageToUploadUri?.let{
                        addPathInRequest(File(it.path!!))
                    }
                }
            }

            Constants.UI.TYPE_PHOTO_PICK_FROM_CAMERA -> {
                if (resultCode == Activity.RESULT_OK) {
                    /*val exceptionHandler = CoroutineExceptionHandler { _, t ->
                        t.printStackTrace()
                        hideProgress()
                        Toast.makeText(
                            this,
                            t.localizedMessage ?: getString(R.string.some_thing_wants_wong),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    GlobalScope.launch(Dispatchers.Main + exceptionHandler) {
                        showProgress()
                        if (data?.data != null) {
                            //destination = File(FileUtils.getPath(getViewActivity(), data.data!!))
                            Observable.fromCallable {
                                saveImage(data.data!!)
                            }.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnError {
                                    showError(
                                        it.localizedMessage
                                            ?: getString(R.string.some_thing_wants_wong)
                                    )
                                }
                                .subscribe {
                                    it?.let {
                                        destination = it
                                        imageToUploadUri = Uri.fromFile(destination)
                                        addPathInRequest(destination!!)
                                        val file_size =
                                            Integer.parseInt((destination!!.length() / 1024).toString())
                                        Log.e(
                                            TAG,
                                            "onCaptureImageResult:AFTER file_size $file_size"
                                        )
                                    }
                                }

                        } else {
                            val mFilePath = onCaptureImageResult()
                            imageToUploadUri = Uri.fromFile(File(mFilePath))
                            val file_size =
                                Integer.parseInt((File(mFilePath).length() / 1024).toString())
                            Log.e(TAG, "after_file_compress: $file_size")
                            addPathInRequest(File(mFilePath))
                        }
                        hideProgress()
                    }*/

                    if (data?.data != null) {//FROM Gallery
                        CropImage.activity(data.data)
                            .setActivityTitle("Crop Image")
                            .setCropMenuCropButtonTitle("Save")
                            .start(getViewActivity())
                    } else {
                        //From Camera
                        CropImage.activity(imageToUploadUri)
                            .setActivityTitle("Crop Image")
                            .setCropMenuCropButtonTitle("Save")
                            .start(getViewActivity())
                    }
                } else {
                    //imageToUploadUri = null
                    when (SelectedImageView) {
                        R.id.constraintLayoutSelectPanCard -> {
                            selectedImage.photo_pancard = null
                            ivPanCardIcon.visibility = View.VISIBLE
                            ivPanCardSelected.setImageResource(0)
                        }
                        R.id.constraintLayoutSelectAadhaarCard -> {
                            selectedImage.photo_aadhaarcard = null
                            ivAadhaarCardIcon.visibility = View.VISIBLE
                            ivAadhaarCardSelected.setImageResource(0)
                        }
                        R.id.constraintLayoutSelectAadhaarCardBack -> {
                            selectedImage.photo_aadhaarcard_back = null
                            ivAadhaarCardIconBack.visibility = View.VISIBLE
                            ivAadhaarCardSelectedBback.setImageResource(0)
                        }
                    }
                }


            }

            Constants.UI.TYPE_PHOTO_PICK_FROM_FILE -> {
                if (resultCode == Activity.RESULT_OK) {

                    val mFilePath = FileUtils.getPath(getViewActivity(), data?.data!!)
                    //imageToUploadUri = Uri.fromFile(File(mFilePath))
                    //addPathInRequest(File(mFilePath))
                    var compressedImage: File?
                    if (!TextUtils.isEmpty(mFilePath)) {
                        Compressor(getViewActivity())
                            .compressToFileAsFlowable(File(mFilePath!!))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                compressedImage = it
                                Log.e(TAG, "ImageCompressed: ${compressedImage!!.length() / 1024}")
                                imageToUploadUri = Uri.fromFile(compressedImage)
                                addPathInRequest(compressedImage!!)

                            }, {
                                it.printStackTrace()
                                showError(it.message!!)
                            })

                    } else {
                        showError("Can't find file")
                    }
                } else {
                    imageToUploadUri = null
                    when (SelectedImageView) {
                        R.id.constraintLayoutSelectPanCard -> {
                            selectedImage.photo_pancard = null
                            ivPanCardIcon.visibility = View.VISIBLE
                            ivPanCardSelected.setImageResource(0)
                        }
                        R.id.constraintLayoutSelectAadhaarCard -> {
                            selectedImage.photo_aadhaarcard = null
                            ivAadhaarCardIcon.visibility = View.VISIBLE
                            ivAadhaarCardSelected.setImageResource(0)
                        }
                        R.id.constraintLayoutSelectAadhaarCardBack -> {
                            selectedImage.photo_aadhaarcard_back = null
                            ivAadhaarCardIconBack.visibility = View.VISIBLE
                            ivAadhaarCardSelectedBback.setImageResource(0)
                        }
                    }
                }
            }

            USER_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.let {
                        bitmapTransformation =
                            decodeExifOrientation(data.getIntExtra("orientation", 0))
                        isDepth = data.getBooleanExtra("depth", false)
                        filePath = data.getStringExtra("image") ?: ""

                        Utils.showProgressDialog(getViewActivity(), "Processing...")
                        Observable.fromCallable {
                            val file_size =
                                Integer.parseInt((File(filePath).length() / 1024).toString())
                            Log.e(TAG, "B4_FILE ${file_size}")
                            compressImageBase64(filePath)
                        }.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnError {
                                Utils.hideProgressDialog()
                                Log.e("TAG", it.message ?: "")
                            }
                            .doOnError {
                                Utils.hideProgressDialog()
                                Log.e("TAG", it.message ?: "")
                            }
                            .subscribe {
                                Utils.hideProgressDialog()
                                if (it == null) {
                                    selectedImage.userSelfieImages = null
                                    ivUserImage.visibility = View.VISIBLE
                                    ivUserFrontPic.setImageResource(0)
                                    return@subscribe
                                }
                                val file_size =
                                    Integer.parseInt((it.length() / 1024).toString())
                                Log.e(TAG, "AFTER_FILE $file_size")
                                it.let { bitmap ->
                                    imageToUploadUri = Uri.fromFile(it)
                                    addPathInRequest(it)
                                }
                            }

                        /*var compressedImage: File?
                        if (!TextUtils.isEmpty(filePath)) {
                            Utils.showProgressDialog(getViewActivity(), "Processing...")
                            Compressor(getViewActivity())
                                .compressToFileAsFlowable(File(filePath))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    Utils.hideProgressDialog()
                                    compressedImage = it
                                    Log.e(TAG, "ImageCompressed: ${compressedImage!!.length() / 1024}")
                                    imageToUploadUri = Uri.fromFile(compressedImage)
                                    addPathInRequest(compressedImage!!)

                                }, {
                                    Utils.hideProgressDialog()
                                    it.printStackTrace()
                                    showError(it.message!!)
                                })

                        } else {
                            showError("Can't find file")
                        }*/
                    }
                }
            }
        }
    }

    private fun compressImageBase64(filePath: String?): File? {

        var compressedImage = Compressor(this@AgentKycRegistration)
            .setQuality(60)
            .compressToBitmap(File(filePath))

        compressedImage = Bitmap.createBitmap(
            compressedImage,
            0,
            0,
            compressedImage.width,
            compressedImage.height,
            bitmapTransformation,
            true
        )
        var fos: FileOutputStream? = null
        var secondFile: File? = null

        try {
            val timeStamp = SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.ENGLISH
            ).format(Date())
            val imageFileName = "IMG_$timeStamp"

            val directory = File("${getExternalFilesDir(Environment.DIRECTORY_DCIM)}")
            if (!directory.exists()) {
                directory.mkdirs()
            }
            secondFile = File.createTempFile(
                imageFileName,   //prefix
                ".jpg",   //suffix
                directory  //directory
            )

            fos = FileOutputStream(secondFile)
            compressedImage.compress(Bitmap.CompressFormat.JPEG, 80, fos)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return secondFile
    }

    private fun setUserData() {

        val dialog = DialogOkycAadhaarDetail(this@AgentKycRegistration, okycUserDataRequest)
        dialog.showMicroAtmTransactionDialog {
            when (it) {
                0 -> {
                    okycUserDataRequest = null
                    finish()
                }
                1 -> {
                    okycUserDataRequest?.let {
                        textInputLayoutRegAadhaarNo.editText!!.isEnabled = false
                        textInputLayoutRegCity.editText!!.isEnabled = false
                        textInputLayoutRegAddress.editText!!.isEnabled = false
                        textInputLayoutRegPinCode.editText!!.isEnabled = false

                        textInputLayoutRegAadhaarNo.editText!!.setText(it.data.aadhaarNumber)
                        textInputLayoutRegCity.editText!!.setText(it.data.addressDist)
                        textInputLayoutRegAddress.editText!!.setText(it.data.address)
                        textInputLayoutRegPinCode.editText!!.setText(it.data.addressVtc)
                    }
                    if (okycUserDataRequest != null && isOkycAadhaarVerified) {
                        presenter.doSendOkycUserAadharData(okycUserDataRequest!!)
                    }
                }
            }
        }
    }

    /*
     * Image Process after Capture from Camera
     * */
    private suspend fun onCaptureImageResult(): String {
        return withContext(Dispatchers.IO) {
            val file = File(destination!!.path)
            val file_size = Integer.parseInt((file.length() / 1024).toString())
            Log.e(TAG, "onCaptureImageResult: file_size $file_size")
            if (file_size > 1028) {
                val imageCompress = ImageCompress(getViewActivity(), file.path)
                imageCompress.compressImage()
            }
            return@withContext file.path
        }
    }

    fun saveImage(data: Uri): File? {
        val mTempFile = Utils.getImageFile(getViewActivity())
        var scaledBitmap: Bitmap? = null
        val input: InputStream =
            contentResolver.openInputStream(data) ?: return null
        scaledBitmap = BitmapFactory.decodeStream(input)

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mTempFile)
            scaledBitmap?.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                fos
            )
            fos.flush()
            fos.close()

            if (mTempFile == null) {
                return null
            }
            val file_size = Integer.parseInt((mTempFile.length() / 1024).toString())
            Log.e(TAG, "onCaptureImageResult: file_size $file_size")
            if (file_size > 1048) {
                val file = File(mTempFile.path)
                val imageCompress = ImageCompress(getViewActivity(), file.path)
                imageCompress.compressImage()
                return file
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return mTempFile
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (hasPermissions(*Utils.PERMISSIONS)) {
            /*if (mediaType == 0) {
                captureImageFromCamera()
            } else {
                selectImageFromGallery()
            }*/
            chooseImage()
        } else if (requestCode == Constants.UI.MY_PERMISSIONS_REQUEST_STORAGE_READ && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_DENIED
        ) {
            //Permission not granted
            showError("Cant able to access app camera or gallery without permission,please provide Camera permission")
        }
    }


    private fun addPathInRequest(file: File) {
        when (SelectedImageView) {
            R.id.constraintLayoutSelectPanCard -> {
                selectedImage.photo_pancard = imageToUploadUri?.path!!
                linearSelectPanCard.visibility = View.GONE
                ivPanCardSelected.setImageURI(imageToUploadUri)
            }
            R.id.constraintLayoutSelectAadhaarCard -> {
                selectedImage.photo_aadhaarcard = imageToUploadUri?.path!!
                linearSelectAadhaarCard.visibility = View.GONE
                ivAadhaarCardSelected.setImageURI(imageToUploadUri)
            }
            R.id.constraintLayoutSelectAadhaarCardBack -> {
                selectedImage.photo_aadhaarcard_back = imageToUploadUri?.path!!
                linearSelectAadhaarCardBack.visibility = View.GONE
                ivAadhaarCardSelectedBback.setImageURI(imageToUploadUri)
            }
            R.id.ivUserFrontPic -> {
                selectedImage.userSelfieImages = imageToUploadUri?.path!!
                ivUserImage.visibility = View.GONE
                ivUserFrontPic.setImageURI(imageToUploadUri)
            }
        }
    }

    override fun agnetKycDone() {
        val response = doRetriveModel().getLoginDomain().agentKuycResponse
        if (response.status == Constants.ApiResponse.RES_SUCCESS) {
            showToast(response.message)
            startActivity(Intent(getViewActivity(), LoginActivity::class.java))
            finishAffinity()
        } else {
            showError(response.message)
        }
    }

    override fun onKarzaToken(it: GenerateTokenFaceResponse) {
        /*if (it.result.success) {
            karzaToken = it.result.data.karzaToken
            val intent = Intent(
                getViewActivity(),
                AadharActivity::class.java
            )
            intent.putExtra("ENV", "test")
            intent.putExtra("KARZA-TOKEN", karzaToken)
            intent.putExtra("CLIENT", "okycapp")
            startActivityForResult(intent, 100)
        }*/
    }

    override fun onOkycAdded(it: RegOtpResponse) {
        /*if (it.status == Constants.ApiResponse.RES_SUCCESS) {
            isValidPanNumber = true

        } else {
            isValidPanNumber = false

            Utils.showAlert(getViewActivity(), it.message, View.OnClickListener { })
        }*/
    }

    override fun onVerifyToken(it: VerifyPancardResponse) {

    }

    private suspend fun compressFile(mTempFile: File):Uri{
        return withContext(Dispatchers.IO) {
            val file = File(mTempFile.path)
            val imageCompress = ImageCompress(getViewActivity(), file.path)
            imageCompress.compressImage()
            val uri = Uri.fromFile(file)
            val file_size = Integer.parseInt((file.length() / 1024).toString())
            Log.e(TAG, "file_size AFTER $file_size")
            return@withContext uri
        }
    }

}
