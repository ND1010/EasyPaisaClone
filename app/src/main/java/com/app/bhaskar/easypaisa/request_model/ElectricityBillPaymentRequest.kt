package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class ElectricityBillPaymentRequest(
    @SerializedName("amount")
    var amount: String = "",
    @SerializedName("number")
    var number: String = "",
    @SerializedName("provider")
    var provider: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("transactionType")
    var transactionType: String = "",
    @SerializedName("user_id")
    var userId: String = "",
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = ""
)