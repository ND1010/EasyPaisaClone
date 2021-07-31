package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class VerifyOtpEkycRequest(
    @SerializedName("encodeFPTxnId")
    var encodeFPTxnId: String = "",
    @SerializedName("otp")
    var otp: String = "",
    @SerializedName("primaryKeyId")
    var primaryKeyId: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("transactionType")
    var transactionType: String = "",
    @SerializedName("user_id")
    var userId: String = ""
)