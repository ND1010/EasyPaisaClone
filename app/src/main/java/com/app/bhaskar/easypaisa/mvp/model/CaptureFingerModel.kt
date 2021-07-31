package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.CaptureFingerDomain
import com.app.bhaskar.easypaisa.request_model.*
import com.pa.baseframework.baseview.BaseViewModel

class CaptureFingerModel: BaseViewModel {

    private var domain : CaptureFingerDomain
    private var aepsRequest = AepsRequest()
    private var fingPayICICIAepsTransactionRequest:FingPayICICIAepsTransactionRequest
    private var sendOtpEkycRequest:SendOtpEkycRequest
    private var verifyOtpEkycRequest: VerifyOtpEkycRequest
    private var ekycAuthenticationRequest: EkycAuthenticationRequest

    constructor(mContext: Context):super(mContext){
        domain = CaptureFingerDomain()
        fingPayICICIAepsTransactionRequest = FingPayICICIAepsTransactionRequest()
        sendOtpEkycRequest = SendOtpEkycRequest()
        verifyOtpEkycRequest = VerifyOtpEkycRequest()
        ekycAuthenticationRequest = EkycAuthenticationRequest()
    }

    fun getAepsRequest():AepsRequest{
        return aepsRequest
    }

    fun getEkycAuthenticationRequest():EkycAuthenticationRequest{
        return ekycAuthenticationRequest
    }

    fun getVerifyOtpEkycRequest():VerifyOtpEkycRequest{
        return verifyOtpEkycRequest
    }

    fun getFingPayAepsRequest():FingPayICICIAepsTransactionRequest{
        return fingPayICICIAepsTransactionRequest
    }

    fun getSendOtpRequest():SendOtpEkycRequest{
        return sendOtpEkycRequest
    }

    fun getCaptureDomain():CaptureFingerDomain{
        return domain
    }
}