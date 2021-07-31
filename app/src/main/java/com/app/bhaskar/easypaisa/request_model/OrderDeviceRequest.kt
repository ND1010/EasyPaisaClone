package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class OrderDeviceRequest(
    @SerializedName("pantokens")
    var pantokens: String = "",
    @SerializedName("provider")
    var provider: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("transactionType")
    var transactionType: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("user_id")
    var userId: String = "",
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = ""
)