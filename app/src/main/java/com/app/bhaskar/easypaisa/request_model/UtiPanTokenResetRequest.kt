package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class UtiPanTokenResetRequest(
    @SerializedName("adhaar")
    var adhaar: String = "",
    @SerializedName("contact_person")
    var contactPerson: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("location")
    var location: String = "",
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("pincode")
    var pincode: String = "",
    @SerializedName("state")
    var state: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("transactionType")
    var transactionType: String = "",
    @SerializedName("user_id")
    var userId: String = "",
    @SerializedName("vleid")
    var vleid: String = ""
)