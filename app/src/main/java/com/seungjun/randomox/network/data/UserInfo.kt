package com.seungjun.randomox.network.data

import com.google.gson.annotations.SerializedName

class UserInfo : HeaderInfo() {

    @SerializedName("user_key")
    var user_key = ""

    @SerializedName("user_point")
    var user_point = 0

    @SerializedName("user_sIndex")
    var user_sIndex = 0

    @SerializedName("rank")
    var rank = 0
}
