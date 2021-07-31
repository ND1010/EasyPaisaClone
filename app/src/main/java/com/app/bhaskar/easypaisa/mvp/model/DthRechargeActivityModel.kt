package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.DthRechargeActivityDomain
import com.app.bhaskar.easypaisa.request_model.ElectricityBillPaymentRequest
import com.app.bhaskar.easypaisa.request_model.GetMobileRechargeProviderRequest
import com.pa.baseframework.baseview.BaseViewModel

class DthRechargeActivityModel : BaseViewModel {

    private var domain: DthRechargeActivityDomain
    private var providerRequest:  GetMobileRechargeProviderRequest
    private var electricityBillPaymentRequest:  ElectricityBillPaymentRequest


    constructor(mContext: Context) : super(mContext) {
        domain = DthRechargeActivityDomain()
        providerRequest = GetMobileRechargeProviderRequest()
        electricityBillPaymentRequest = ElectricityBillPaymentRequest()
    }

    fun getGetMobileRechargeProviderRequest(): GetMobileRechargeProviderRequest {
        return providerRequest
    }

    fun getDomain(): DthRechargeActivityDomain {
        return domain
    }

    fun getElectricityBillPaymentRequest(): ElectricityBillPaymentRequest {
        return electricityBillPaymentRequest
    }
}