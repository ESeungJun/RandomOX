package com.seungjun.randomox.network.data

import com.google.gson.annotations.SerializedName

data class UserInfo(@SerializedName("user_key") var user_key: String = "",
                    @SerializedName("user_point") var user_point: Int = 0,
                    @SerializedName("user_sIndex") var user_sIndex: Int = 0,
                    @SerializedName("rank") var rank: Int = 0) : HeaderInfo()
