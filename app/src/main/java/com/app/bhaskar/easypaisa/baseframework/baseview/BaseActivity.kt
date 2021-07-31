package com.pa.baseframework.baseview

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.TransitionHelper
import com.app.bhaskar.easypaisa.utils.Utils
import com.app.bhaskar.easypaisa.utils.Utils.Companion.isNetworkConnected
import com.google.android.material.snackbar.Snackbar


abstract class BaseActivity : AppCompatActivity(), BaseView {
    var receiver: BroadcastReceiver? = null
    private var intentFilter: IntentFilter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setupWindowAnimations()

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                onNetworkStateChange(isNetworkConnected(this@BaseActivity))
                Log.e("status", isNetworkConnected(this@BaseActivity).toString())
            }
        }
        intentFilter = IntentFilter()
        intentFilter!!.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
    }

    fun setupWindowAnimations() {
        val slide = TransitionInflater.from(this).inflateTransition(R.transition.activity_fade)
        window.enterTransition = slide
    }

    override fun onPause() {
        this.unregisterReceiver(receiver)
        super.onPause()
    }

    override fun onResume() {
        this.registerReceiver(receiver, intentFilter)
        super.onResume()
    }

    override fun showError(message: String) {
        Utils.showSnackBar(
            getViewActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG,
            true,
            getViewActivity()
        )
    }

    override fun showProgress() {
        Utils.showProgressDialog(getViewActivity(), "")
    }

    override fun showSnackBar(message: String) {
        Utils.showSnackBar(
            getViewActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG,
            false,
            getViewActivity()
        )
    }

    override fun showToast(message: String) {
        Utils.showToast(message, getViewActivity())
    }

    override fun hideProgress() {
        Utils.hideProgressDialog()
    }

    override fun showProgress(mesg: String) {
        Utils.showProgressDialog(getViewActivity(), mesg)
    }
    /*fun getNavigationController() : NavController {
        return  NavHostFragment.findNavController(
            supportFragmentManager.findFragmentById(
                R.id.nav_host_fragment
            )!!
        )
    }*/

    /*open fun transitionTo(i: Intent?) {
        val pairs =
            TransitionHelper.createSafeTransitionParticipants(this, true)
        val transitionActivityOptions =
            ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs)
        startActivity(i, transitionActivityOptions.toBundle())
    }*/
}
