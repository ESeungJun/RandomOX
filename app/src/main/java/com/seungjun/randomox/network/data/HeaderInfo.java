package com.seungjun.randomox.network.data;

import com.google.gson.annotations.SerializedName;

public class HeaderInfo {


    @SerializedName("reqCode")
    public int reqCode = 0;

    @SerializedName("reqMsg")
    public String reqMsg = "";
}
