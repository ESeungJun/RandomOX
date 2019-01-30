package com.seungjun.randomox.network.data;


import com.google.gson.annotations.SerializedName;

public class NoticesInfo extends HeaderInfo {

    @SerializedName("noti_id")
    public int noti_id = 0;

    @SerializedName("noti_text")
    public String noti_text = "";

    @SerializedName("noti_date")
    public String noti_date = "";

    @SerializedName("noti_yn")
    public String noti_yn = "";


    public int getNoti_id() {
        return noti_id;
    }

    public void setNoti_id(int noti_id) {
        this.noti_id = noti_id;
    }

    public String getNoti_text() {
        return noti_text;
    }

    public void setNoti_text(String noti_text) {
        this.noti_text = noti_text;
    }

    public String getNoti_date() {
        return noti_date;
    }

    public void setNoti_date(String noti_date) {
        this.noti_date = noti_date;
    }

    public String getNoti_yn() {
        return noti_yn;
    }

    public void setNoti_yn(String noti_yn) {
        this.noti_yn = noti_yn;
    }
}
