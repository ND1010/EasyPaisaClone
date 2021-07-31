package com.app.bhaskar.easypaisa.ui.activity

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.mvp.presenter.HomeActivityPresenterImpl
import com.app.bhaskar.easypaisa.mvp.presenter.HomePresenter
import com.app.bhaskar.easypaisa.ui.adapter.BottomNavigationAdapter
import com.app.bhaskar.easypaisa.ui.listener.OnUserLocationListener
import com.app.bhaskar.easypaisa.ui.service.GoogleFuesedLocationService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pa.baseframework.baseview.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject


class HomeActivity : BaseActivity(), HomePresenter.HomeView,
    BottomNavigationView.OnNavigationItemSelectedListener {


    companion object {
        const val TAG = "HomeActivity"
    }

    @Inject
    lateinit var presenter: HomeActivityPresenterImpl
    private lateinit var bottomNavigationAdapterHome: BottomNavigationAdapter
    private lateinit var googleFusedLocation : GoogleFuesedLocationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupWindowAnimations()
        presenter = HomeActivityPresenterImpl(this)
        EasyPaisaApp.component.inject(presenter)
        initView()

        googleFusedLocation = GoogleFuesedLocationService(getViewActivity(),object : OnUserLocationListener{
            override fun onUserLocationSuccess(location: Location) {

            }

            override fun onUserLocationFail(failMessage: String) {
            }

        },false)
        googleFusedLocation.accessUserCurrentLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        EasyPaisaApp.removeUserLatLong()
    }

    private fun initView() {
        bottomNavigationAdapterHome =
            BottomNavigationAdapter(
                getViewActivity(),
                supportFragmentManager,
                presenter.getFragmentList())
        viewPagerDashboard.adapter = bottomNavigationAdapterHome
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        viewPagerDashboard.offscreenPageLimit = 4
    }

    override fun getViewActivity(): Activity {
        return this@HomeActivity
    }

    override fun onNetworkStateChange(isConnect: Boolean) {

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                if (viewPagerDashboard.currentItem != 0) {
                    viewPagerDashboard.setCurrentItem(0, true)
                }
            }
            R.id.nav_wallet -> {
                if (viewPagerDashboard.currentItem != 1) {
                    viewPagerDashboard.setCurrentItem(1, true)
                    bottomNavigationAdapterHome.notifyDataSetChanged()
                }
            }
            R.id.nav_account -> {
                if (viewPagerDashboard.currentItem != 2) {
                    viewPagerDashboard.setCurrentItem(2, true)
                    bottomNavigationAdapterHome.notifyDataSetChanged()
                }
            }
            R.id.nav_transaction -> {
                if (viewPagerDashboard.currentItem != 3) {
                    viewPagerDashboard.setCurrentItem(3, true)
                    bottomNavigationAdapterHome.notifyDataSetChanged()
                }
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            3030 -> when (resultCode) {
                Activity.RESULT_OK -> {
                    /**
                     * User agreed to make required location settings changes.
                     * */
                    //googleFusedLocation.accessUserCurrentLocation()
                    googleFusedLocation.getUserLocation()
                }
                Activity.RESULT_CANCELED -> {
                    /**
                     * User chose not to make required location settings changes.
                     * */
                }
            }
        }
    }


}
