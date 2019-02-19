package com.seungjun.randomox.network.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RankInfo extends HeaderInfo {

    @SerializedName("ranks")
    public ArrayList<RankData> ranks = new ArrayList<RankData>();

    public class RankData {


        @SerializedName("user_nick")
        public String user_nick = "";

        @SerializedName("user_point")
        public int user_point = 0;

        @SerializedName("rank")
        public int rank = 0;

    }

}
