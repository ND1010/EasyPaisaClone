package com.pa.baseframework.baseview

import android.app.Activity



interface BaseView {

    fun showProgress()

    fun showProgress(mesg:String)

    fun hideProgress()

    fun showToast(message: String)

    fun showError(message: String)

    fun showSnackBar(message: String)

    fun getViewActivity(): Activity

    fun onNetworkStateChange(isConnect: Boolean)

}