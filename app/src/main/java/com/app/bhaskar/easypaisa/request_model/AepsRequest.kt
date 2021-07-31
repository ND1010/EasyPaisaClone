package com.app.bhaskar.easypaisa.request_model


import com.app.bhaskar.easypaisa.response_model.UserRequiredDataResponse
import com.google.gson.annotations.SerializedName

data class AepsRequest(
    @SerializedName("aadharNo")
    var aadharNo: String = "",
    @SerializedName("transactionFor")
    var transactionFor: String = "",
    @SerializedName("amount")
    var amount: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("bank")
    var bank: UserRequiredDataResponse.Aepsbank? = null,
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("aepsServiceFor")
    var aepsServiceFor: Int = 0
)