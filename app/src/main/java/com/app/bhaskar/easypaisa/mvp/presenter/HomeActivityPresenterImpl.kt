package com.app.bhaskar.easypaisa.mvp.presenter

import androidx.fragment.app.Fragment
import com.app.bhaskar.easypaisa.R
import com.app.bhaskar.easypaisa.application.EasyPaisaApp
import com.app.bhaskar.easypaisa.model.HomeData
import com.app.bhaskar.easypaisa.repositories.EasyPaisaRepository
import com.app.bhaskar.easypaisa.ui.fragment.AccountFragment
import com.app.bhaskar.easypaisa.ui.fragment.HomeFragment
import com.app.bhaskar.easypaisa.ui.fragment.TransactionFragment
import com.app.bhaskar.easypaisa.ui.fragment.WalletFragment
import java.util.ArrayList
import javax.inject.Inject

class HomeActivityPresenterImpl(val view: HomePresenter.HomeView) : HomePresenter {

    private val mEventBus = EasyPaisaApp.getDefault()
    @Inject
    lateinit var repository: EasyPaisaRepository

    /*override fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest) {

        if (Utils.isNetworkConnected(view.getViewActivity())) {
            if (forgotPasswordRequest.email.trim().isEmpty()) {
                view.showError(view.getViewActivity().getString(R.string.label_enter_email_id))
            } else {
                view.showProgress()
                repository.forgotPassword(forgotPasswordRequest, {
                    view.hideProgress()
                    view.onForgotPassword(it!!)
                }, {
                    view.hideProgress()
                    view.showError(it!!.localizedMessage)
                })
            }
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }

    }

    override fun doSignin(signinRequest: SigninRequest) {
        if (Utils.isNetworkConnected(view.getViewActivity())) {
            if (signinRequest.email.trim().isEmpty()) {
                view.showError(view.getViewActivity().getString(R.string.label_enter_email_id))
            } else if (!Utils.isValidEmail(signinRequest.email.trim())) {
                view.showError(view.getViewActivity().getString(R.string.validation_Please_enter_valid_email_id))
            } else if (signinRequest.password.trim().isEmpty()) {
                view.showError(view.getViewActivity().getString(R.string.label_enter_password))
            } else if (signinRequest.password.trim().length < 6) {
                view.showError(view.getViewActivity().getString(R.string.password_should_be_more_then_6_character))
            } else {
                view.showProgress()
                repository.doLogin(signinRequest, {
                    view.hideProgress()
                    view.onLoginSuccessful(it!!)
                }, {
                    view.hideProgress()
                    view.showError(it!!.localizedMessage)
                })
            }
        } else {
            view.showError(view.getViewActivity().getString(R.string.no_internet_message))
        }
    }*/


    override fun onError(message: String) {
    }

    override fun registerBus() {
        mEventBus.register(this)
    }

    override fun unRegisterBus() {
        mEventBus.unregister(this)
    }

    override fun doGetHomeList(): ArrayList<HomeData> {
        val arrayHome = ArrayList<HomeData>()

        //Banking services
        //Easypay  Yes
        val homeData1 = HomeData()
        homeData1.categoryName = "Banking Service"
        val serAepsY = HomeData.Servi()
        serAepsY.serviceIcon = R.drawable.ic_aeps_ybl
        serAepsY.serviceName = view.getViewActivity().getString(R.string.yes_aeps)

        //Easypay  ICICI
        val serAepsIF = HomeData.Servi()
        serAepsIF.serviceIcon = R.drawable.ic_aeps_icici
        serAepsIF.serviceName = view.getViewActivity().getString(R.string.yes_aeps_icici_ep)

        val serAepsF = HomeData.Servi()
        serAepsF.serviceIcon = R.drawable.ic_aeps_fino
        serAepsF.serviceName = view.getViewActivity().getString(R.string.fino_aeps)

        val serAepsI = HomeData.Servi()
        serAepsI.serviceIcon = R.drawable.ic_aeps_icici
        serAepsI.serviceName = view.getViewActivity().getString(R.string.icici_aeps)

        val serAepsM = HomeData.Servi()
        serAepsM.serviceIcon = R.drawable.ic_microatm
        serAepsM.serviceName = view.getViewActivity().getString(R.string.micro_atm)

        val serAepsMTfr = HomeData.Servi()
        serAepsMTfr.serviceIcon = R.drawable.ic_money_transfer
        serAepsMTfr.serviceName = view.getViewActivity().getString(R.string.money_transfer)

        homeData1.servi.add(serAepsY)
        homeData1.servi.add(serAepsIF)
        homeData1.servi.add(serAepsF)
        homeData1.servi.add(serAepsI)
        homeData1.servi.add(serAepsM)
        homeData1.servi.add(serAepsMTfr)

        //Bill Pay and Recharge

        val homeData2 = HomeData()
        homeData2.categoryName = "Bill Pay and Recharge"
        val serAepsEl = HomeData.Servi()
        serAepsEl.serviceIcon = R.drawable.ic_ele
        serAepsEl.serviceName = view.getViewActivity().getString(R.string.electricity)

        val serAepsmobile = HomeData.Servi()
        serAepsmobile.serviceIcon = R.drawable.ic_mobile
        serAepsmobile.serviceName = view.getViewActivity().getString(R.string.mobile)

        val serAepsdth = HomeData.Servi()
        serAepsdth.serviceIcon = R.drawable.ic_dth
        serAepsdth.serviceName = view.getViewActivity().getString(R.string.dth)

        val serAepsdataCard = HomeData.Servi()
        serAepsdataCard.serviceIcon = R.drawable.ic_datacard
        serAepsdataCard.serviceName = view.getViewActivity().getString(R.string.data_card)

        homeData2.servi.add(serAepsEl)
        homeData2.servi.add(serAepsmobile)
        homeData2.servi.add(serAepsdth)
        homeData2.servi.add(serAepsdataCard)

        //Other Service
        val homeData3 = HomeData()
        homeData3.categoryName = view.getViewActivity().getString(R.string.other_services)
        val serUtiPancard = HomeData.Servi()
        serUtiPancard.serviceIcon = R.drawable.ic_pancard
        serUtiPancard.serviceName = view.getViewActivity().getString(R.string.uti_pancard)

        val serInc = HomeData.Servi()
        serInc.serviceIcon = R.drawable.ic_insurance
        serInc.serviceName = view.getViewActivity().getString(R.string.insurance)
        homeData3.servi.add(serUtiPancard)
        homeData3.servi.add(serInc)

        //All service at home arraylist
        arrayHome.add(homeData1)
        arrayHome.add(homeData2)
        arrayHome.add(homeData3)

        return arrayHome

    }

    override fun getFragmentList(): ArrayList<Fragment> {
        val arrListFragment: ArrayList<Fragment> = ArrayList()
        arrListFragment.add(HomeFragment())
        arrListFragment.add(WalletFragment())
        arrListFragment.add(AccountFragment())
        arrListFragment.add(TransactionFragment())
        return arrListFragment
    }


}