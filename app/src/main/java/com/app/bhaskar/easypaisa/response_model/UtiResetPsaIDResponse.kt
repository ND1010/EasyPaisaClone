package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class UtiResetPsaIDResponse(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: String = ""
)