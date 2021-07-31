package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class AddNewBeneficiaryRequest(
    @SerializedName("beneaccount")
    var beneaccount: String = "",
    @SerializedName("beneifsc")
    var beneifsc: String = "",
    @SerializedName("benename")
    var benename: String = "",
    @SerializedName("bankname")
    var bankname: String = "",
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("user_id")
    var userId: String = ""
)