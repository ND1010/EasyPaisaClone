package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class FingPayAepsTxnResponse(
    @SerializedName("amount")
    var amount: String = "",
    @SerializedName("balance")
    var balance: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("ministatementtype1")
    var ministatementtype1: String = "",
    @SerializedName("ministatementtype2")
    var ministatementtype2: String = "",
    @SerializedName("rrn")
    var rrn: String = "",
    @SerializedName("statement1")
    var statement1: List<Statement1> = listOf(),
    @SerializedName("statement2")
    var statement2: List<String> = listOf(),
    @SerializedName("status")
    var status: String = "",
    @SerializedName("txnid")
    var txnid: String? = ""
) {
    data class Statement1(
        @SerializedName("amount")
        var amount: String = "",
        @SerializedName("date")
        var date: String = "",
        @SerializedName("narration")
        var narration: String = "",
        @SerializedName("txnType")
        var txnType: String = ""
    )
}