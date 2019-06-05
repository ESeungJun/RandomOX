package com.seungjun.randomox.network.data

import com.google.gson.annotations.SerializedName

open class HeaderInfo {


    @SerializedName("reqCode")
    var reqCode = 0

    @SerializedName("reqMsg")
    var reqMsg = ""
}
