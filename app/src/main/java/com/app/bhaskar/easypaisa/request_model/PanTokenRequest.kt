package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class PanTokenRequest(
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
    @SerializedName("pantokens")
    var pantokens: String = "",
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
    var vleid: String = "",
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = ""
)