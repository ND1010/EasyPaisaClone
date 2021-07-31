package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class AddNewBankRequest(
    @SerializedName("account")
    var account: String = "",
    @SerializedName("accounttype")
    var accounttype: String = "",
    @SerializedName("bank")
    var bank: String = "",
    @SerializedName("bankname")
    var bankname: String = "",
    @SerializedName("ifsc")
    var ifsc: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("user_id")
    var userId: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("txnid")
    var txnid: String = ""
)