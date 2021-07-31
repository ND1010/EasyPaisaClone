package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class OtpVerificationRequest(
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("otp")
    var otp: String = "",
    @SerializedName("responsecode")
    var responsecode: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("user_id")
    var userId: String = ""
)