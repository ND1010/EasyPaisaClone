package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class FingPayICICIAepsTransactionRequest(
    @SerializedName("adhaarNumber")
    var adhaarNumber: String = "",
    @SerializedName("bankId")
    var bankId: String = "",
    @SerializedName("ip")
    var ip: String = "",
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = "",
    @SerializedName("biodata")
    var biodata: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("agree")
    var agree: Boolean = true,
    @SerializedName("mobileNumber")
    var mobileNumber: String = "",
    @SerializedName("device")
    var device: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("transactionType")
    var transactionType: String = "",
    @SerializedName("user_id")
    var userId: String = "",
    @SerializedName("transactionAmount")
    var transactionAmount: String = ""
)