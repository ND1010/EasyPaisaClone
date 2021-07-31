package com.app.bhaskar.easypaisa.response_model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserRequiredDataResponse(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("news")
    var news: String = "",
    @SerializedName("notice")
    var notice: String = "",
    @SerializedName("billnotice")
    var billnotice: String = "",
    @SerializedName("supportnumber")
    var supportnumber: String = "",
    @SerializedName("supportemail")
    var supportemail: String = "",
    @SerializedName("aadharbanks")
    var aadharbanks: List<Aadharbank> = listOf(),
    @SerializedName("slides")
    var slides: ArrayList<Slides> = ArrayList(),
    @SerializedName("kyc")
    var kyc: String = "",
    @SerializedName("resetpwd")
    var resetpwd: String = "",
    @SerializedName("aepsbanks")
    var aepsbanks: List<Aepsbank> = listOf(),
    @SerializedName("agent")
    var agent: Agent = Agent(),
    @SerializedName("state")
    var state: List<State> = listOf(),
    @SerializedName("yesagent")
    var yesagent: Yesagent = Yesagent(),
    @SerializedName("yesbanks")
    var yesbanks: List<Yesbank> = listOf()
) {
    data class Aadharbank(
        @SerializedName("bankName")
        var bankName: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("iinno")
        var iinno: String = ""
    ) : Serializable

    data class Aepsbank(
        @SerializedName("activeFlag")
        var activeFlag: String = "",
        @SerializedName("bankName")
        var bankName: String = "",
        @SerializedName("details")
        var details: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("iinno")
        var iinno: String = "",
        @SerializedName("remarks")
        var remarks: String = "",
        @SerializedName("timestamp")
        var timestamp: String = ""
    ) : Serializable

    data class Agent(
        @SerializedName("aadharPic")
        var aadharPic: String = "",
        @SerializedName("everify")
        var everify: String = "",
        @SerializedName("created_at")
        var createdAt: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("merchantAadhar")
        var merchantAadhar: String = "",
        @SerializedName("merchantAddress")
        var merchantAddress: String = "",
        @SerializedName("merchantCityName")
        var merchantCityName: String = "",
        @SerializedName("merchantEmail")
        var merchantEmail: String = "",
        @SerializedName("merchantLoginId")
        var merchantLoginId: String = "",
        @SerializedName("merchantLoginPin")
        var merchantLoginPin: String = "",
        @SerializedName("merchantName")
        var merchantName: String = "",
        @SerializedName("merchantPhoneNumber")
        var merchantPhoneNumber: String = "",
        @SerializedName("merchantPinCode")
        var merchantPinCode: String = "",
        @SerializedName("merchantShopName")
        var merchantShopName: String = "",
        @SerializedName("merchantState")
        var merchantState: String = "",
        @SerializedName("pancardPic")
        var pancardPic: String = "",
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

    data class State(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("state")
        var state: String = "",
        @SerializedName("stateCode")
        var stateCode: String = "",
        @SerializedName("stateId")
        var stateId: String = ""
    ) {
        override fun toString(): String {
            return state
        }
    }

    data class Yesagent(
        @SerializedName("aadharPic")
        var aadharPic: String = "",
        @SerializedName("created_at")
        var createdAt: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("merchantAadhar")
        var merchantAadhar: String = "",
        @SerializedName("merchantAddress")
        var merchantAddress: String = "",
        @SerializedName("merchantAgentcode")
        var merchantAgentcode: String = "",
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
        var merchantShopName: String = "",
        @SerializedName("merchantState")
        var merchantState: String = "",
        @SerializedName("pancardPic")
        var pancardPic: String = "",
        @SerializedName("remark")
        var remark: Any = Any(),
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

    data class Yesbank(
        @SerializedName("AcquirerCode")
        var acquirerCode: String = "",
        @SerializedName("BankCode")
        var bankCode: String = "",
        @SerializedName("BankIIN")
        var bankIIN: String = "",
        @SerializedName("BankId")
        var bankId: Int = 0,
        @SerializedName("BankName")
        var bankName: String = "",
        @SerializedName("id")
        var id: Int = 0
    ) : Serializable

    data class Slides(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("name")
        var name: String = "",
        @SerializedName("value")
        var value: String = "",
        @SerializedName("company_id")
        var company_id: Int = 0
    ) {

    }
}