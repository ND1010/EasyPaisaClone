package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.AgentKycResponse

data class AgentKycActivityDomain(var agentKuycResponse:AgentKycResponse) {
    constructor():this(AgentKycResponse())
}