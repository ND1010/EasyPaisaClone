package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class MobileOperatorFromMobile(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("provider")
    var provider: Int = 0,
    @SerializedName("status")
    var status: String = ""
)