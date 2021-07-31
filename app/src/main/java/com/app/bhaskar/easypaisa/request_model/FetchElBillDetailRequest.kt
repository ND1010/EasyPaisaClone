package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class FetchElBillDetailRequest(
    @SerializedName("number")
    var number: String = "",
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("provider")
    var provider: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("transactionType")
    var transactionType: String = "",
    @SerializedName("user_id")
    var userId: String = ""
)