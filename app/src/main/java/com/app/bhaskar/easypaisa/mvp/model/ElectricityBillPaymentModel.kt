package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.ElectricityBillPaymentDomain
import com.app.bhaskar.easypaisa.request_model.ElectricityBillPaymentRequest
import com.app.bhaskar.easypaisa.request_model.ElectricityBoardRequest
import com.app.bhaskar.easypaisa.request_model.ElectricityStateRequest
import com.app.bhaskar.easypaisa.request_model.FetchElBillDetailRequest
import com.pa.baseframework.baseview.BaseViewModel

class ElectricityBillPaymentModel : BaseViewModel {

    private var domain: ElectricityBillPaymentDomain
    private var electricityStateRequest: ElectricityStateRequest
    private var electricityBoardRequest: ElectricityBoardRequest
    private var fetchElBillDetailRequest: FetchElBillDetailRequest
    private var electricityBillPaymentRequest: ElectricityBillPaymentRequest

    constructor(mContext: Context) : super(mContext) {
        domain = ElectricityBillPaymentDomain()
        electricityBoardRequest = ElectricityBoardRequest()
        electricityStateRequest = ElectricityStateRequest()
        fetchElBillDetailRequest = FetchElBillDetailRequest()
        electricityBillPaymentRequest = ElectricityBillPaymentRequest()
    }

    fun getElectricityBoardRequest(): ElectricityBoardRequest {
        return electricityBoardRequest
    }
    fun getElectricityBillPaymentRequest(): ElectricityBillPaymentRequest{
        return electricityBillPaymentRequest
    }
    fun getFetchElBillDetailRequest(): FetchElBillDetailRequest{
        return fetchElBillDetailRequest
    }

    fun getElectricityStateRequest(): ElectricityStateRequest {
        return electricityStateRequest
    }

    fun getDomain(): ElectricityBillPaymentDomain {
        return domain
    }
}