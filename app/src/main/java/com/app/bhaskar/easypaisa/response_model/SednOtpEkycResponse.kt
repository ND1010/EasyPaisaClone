package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class SednOtpEkycResponse(
    @SerializedName("encodeFPTxnId")
    var encodeFPTxnId: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("primaryKeyId")
    var primaryKeyId: Int = 0,
    @SerializedName("status")
    var status: String = ""
)