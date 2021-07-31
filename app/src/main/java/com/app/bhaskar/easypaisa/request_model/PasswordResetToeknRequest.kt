package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class PasswordResetToeknRequest(
    @SerializedName("mobile")
    var mobile: String = ""
)