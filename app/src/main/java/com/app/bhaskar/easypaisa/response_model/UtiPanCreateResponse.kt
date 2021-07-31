package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class UtiPanCreateResponse(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("refno")
    var refno: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("txnid")
    var txnid: String = ""
)