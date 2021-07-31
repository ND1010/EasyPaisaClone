package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.HomeFragmentDomain
import com.app.bhaskar.easypaisa.request_model.GenerateOtpRequest
import com.app.bhaskar.easypaisa.request_model.UserRequiredDataRequest
import com.app.bhaskar.easypaisa.request_model.VerifyRemitterOtpRequest
import com.app.bhaskar.easypaisa.request_model.WalletBalanceRequest
import com.pa.baseframework.baseview.BaseViewModel

class HomeFragmentModel: BaseViewModel {
    private var walletBalanceReq : WalletBalanceRequest
    private var uesrRequiredDataRequest: UserRequiredDataRequest
    private var domain : HomeFragmentDomain
    private var verifyRemitterOtpRequest = VerifyRemitterOtpRequest()
    private var generateOtpRequest = GenerateOtpRequest()
    private var logoutReq : WalletBalanceRequest

    constructor(mContext: Context):super(mContext){
        walletBalanceReq = WalletBalanceRequest()
        uesrRequiredDataRequest= UserRequiredDataRequest()
        domain = HomeFragmentDomain()
        generateOtpRequest = GenerateOtpRequest()
        verifyRemitterOtpRequest = VerifyRemitterOtpRequest()
        logoutReq =WalletBalanceRequest()
    }
    fun getVerifyRemitterReq(): VerifyRemitterOtpRequest {
        return verifyRemitterOtpRequest
    }
    fun getWalletBalanceReq():WalletBalanceRequest{
        return  walletBalanceReq
    }
    fun getLoginDomain():HomeFragmentDomain{
        return domain
    }
        fun getUserReuiredDataRequest():UserRequiredDataRequest{
        return uesrRequiredDataRequest
    }
    fun getGenerateOtpReq(): GenerateOtpRequest {
        return generateOtpRequest
    }
    fun getLogoutReq():WalletBalanceRequest{
        return logoutReq
    }
}