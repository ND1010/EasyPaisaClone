package com.pa.baseframework.baseview

import android.app.Activity
import androidx.fragment.app.Fragment
import com.app.bhaskar.easypaisa.utils.Utils
import com.google.android.material.snackbar.Snackbar

open class BaseFragment : Fragment(),BaseView  {
    override fun showProgress(mesg: String) {
        Utils.showProgressDialog(getViewActivity(),mesg)
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
        Utils.showToast(message,getViewActivity())
    }

    override fun hideProgress() {
        Utils.hideProgressDialog()
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

    override fun getViewActivity(): Activity {
        return  activity!!
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    /*fun getNavigationController() : NavController {
        return  NavHostFragment.findNavController(
            activity!!.supportFragmentManager.findFragmentById(
                R.id.nav_host_fragment
            )!!
        )
    }*/
}