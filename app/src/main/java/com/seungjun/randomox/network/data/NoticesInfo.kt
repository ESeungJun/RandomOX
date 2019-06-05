package com.seungjun.randomox.network.data


import com.google.gson.annotations.SerializedName

class NoticesInfo : HeaderInfo() {

    @SerializedName("noti_id")
    var noti_id = 0

    @SerializedName("noti_text")
    var noti_text = ""

    @SerializedName("noti_date")
    var noti_date = ""

    @SerializedName("noti_yn")
    var noti_yn = ""
}
