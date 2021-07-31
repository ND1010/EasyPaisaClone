package com.app.bhaskar.easypaisa.mvp.model

import android.content.Context
import com.app.bhaskar.easypaisa.mvp.domain.AepsActivityDomain
import com.app.bhaskar.easypaisa.mvp.domain.AgentKycActivityDomain
import com.app.bhaskar.easypaisa.mvp.domain.LoginActivityDomain
import com.app.bhaskar.easypaisa.request_model.AgentKycRequest
import com.app.bhaskar.easypaisa.request_model.LoginRequest
import com.app.bhaskar.easypaisa.request_model.PasswordResetToeknRequest
import com.pa.baseframework.baseview.BaseViewModel

class AgentKycActivityModel: BaseViewModel {

    private var agentKycRequest:AgentKycRequest
    private var domain : AgentKycActivityDomain

    constructor(mContext: Context):super(mContext){
        domain = AgentKycActivityDomain()
        agentKycRequest = AgentKycRequest()
    }

    fun getAgentKycRequest():AgentKycRequest{
        return agentKycRequest
    }
    fun getLoginDomain():AgentKycActivityDomain{
        return domain
    }
}