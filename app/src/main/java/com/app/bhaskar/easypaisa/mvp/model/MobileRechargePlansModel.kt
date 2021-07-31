package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.MobileRechargePlanDomain
import com.app.bhaskar.easypaisa.request_model.MobileRechargePlanRequest
import com.pa.baseframework.baseview.BaseViewModel

class MobileRechargePlansModel: BaseViewModel {

    private var domain : MobileRechargePlanDomain
    private var mobileRechargePlanReq= MobileRechargePlanRequest()

    constructor(mContext: Context):super(mContext){
        domain = MobileRechargePlanDomain()
    }

    fun getMobilRechargePlanRequest():MobileRechargePlanRequest{
        return mobileRechargePlanReq
    }

    fun getDomain():MobileRechargePlanDomain{
        return domain
    }
}