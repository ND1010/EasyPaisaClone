package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.*

data class MobileRechargeDomain(var mobileProviderResponse: RechargeProviderResponse, var mobileOperatorFromMobile: MobileOperatorFromMobile, var mobileRechargeResponse: MobileRechargeResponse) {
    constructor():this(RechargeProviderResponse(),MobileOperatorFromMobile(),MobileRechargeResponse())

}