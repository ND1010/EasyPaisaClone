package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class DmtTransferRequest(
    @SerializedName("amount")
    var amount: String = "",
    @SerializedName("beneaccount")
    var beneaccount: String = "",
    @SerializedName("benebank")
    var benebank: String = "",
    @SerializedName("beneifsc")
    var beneifsc: String = "",
    @SerializedName("ip")
    var ip: String = "",
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = "",
    @SerializedName("benename")
    var benename: String = "",
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("user_id")
    var userId: String = ""
)