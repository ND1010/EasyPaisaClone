package com.app.bhaskar.easypaisa.mvp.presenter

import androidx.fragment.app.Fragment
import com.app.bhaskar.easypaisa.model.HomeData
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView
import java.util.ArrayList


interface HomePresenter : BasePresenter {
    fun doGetHomeList(): ArrayList<HomeData>
    fun getFragmentList(): ArrayList<Fragment>

    interface HomeView : BaseView {

    }
}