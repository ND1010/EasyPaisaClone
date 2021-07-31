package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class MobileRechargePlanResponse(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("plans")
    var plans: List<Plan> = listOf(),
    @SerializedName("status")
    var status: String = ""
) {
    data class Plan(
        @SerializedName("amount")
        var amount: String = "",
        @SerializedName("detail")
        var detail: String = "",
        @SerializedName("talktime")
        var talktime: String = "",
        @SerializedName("validity")
        var validity: String = ""
    )
}