package com.app.bhaskar.easypaisa.repositories


import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @SerializedName("aadharcard")
    var aadharcard: String = "",
    @SerializedName("address")
    var address: String = "",
    @SerializedName("city")
    var city: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("otp")
    var otp: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("pancard")
    var pancard: String = "",
    @SerializedName("pincode")
    var pincode: String = "",
    @SerializedName("state")
    var state: String = ""
)