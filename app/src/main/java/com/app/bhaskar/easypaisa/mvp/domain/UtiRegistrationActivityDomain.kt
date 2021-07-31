package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.UtiRegistrationResponse

data class UtiRegistrationActivityDomain(var utiRegistrationResponse: UtiRegistrationResponse) {
    constructor():this(UtiRegistrationResponse())
}