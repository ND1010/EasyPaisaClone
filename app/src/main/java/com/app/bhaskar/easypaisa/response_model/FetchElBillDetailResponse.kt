package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class FetchElBillDetailResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: String = ""
) {
    data class Data(
        @SerializedName("customername")
        var customername: String = "",
        @SerializedName("dueamount")
        var dueamount: String = "",
        @SerializedName("duedate")
        var duedate: String = "",
        @SerializedName("mobile")
        var mobile: Any = Any(),
        @SerializedName("number")
        var number: String = "",
        @SerializedName("paramname")
        var paramname: String = "",
        @SerializedName("provider")
        var provider: String = "",
        @SerializedName("providerid")
        var providerid: Int = 0
    )
}