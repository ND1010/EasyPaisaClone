package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class RegisterOtpRequest(
    @SerializedName("mobile")
    var mobile: String = ""
)