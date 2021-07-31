package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class AepsTnxDataResponse(
    @SerializedName("agentcode")
    var agentcode: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("txnid")
    var txnid: String = ""
)