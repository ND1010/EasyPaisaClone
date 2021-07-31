package com.app.bhaskar.easypaisa.mvp.domain

import com.app.bhaskar.easypaisa.response_model.UpdatePasswordResponse

data class ChangePassActivityDomain(var updatePasswordResponse: UpdatePasswordResponse) {
    constructor():this(UpdatePasswordResponse())
}