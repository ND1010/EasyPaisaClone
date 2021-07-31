package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class MatmOnlineOrderDeviceUpdateRequest(
    @SerializedName("amount")
    var amount: String = "",
    @SerializedName("orderid")
    var orderid: String = "",
    @SerializedName("payid")
    var payid: String = "",
    @SerializedName("signature")
    var signature: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("transactionType")
    var transactionType: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("pantokens")
    var pantokens: String = "",
    @SerializedName("user_id")
    var userId: String = "",
    @SerializedName("status")
    var status: String = ""
)