package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class DepositeGetOtpRequest(
    @SerializedName("accountNumber")
    var accountNumber: String = "",
    @SerializedName("iinno")
    var iinno: String = "",
    @SerializedName("mobileNumber")
    var mobileNumber: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("transactionAmount")
    var transactionAmount: String = "",
    @SerializedName("transactionType")
    var transactionType: String = "",
    @SerializedName("user_id")
    var userId: String = "",
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = ""
)