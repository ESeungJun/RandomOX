package com.seungjun.randomox.network.data


import com.google.gson.annotations.SerializedName

data class NoticesInfo(@SerializedName("noti_id") var noti_id: Int = 0,
                       @SerializedName("noti_text") var noti_text: String = "",
                       @SerializedName("noti_date") var noti_date: String = "",
                       @SerializedName("noti_yn") var noti_yn: String = "") : HeaderInfo()
