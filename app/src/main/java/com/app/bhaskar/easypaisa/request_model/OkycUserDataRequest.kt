package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class OkycUserDataRequest(
    @SerializedName("token")
    var apptoken: String = "",
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("user_id")
    var userId: String = ""
) {
    data class Data(
        @SerializedName("aadhaarNumber")
        var aadhaarNumber: String = "",
        @SerializedName("address")
        var address: String = "",
        @SerializedName("address_careof")
        var addressCareof: String = "",
        @SerializedName("address_country")
        var addressCountry: String = "",
        @SerializedName("address_dist")
        var addressDist: String = "",
        @SerializedName("address_house")
        var addressHouse: String = "",
        @SerializedName("address_landmark")
        var addressLandmark: String = "",
        @SerializedName("address_loc")
        var addressLoc: String = "",
        @SerializedName("address_pc")
        var addressPc: String = "",
        @SerializedName("address_po")
        var addressPo: String = "",
        @SerializedName("address_state")
        var addressState: String = "",
        @SerializedName("address_street")
        var addressStreet: String = "",
        @SerializedName("address_subdist")
        var addressSubdist: String = "",
        @SerializedName("address_vtc")
        var addressVtc: String = "",
        @SerializedName("code")
        var code: String = "",
        @SerializedName("dob")
        var dob: String = "",
        @SerializedName("emailHash")
        var emailHash: String = "",
        @SerializedName("genDate")
        var genDate: String = "",
        @SerializedName("gender")
        var gender: String = "",
        @SerializedName("imagebase64")
        var imagebase64: String = "",
        @SerializedName("isEmailVerified")
        var isEmailVerified: String = "",
        @SerializedName("isError")
        var isError: String = "",
        @SerializedName("isMobileVerified")
        var isMobileVerified: String = "",
        @SerializedName("isXmlVerify")
        var isXmlVerify: String = "",
        @SerializedName("maskedAadhaarNumber")
        var maskedAadhaarNumber: String = "",
        @SerializedName("mobileHash")
        var mobileHash: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("sharecode")
        var sharecode: String = "",
        @SerializedName("zipFileBase64")
        var zipFileBase64: String = ""
    )
}