package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class AgentKycDetailResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: String = ""
) {
    data class Data(
        @SerializedName("agent")
        var agent: Agent = Agent(),
        @SerializedName("agentCode")
        var agentCode: String = "",
        @SerializedName("cpCode")
        var cpCode: String = "",
        @SerializedName("cs")
        var cs: String = "",
        @SerializedName("mobileNumber")
        var mobileNumber: String = "",
        @SerializedName("panCardNo")
        var panCardNo: String = "",
        @SerializedName("shopName")
        var shopName: String? = "",
        @SerializedName("sscode")
        var sscode: String = "",
        @SerializedName("ts")
        var ts: Int = 0,
        @SerializedName("url")
        var url: String = ""
    ) {
        data class Agent(
            @SerializedName("aadharPic")
            var aadharPic: String = "",
            @SerializedName("created_at")
            var createdAt: String = "",
            @SerializedName("everify")
            var everify: String = "",
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("merchantAadhar")
            var merchantAadhar: String = "",
            @SerializedName("merchantAddress")
            var merchantAddress: String = "",
            @SerializedName("merchantAgentcode")
            var merchantAgentcode: String = "",
            @SerializedName("merchantAgentcodebck")
            var merchantAgentcodebck: String = "",
            @SerializedName("merchantCityName")
            var merchantCityName: String = "",
            @SerializedName("merchantEmail")
            var merchantEmail: String = "",
            @SerializedName("merchantName")
            var merchantName: String = "",
            @SerializedName("merchantPhoneNumber")
            var merchantPhoneNumber: String = "",
            @SerializedName("merchantPinCode")
            var merchantPinCode: String = "",
            @SerializedName("merchantShopName")
            var merchantShopName: Any = Any(),
            @SerializedName("merchantState")
            var merchantState: String = "",
            @SerializedName("pancardPic")
            var pancardPic: String = "",
            @SerializedName("remark")
            var remark: String = "",
            @SerializedName("status")
            var status: String = "",
            @SerializedName("updated_at")
            var updatedAt: String = "",
            @SerializedName("user")
            var user: User = User(),
            @SerializedName("user_id")
            var userId: Int = 0,
            @SerializedName("userPan")
            var userPan: String = "",
            @SerializedName("via")
            var via: String = ""
        ) {
            data class User(
                @SerializedName("company")
                var company: Any = Any(),
                @SerializedName("id")
                var id: Int = 0,
                @SerializedName("mobile")
                var mobile: String = "",
                @SerializedName("name")
                var name: String = "",
                @SerializedName("parents")
                var parents: String = "",
                @SerializedName("role")
                var role: Role = Role(),
                @SerializedName("role_id")
                var roleId: Int = 0
            ) {
                data class Role(
                    @SerializedName("created_at")
                    var createdAt: String = "",
                    @SerializedName("id")
                    var id: Int = 0,
                    @SerializedName("name")
                    var name: String = "",
                    @SerializedName("scheme")
                    var scheme: Int = 0,
                    @SerializedName("slug")
                    var slug: String = "",
                    @SerializedName("updated_at")
                    var updatedAt: String = ""
                )
            }
        }
    }
}