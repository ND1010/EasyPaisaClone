package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class DmtTransactionResponse(
    @SerializedName("amount")
    var amount: String = "",
    @SerializedName("beneaccount")
    var beneaccount: String = "",
    @SerializedName("benebank")
    var benebank: String = "",
    @SerializedName("beneifsc")
    var beneifsc: String = "",
    @SerializedName("benename")
    var benename: String = "",
    @SerializedName("date")
    var date: String = "",
    @SerializedName("id")
    var id: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("utr")
    var utr: String = ""
)