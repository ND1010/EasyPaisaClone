package com.app.bhaskar.easypaisa.response_model


import com.google.gson.annotations.SerializedName

data class AgentPancardResponse(
    @SerializedName("charge")
    var charge: Double = 0.0,
    @SerializedName("matmcharge")
    var matmcharge: Double = 0.0,
    @SerializedName("matmprovider")
    var matmprovider: Matmprovider = Matmprovider(),
    @SerializedName("providers")
    var providers: Providers = Providers(),
    @SerializedName("utiagent")
    var utiagent: Utiagent = Utiagent()
) {
    data class Matmprovider(
        @SerializedName("api_id")
        var apiId: Int = 0,
        @SerializedName("billfetch")
        var billfetch: String = "",
        @SerializedName("circle_id")
        var circleId: Any = Any(),
        @SerializedName("extraparamname")
        var extraparamname: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("logo")
        var logo: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("paramname")
        var paramname: Any = Any(),
        @SerializedName("recharge1")
        var recharge1: String = "",
        @SerializedName("recharge2")
        var recharge2: String = "",
        @SerializedName("recharge3")
        var recharge3: String = "",
        @SerializedName("recharge4")
        var recharge4: String = "",
        @SerializedName("status")
        var status: String = "",
        @SerializedName("type")
        var type: String = ""
    )

    data class Providers(
        @SerializedName("api_id")
        var apiId: Int = 0,
        @SerializedName("billfetch")
        var billfetch: String = "",
        @SerializedName("circle_id")
        var circleId: Any = Any(),
        @SerializedName("extraparamname")
        var extraparamname: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("logo")
        var logo: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("paramname")
        var paramname: Any = Any(),
        @SerializedName("recharge1")
        var recharge1: String = "",
        @SerializedName("recharge2")
        var recharge2: String = "",
        @SerializedName("recharge3")
        var recharge3: String = "",
        @SerializedName("recharge4")
        var recharge4: String = "",
        @SerializedName("status")
        var status: String = "",
        @SerializedName("type")
        var type: String = ""
    )

    data class Utiagent(
        @SerializedName("api")
        var api: Any = Any(),
        @SerializedName("api_id")
        var apiId: Any = Any(),
        @SerializedName("contact_person")
        var contactPerson: String = "",
        @SerializedName("created_at")
        var createdAt: String = "",
        @SerializedName("email")
        var email: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("location")
        var location: String = "",
        @SerializedName("mobile")
        var mobile: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("payid")
        var payid: Any = Any(),
        @SerializedName("pincode")
        var pincode: String = "",
        @SerializedName("remark")
        var remark: Any = Any(),
        @SerializedName("sender_id")
        var senderId: Any = Any(),
        @SerializedName("state")
        var state: String = "",
        @SerializedName("status")
        var status: String = "",
        @SerializedName("txnid")
        var txnid: String = "",
        @SerializedName("type")
        var type: String = "",
        @SerializedName("updated_at")
        var updatedAt: String = "",
        @SerializedName("user")
        var user: User = User(),
        @SerializedName("user_id")
        var userId: Int = 0,
        @SerializedName("vleid")
        var vleid: String = "",
        @SerializedName("vlepassword")
        var vlepassword: String = ""
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