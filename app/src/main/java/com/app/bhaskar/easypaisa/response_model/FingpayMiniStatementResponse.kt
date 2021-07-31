package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class FingpayMiniStatementResponse(
    @SerializedName("amount")
    var amount: String = "",
    @SerializedName("balance")
    var balance: String = "",
    @SerializedName("bank")
    var bank: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("rrn")
    var rrn: String = "",
    @SerializedName("statement")
    var statement: ArrayList<Statement> = ArrayList(),
    @SerializedName("status")
    var status: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("transactionType")
    var transactionType: String = "",
    @SerializedName("txnid")
    var txnid: String = ""
) {
    data class Statement(
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