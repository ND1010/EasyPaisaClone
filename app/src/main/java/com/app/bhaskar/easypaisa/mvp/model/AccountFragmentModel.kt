package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.AccountFragmentDomain
import com.app.bhaskar.easypaisa.mvp.domain.AepsActivityDomain
import com.app.bhaskar.easypaisa.mvp.domain.LoginActivityDomain
import com.app.bhaskar.easypaisa.request_model.*
import com.pa.baseframework.baseview.BaseViewModel

class AccountFragmentModel: BaseViewModel {

    private var domain : AccountFragmentDomain
    private var changePassRequest : ChangePasswordRequest
    private var logoutReq : WalletBalanceRequest

    constructor(mContext: Context):super(mContext){
        domain = AccountFragmentDomain()
        changePassRequest = ChangePasswordRequest()
        logoutReq = WalletBalanceRequest()
    }

    fun getChangePassRequest():ChangePasswordRequest{
        return changePassRequest
    }

    fun getDomain():AccountFragmentDomain{
        return domain
    }

    fun getLogoutReq():WalletBalanceRequest{
        return logoutReq
    }
}