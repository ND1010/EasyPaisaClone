package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.LoginActivityDomain
import com.app.bhaskar.easypaisa.request_model.LoginRequest
import com.app.bhaskar.easypaisa.request_model.PasswordResetToeknRequest
import com.pa.baseframework.baseview.BaseViewModel

class LoginActivityModel: BaseViewModel {
    private var loginRequest : LoginRequest
    private var sendPasswordTokenRequest : PasswordResetToeknRequest

    private var domain : LoginActivityDomain

    constructor(mContext: Context):super(mContext){
        loginRequest = LoginRequest()
        sendPasswordTokenRequest = PasswordResetToeknRequest()
        domain = LoginActivityDomain()
    }

    fun getLoginRequest():LoginRequest{
        return  loginRequest
    }
    fun getLoginDomain():LoginActivityDomain{
        return domain
    }
    fun getPasswordTokenRequest():PasswordResetToeknRequest{
        return sendPasswordTokenRequest
    }
}