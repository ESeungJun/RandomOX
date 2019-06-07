package com.seungjun.randomox.network.data

import com.google.gson.annotations.SerializedName

import java.util.ArrayList

data class RankInfo(@SerializedName("ranks") var ranks: ArrayList<RankData> = arrayListOf()) : HeaderInfo() {

    inner class RankData {

        @SerializedName("user_nick")
        var user_nick = ""

        @SerializedName("user_point")
        var user_point = 0

        @SerializedName("rank")
        var rank = 0

    }

}
