package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    @SerializedName("data")
    var `data`: List<Data> = listOf(),
    @SerializedName("pages")
    var pages: Int = 0,
    @SerializedName("statuscode")
    var statuscode: String = ""
) {
    data class Data(
        @SerializedName("amount")
        var amount: Double = 0.0,
        @SerializedName("api_id")
        var apiId: Int = 0,
        @SerializedName("apicode")
        var apicode: String = "",
        @SerializedName("apiname")
        var apiname: String = "",
        @SerializedName("apitxnid")
        var apitxnid: String = "",
        @SerializedName("charge")
        var charge: Double = 0.0,
        @SerializedName("created_at")
        var createdAt: String = "",
        @SerializedName("fundbank")
        var fundbank: String = "",
        @SerializedName("gst")
        var gst: Int = 0,
        @SerializedName("mobile")
        var mobile: Any = Any(),
        @SerializedName("number")
        var number: String = "",
        @SerializedName("option1")
        var option1: String = "",
        @SerializedName("option2")
        var option2: String = "",
        @SerializedName("option3")
        var option3: String = "",
        @SerializedName("option4")
        var option4: String = "",
        @SerializedName("payid")
        var payid: String = "",
        /*@SerializedName("profit")
        var profit: Double = 0.0,*/
        @SerializedName("provider_id")
        var providerId: Int = 0,
        @SerializedName("providername")
        var providername: String = "",
        @SerializedName("refno")
        var refno: String = "",
        @SerializedName("sendername")
        var sendername: String = "",
        @SerializedName("status")
        var status: String = "",
        /*@SerializedName("tds")
        var tds: Int = 0,*/
        @SerializedName("txnid")
        var txnid: String = "",
        @SerializedName("user_id")
        var userId: Int = 0,
        @SerializedName("username")
        var username: String = ""
    )
}