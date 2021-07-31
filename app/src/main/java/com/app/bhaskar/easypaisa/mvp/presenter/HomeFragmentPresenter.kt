package com.app.bhaskar.easypaisa.mvp.presenter

import com.app.bhaskar.easypaisa.model.HomeData
import com.app.bhaskar.easypaisa.mvp.model.HomeFragmentModel
import com.app.bhaskar.easypaisa.request_model.SearchRemitter
import com.app.bhaskar.easypaisa.response_model.GenerateTokenFaceResponse
import com.app.bhaskar.easypaisa.response_model.UserRequiredDataResponse
import com.pa.baseframework.baseview.BasePresenter
import com.pa.baseframework.baseview.BaseView
import java.util.ArrayList


interface HomeFragmentPresenter : BasePresenter {
    fun doGetHomeList(): ArrayList<HomeData>
    fun goGetMainWalletBalance()
    fun proceedAction(it: HomeData.Servi)
    fun getUserRequiredData()
    fun gotoAgentKycScreen()
    fun searchRemitter(it: SearchRemitter)
    fun doVerifyRemitter()
    fun sendOtp()
    fun gotoUpdatePassword()
    fun doLogout()
    fun gotoAgenteKycScreen()

    interface HomeView : BaseView {
        fun doRetriveModel(): HomeFragmentModel
        fun onWalletBalance()
        fun onUserData()
        fun initiateAtmTransaction(it: Int)
        fun openSearchUserForDmt()
        fun onRemitterVerify()
        fun onVerifyRemitter()
        fun onGenerateOtp()
        fun onAppLogout()
        fun getUserData(): UserRequiredDataResponse?
    }
}