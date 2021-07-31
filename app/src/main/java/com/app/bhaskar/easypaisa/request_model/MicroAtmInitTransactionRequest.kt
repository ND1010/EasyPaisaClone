package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class MicroAtmInitTransactionRequest(
    @SerializedName("amount")
    var amount: String = "",
    @SerializedName("imei")
    var imei: String = "",
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("remark")
    var remark: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("transactionType")
    var transactionType: String = "",
    @SerializedName("user_id")
    var userId: String = "",
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = ""
)