package com.seungjun.randomox.network.data

import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class RankInfo : HeaderInfo() {

    @SerializedName("ranks")
    var ranks = ArrayList<RankData>()

    inner class RankData {


        @SerializedName("user_nick")
        var user_nick = ""

        @SerializedName("user_point")
        var user_point = 0

        @SerializedName("rank")
        var rank = 0

    }

}
