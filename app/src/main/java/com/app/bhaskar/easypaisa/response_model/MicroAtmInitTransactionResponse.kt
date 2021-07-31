package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class MicroAtmInitTransactionResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: String = ""
) {
    data class Data(
        @SerializedName("lat")
        var lat: String = "",
        @SerializedName("lon")
        var lon: String = "",
        @SerializedName("merchantId")
        var merchantId: String = "",
        @SerializedName("merchantPassword")
        var merchantPassword: String = "",
        @SerializedName("superMerchentId")
        var superMerchentId: String = "",
        @SerializedName("txnid")
        var txnid: String = ""
    )
}