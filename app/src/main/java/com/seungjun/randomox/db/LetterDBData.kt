package com.seungjun.randomox.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "letterTB_2")
data class LetterDBData(
        @PrimaryKey
        var _id: Int = 0,
        @ColumnInfo(typeAffinity = 1)
        var letter_req_date: String = "",
        var letter_req_text: String = "",
        var letter_read: String = "")
