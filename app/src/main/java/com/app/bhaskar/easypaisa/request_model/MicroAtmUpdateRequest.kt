package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class MicroAtmUpdateRequest(
    @SerializedName("BankName")
    var bankName: String = "",
    @SerializedName("BankRRN")
    var bankRRN: String = "",
    @SerializedName("CardNum")
    var cardNum: String = "",
    @SerializedName("CardType")
    var cardType: String = "",
    @SerializedName("FpId")
    var fpId: String = "",
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false,
    @SerializedName("token")
    var token: String = "",
    @SerializedName("txnid")
    var txnid: String = "",
    @SerializedName("user_id")
    var userId: String = ""
)