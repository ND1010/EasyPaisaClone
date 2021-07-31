package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class WalletLoadRequest(
    @SerializedName("amount")
    var amount: String = "",
    @SerializedName("fundbank_id")
    var fundbankId: String = "",
    @SerializedName("paydate")
    var paydate: String = "",
    @SerializedName("paymode")
    var paymode: String = "",
    @SerializedName("ref_no")
    var refNo: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("user_id")
    var userId: String = "",
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = ""
)