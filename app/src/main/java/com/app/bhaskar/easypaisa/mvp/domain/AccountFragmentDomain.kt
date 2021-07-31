package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.request_model.LogoutResponse
import com.app.bhaskar.easypaisa.response_model.UpdatePasswordResponse

data class AccountFragmentDomain(val updatePasswordResponse: UpdatePasswordResponse,var logoutResponse: LogoutResponse) {
    constructor():this(UpdatePasswordResponse(),LogoutResponse())
}