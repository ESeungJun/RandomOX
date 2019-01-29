package com.seungjun.randomox.network.data;

import com.google.gson.annotations.SerializedName;

public class UserInfo extends HeaderInfo {

    @SerializedName("user_key")
    public String user_key = "";

    @SerializedName("user_point")
    public int user_point = 0;

    @SerializedName("user_sIndex")
    public int user_sIndex = 0;
}
