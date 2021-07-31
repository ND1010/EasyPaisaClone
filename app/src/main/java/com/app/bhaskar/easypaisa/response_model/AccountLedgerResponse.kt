package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class AccountLedgerResponse(
    @SerializedName("data")
    var `data`: List<Data> = listOf(),
    @SerializedName("pages")
    var pages: Int = 0,
    @SerializedName("statuscode")
    var statuscode: String = ""
) {
    data class Data(
        @SerializedName("amount")
        var amount: Double = 0.0,
        @SerializedName("balance")
        var balance: Double = 0.0,
        @SerializedName("created_at")
        var createdAt: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("mode")
        var mode: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("number")
        var number: String = "",
        @SerializedName("product")
        var product: String = "",
        @SerializedName("provider_id")
        var providerId: Int = 0,
        @SerializedName("report_id")
        var reportId: Int = 0,
        @SerializedName("tds")
        var tds: Double = 0.0,
        @SerializedName("type")
        var type: String = "",
        @SerializedName("updated_at")
        var updatedAt: String = "",
        @SerializedName("user_id")
        var userId: Int = 0
    )
}