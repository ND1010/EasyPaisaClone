/**
 *Created by dhruv on 27-06-2020.
 */
package com.app.bhaskar.easypaisa.ui.listener

import android.location.Location

interface OnUserLocationListener {
    fun onUserLocationSuccess(location:Location)
    fun onUserLocationFail(failMessage:String)
}