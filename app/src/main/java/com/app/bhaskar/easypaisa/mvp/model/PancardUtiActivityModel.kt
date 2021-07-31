package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.AepsActivityDomain
import com.app.bhaskar.easypaisa.mvp.domain.LoginActivityDomain
import com.app.bhaskar.easypaisa.mvp.domain.PancardUtiActivityDomain
import com.app.bhaskar.easypaisa.request_model.*
import com.pa.baseframework.baseview.BaseViewModel

class PancardUtiActivityModel: BaseViewModel {

    private var domain : PancardUtiActivityDomain
    private var panTokenRequest : PanTokenRequest
    private var panTokenResetRequest: UtiPanTokenResetRequest
    private var orderDeviceRequest:OrderDeviceRequest
    private var matmOnlineOrderRequest :MatmOnlineOrderRequest
    private var matmOnlineOrderDeviceUpdateRequest :MatmOnlineOrderDeviceUpdateRequest

    constructor(mContext: Context):super(mContext){
        domain = PancardUtiActivityDomain()
        panTokenRequest =PanTokenRequest()
        panTokenResetRequest = UtiPanTokenResetRequest()
        orderDeviceRequest = OrderDeviceRequest()
        matmOnlineOrderRequest = MatmOnlineOrderRequest()
        matmOnlineOrderDeviceUpdateRequest = MatmOnlineOrderDeviceUpdateRequest()
    }

    fun getOrderDeviceOnlineMatm(): MatmOnlineOrderRequest{
        return matmOnlineOrderRequest
    }

    fun getMatmOnlineOrderDeviceUpdateRequest():MatmOnlineOrderDeviceUpdateRequest{
        return matmOnlineOrderDeviceUpdateRequest
    }

    fun getOrderDeviceReq() : OrderDeviceRequest {
        return orderDeviceRequest
    }
    fun getPanTokenRequest():PanTokenRequest{
        return panTokenRequest
    }

    fun getDomain():PancardUtiActivityDomain{
        return domain
    }

    fun getUtiPanTokenResetRequest():UtiPanTokenResetRequest{
        return panTokenResetRequest
    }
}