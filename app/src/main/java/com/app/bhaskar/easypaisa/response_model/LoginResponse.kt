package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("userdata")
    var userdata: Userdata = Userdata()
) {
    data class Userdata(
        @SerializedName("aadharcard")
        var aadharcard: String = "",
        var firstTime: Boolean = false,
        @SerializedName("appversion")
        var appversion: String = "",
        @SerializedName("address")
        var address: String = "",
        @SerializedName("apptoken")
        var apptoken: String = "",
        @SerializedName("city")
        var city: String = "",
        @SerializedName("company")
        var company: Any = Any(),
        @SerializedName("email")
        var email: String = "",
        @SerializedName("gstin")
        var gstin: Any = Any(),
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("mobile")
        var mobile: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("pancard")
        var pancard: String = "",
        @SerializedName("parents")
        var parents: String = "",
        @SerializedName("pincode")
        var pincode: String = "",
        @SerializedName("role")
        var role: Role = Role(),
        @SerializedName("role_id")
        var roleId: Int = 0,
        @SerializedName("shopname")
        var shopname: String = "",
        @SerializedName("state")
        var state: String = ""
    ) {
        data class Role(
            @SerializedName("created_at")
            var createdAt: String = "",
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("name")
            var name: String = "",
            @SerializedName("scheme")
            var scheme: String = "",
            @SerializedName("slug")
            var slug: String = "",
            @SerializedName("updated_at")
            var updatedAt: String = ""
        )
    }
}