package com.seungjun.randomox.network.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class OxContentInfo : HeaderInfo() {

    @SerializedName("oxList")
    var oxList = ArrayList<OxData>()


    class OxData protected constructor(`in`: Parcel) : Parcelable {

        @SerializedName("quiz_index")
        var quiz_index = 0

        @SerializedName("quiz_text")
        var quiz_text: String = ""

        @SerializedName("quiz_ox")
        var quiz_ox: String = ""

        @SerializedName("quiz_img")
        var quiz_img: String = ""

        @SerializedName("quiz_point")
        var quiz_point = 0

        @SerializedName("quiz_coment")
        var quiz_coment: String = ""

        @SerializedName("quiz_tag")
        var quiz_tag: String = ""

        @SerializedName("quiz_g_coment")
        var quiz_g_coment: String = ""

        @SerializedName("quiz_g_img")
        var quiz_g_img: String = ""

        @SerializedName("quiz_special")
        var quiz_special = 0


        init {
            quiz_index = `in`.readInt()
            quiz_text = `in`.readString()
            quiz_ox = `in`.readString()
            quiz_img = `in`.readString()
            quiz_point = `in`.readInt()
            quiz_coment = `in`.readString()
            quiz_tag = `in`.readString()
            quiz_g_coment = `in`.readString()
            quiz_g_img = `in`.readString()
            quiz_special = `in`.readInt()
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(parcel: Parcel, i: Int) {
            parcel.writeInt(quiz_index)
            parcel.writeString(quiz_text)
            parcel.writeString(quiz_ox)
            parcel.writeString(quiz_img)
            parcel.writeInt(quiz_point)
            parcel.writeString(quiz_coment)
            parcel.writeString(quiz_tag)
            parcel.writeString(quiz_g_coment)
            parcel.writeString(quiz_g_img)
            parcel.writeInt(quiz_special)
        }

        companion object CREATOR: Parcelable.Creator<OxData> {
                override fun createFromParcel(`in`: Parcel): OxData {
                    return OxData(`in`)
                }

                override fun newArray(size: Int): Array<OxData?> {
                    return arrayOfNulls(size)
                }
            }
        }

}
