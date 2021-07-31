package com.app.bhaskar.easypaisa.ui.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.response_model.UserLocationLatLng
import com.app.bhaskar.easypaisa.ui.activity.HomeActivity
import com.app.bhaskar.easypaisa.ui.dialog.DialogAlert
import com.app.bhaskar.easypaisa.ui.listener.OnUserLocationListener
import com.app.bhaskar.easypaisa.utils.Constants
import com.app.bhaskar.easypaisa.utils.Utils
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task


class GoogleFuesedLocationService() {

    private val TAG = "GoogleFusedLocation"
    var needToShowDialog = false
    private val permissions: ArrayList<String> = ArrayList()
    lateinit var mContext: Context
    private lateinit var onUserLocationListener: OnUserLocationListener
    private var permissionsToRequest: ArrayList<String>? = null

    //A client that handles connection / connection failures for Google locations
    // (changed from play-services 11.0.0)
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    /**
     * Provide callbacks for location events.
     */
    private var mLocationCallback: LocationCallback? = null

    /**
     * An object representing the current location
     */
    private var mCurrentLocation: Location? = null
    private var locationResponse: Task<LocationSettingsResponse>? = null

    /**
     * FusedLocationProviderApi Save request parameters
     */
    private var mLocationRequest: LocationRequest? = null

    constructor(
        mContext: Context,
        onUserLocationListener: OnUserLocationListener,
        needToShowDialog: Boolean
    ) : this() {
        this.needToShowDialog = needToShowDialog
        this.mContext = mContext
        this.onUserLocationListener = onUserLocationListener

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult?) {
                super.onLocationResult(result)
                Utils.hideProgressDialog()
                result?.let {
                    mCurrentLocation = it.locations[0]
                    Log.e("Location(Lat)==", "" + mCurrentLocation?.latitude)
                    Log.e("Location(Long)==", "" + mCurrentLocation?.longitude)
                    val userLatlng = UserLocationLatLng()
                    userLatlng.latitude = mCurrentLocation!!.latitude
                    userLatlng.longitude = mCurrentLocation!!.longitude
                    userLatlng.isMockLocation = mCurrentLocation!!.isFromMockProvider
                    EasyPaisaApp.setUserLatLng(userLatlng)
                    onUserLocationListener.onUserLocationSuccess(mCurrentLocation!!)
                    stopLocationUpdates()
                }
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                super.onLocationAvailability(locationAvailability)
                Log.e(HomeActivity.TAG, "onLocationAvailability: ")
            }
        }

        val mSettingsClient = LocationServices.getSettingsClient(mContext)
        mLocationRequest = LocationRequest()
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val mLocationSettingsRequest = builder.build()
        locationResponse =
            mSettingsClient.checkLocationSettings(mLocationSettingsRequest)

    }


    fun getCurrentUserLocation() {
        locationResponse?.addOnSuccessListener {
            Log.e(TAG, "addOnSuccessListener")
            if (ActivityCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                return@addOnSuccessListener
            }
            //Location api call
            getUserLocation()
        }
        locationResponse?.addOnFailureListener(mContext as Activity) { e ->
            Log.e(TAG, "result_addOnFailureListener")
            val statusCode = (e as ApiException).statusCode
            when (statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Log.e(TAG, "getCurrentUserLocation_RESOLUTION_REQUIRED")
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    val errorMessage = "Check location setting"
                    Log.e("onFailure", errorMessage)
                }
            }

            if (e is ResolvableApiException) {
                try {
                    if (!needToShowDialog) {
                        val resolvable: ResolvableApiException = e
                        resolvable.startResolutionForResult(mContext as Activity, 3030)
                        return@addOnFailureListener
                    }
                    if (!(mContext as Activity).isFinishing) {
                        val dialogLocationAccess = DialogAlert(mContext)
                        dialogLocationAccess.setMessage("Please turn on GPS location to access location information for detect suspicious activity and keep your account safe.")
                        dialogLocationAccess.setPositiveButton("Turn On", View.OnClickListener {
                            needToShowDialog = false
                            getCurrentUserLocation()
                        })
                        dialogLocationAccess.setNegativeButton("Cancel", View.OnClickListener {  })
                        dialogLocationAccess.setCancelable(false)
                        dialogLocationAccess.show()

                        /*val dialogLocationAccess = DialogAlert() {
                            val resolvable: ResolvableApiException = e
                            resolvable.startResolutionForResult(mContext as Activity, AppConstants.GPS_ENABLED)
                        }
                        dialogLocationAccess.showDialog()
                        Toast.makeText(mContext, "Need location", Toast.LENGTH_SHORT).show()*/
                    }
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        Utils.showProgressDialog(mContext, "Fetching GPS location...")
//        if (needToShowDialog)

        Log.e(TAG, "Fetching GPS location: ")
        mFusedLocationClient?.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            null
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun accessUserCurrentLocation() {
        if (hasLocationPermission()) {
            getCurrentUserLocation()
        }
    }

    fun hasLocationPermission(): Boolean {

        // we add permissions we need to request location of the users
        permissions.clear()
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)

        permissionsToRequest = permissionsToRequest(permissions)
        if (permissionsToRequest!!.size > 0) {
            (mContext as Activity).requestPermissions(
                permissionsToRequest!!.toTypedArray(),
                Constants.UI.PERMISSIONS_CURRENT_LOCATION
            )
            return false
        }
        return true
    }

    fun permissionsToRequest(wantedPermissions: ArrayList<String>): ArrayList<String> {
        val result: ArrayList<String> = ArrayList()
        for (perm in wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm)
            }
        }
        return result
    }

    private fun hasPermission(permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    private fun stopLocationUpdates() {
        mFusedLocationClient?.let {
            it.removeLocationUpdates(mLocationCallback)
        }
    }

}