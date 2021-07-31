package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class AepsTransactionRequest(
    @SerializedName("aeps")
    var aeps: String = "",
    @SerializedName("ip")
    var ip: String = "",
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = "",
    @SerializedName("mobileNumber")
    var mobileNumber: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("transactionAmount")
    var transactionAmount: String = "",
    @SerializedName("transactionType")
    var transactionType: String = "",
    @SerializedName("user_id")
    var userId: String = ""
)