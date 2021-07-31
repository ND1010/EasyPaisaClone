package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.AepsActivityDomain
import com.app.bhaskar.easypaisa.mvp.domain.LoginActivityDomain
import com.app.bhaskar.easypaisa.mvp.domain.MobileRechargeDomain
import com.app.bhaskar.easypaisa.request_model.*
import com.pa.baseframework.baseview.BaseViewModel

class MobileRechargeModel: BaseViewModel {

    private var domain : MobileRechargeDomain
    private var mobileRechargeProviderRequest:GetMobileRechargeProviderRequest
    private var operatorByMobile: GetMobileOperatorFromMobile
    private var mobileRechargeRequest: MobileRechargeRequest

    constructor(mContext: Context):super(mContext){
        domain = MobileRechargeDomain()
        mobileRechargeProviderRequest =GetMobileRechargeProviderRequest()
        operatorByMobile =GetMobileOperatorFromMobile()
        mobileRechargeRequest = MobileRechargeRequest()
    }

    fun getMobileRechargeRequest():MobileRechargeRequest{
        return mobileRechargeRequest
    }

    fun getMobilerProviderRequest():GetMobileRechargeProviderRequest{
        return mobileRechargeProviderRequest
    }

    fun getMonileOperatorFromMobile():GetMobileOperatorFromMobile{
        return operatorByMobile
    }

    fun getDomain():MobileRechargeDomain{
        return domain
    }
}