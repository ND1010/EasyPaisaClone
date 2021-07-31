package com.app.bhaskar.easypaisa

import android.app.Activity
import android.os.Bundle
import com.pa.baseframework.baseview.BaseActivity

class MainActivity : BaseActivity() {

    companion object{
        const val TAG ="MainActivity"
    }

    override fun onNetworkStateChange(isConnect: Boolean) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_easypaisa)
    }

    override fun getViewActivity(): Activity {
        return this@MainActivity
    }
}
