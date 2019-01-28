package com.seungjun.randomox.network.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OxContentInfo extends HeaderInfo{

    @SerializedName("oxList")
    public ArrayList<OxData> oxList = new ArrayList<>();


    public static class OxData  implements Parcelable {

        @SerializedName("quiz_index")
        public int quiz_index = 0;

        @SerializedName("quiz_text")
        public String quiz_text = "";

        @SerializedName("quiz_ox")
        public String quiz_ox = "";

        @SerializedName("quiz_img")
        public String quiz_img = "";

        @SerializedName("quiz_point")
        public int quiz_point = 0;

        @SerializedName("quiz_coment")
        public String quiz_coment = "";

        @SerializedName("quiz_tag")
        public String quiz_tag = "";

        @SerializedName("quiz_g_coment")
        public String quiz_g_coment = "";

        @SerializedName("quiz_g_img")
        public String quiz_g_img = "";


        protected OxData(Parcel in) {
            quiz_index = in.readInt();
            quiz_text = in.readString();
            quiz_ox = in.readString();
            quiz_img = in.readString();
            quiz_point = in.readInt();
            quiz_coment = in.readString();
            quiz_tag = in.readString();
            quiz_g_coment = in.readString();
            quiz_g_img = in.readString();
        }

        public static final Creator<OxData> CREATOR = new Creator<OxData>() {
            @Override
            public OxData createFromParcel(Parcel in) {
                return new OxData(in);
            }

            @Override
            public OxData[] newArray(int size) {
                return new OxData[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(quiz_index);
            parcel.writeString(quiz_text);
            parcel.writeString(quiz_ox);
            parcel.writeString(quiz_img);
            parcel.writeInt(quiz_point);
            parcel.writeString(quiz_coment);
            parcel.writeString(quiz_tag);
            parcel.writeString(quiz_g_coment);
            parcel.writeString(quiz_g_img);
        }
    }

}
