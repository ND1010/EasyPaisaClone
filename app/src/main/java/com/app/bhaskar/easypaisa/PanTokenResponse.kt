package com.app.bhaskar.easypaisa


import com.google.gson.annotations.SerializedName

data class PanTokenResponse(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("refno")
    var refno: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("txnid")
    var txnid: String = ""
)