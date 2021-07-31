package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class AgentKycRequest(
    @SerializedName("token")
    var token: String = "",
    @SerializedName("user_id")
    var userId: String = "",
    @SerializedName("address")
    var merchantAddress: String = "",
    @SerializedName("city")
    var merchantCityName: String = "",
    @SerializedName("state")
    var merchantState: String = "",
    @SerializedName("pincode")
    var merchantPinCode: String = "",
    @SerializedName("pancard")
    var userPan: String = "",
    @SerializedName("aadharcard")
    var merchantAadhar: String = "",
    @SerializedName("aadharcardpics")
    var aadharPics: String = "",
    @SerializedName("aadharcardbackpics")
    var aadharcardbackpics: String = "",
    @SerializedName("pancardpics")
    var pancardPics: String = "",
    @SerializedName("userSelfieImages")
    var userSelfieImages: String = "",
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = ""

)