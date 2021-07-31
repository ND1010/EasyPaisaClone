package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.MobileRechargePlanResponse

data class MobileRechargePlanDomain(var mobileRechargePlanResponse: MobileRechargePlanResponse) {
    constructor():this(MobileRechargePlanResponse())
}