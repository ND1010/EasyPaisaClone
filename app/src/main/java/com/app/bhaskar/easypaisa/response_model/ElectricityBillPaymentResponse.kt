package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class ElectricityBillPaymentResponse(
    @SerializedName("amount")
    var amount: String = "",
    @SerializedName("date")
    var date: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("invoice")
    var invoice: String = "",
    @SerializedName("providername")
    var providername: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("mobile")
    var mobile: Any = Any(),
    @SerializedName("name")
    var name: Any = Any(),
    @SerializedName("number")
    var number: String = "",
    @SerializedName("paramname")
    var paramname: String = "",
    @SerializedName("provider")
    var provider: String = "",
    @SerializedName("providerid")
    var providerid: Int = 0,
    @SerializedName("refno")
    var refno: Any = Any(),
    @SerializedName("status")
    var status: String = "",
    @SerializedName("txnid")
    var txnid: String = ""
)