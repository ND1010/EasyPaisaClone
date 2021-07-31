package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class GenerateOtpRequest(
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("user_id")
    var userId: String = ""
)