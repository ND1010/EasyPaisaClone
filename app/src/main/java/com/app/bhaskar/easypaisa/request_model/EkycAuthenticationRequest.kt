package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class EkycAuthenticationRequest(
    @SerializedName("biodata")
    var biodata: String = "",
    @SerializedName("encodeFPTxnId")
    var encodeFPTxnId: String = "",
    @SerializedName("primaryKeyId")
    var primaryKeyId: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("transactionType")
    var transactionType: String = "",
    @SerializedName("user_id")
    var userId: String = ""
)