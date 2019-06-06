package com.seungjun.randomox.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class RandomOxDBHelper(private val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {

        private val DB_NAME = "randomox.db"
        private val DB_VERSION = 2

        val LETTER_TB_NAME = "letterTB"
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {

        val sb = StringBuffer().apply {
            append(" CREATE TABLE letterTB (")
            append(" _id INTEGER PRIMARY KEY AUTOINCREMENT, ") // 인덱스
            append(" letter_req_date  DATETIME, ") // 받은 날짜
            append(" letter_req_text  TEXT, ") // 받은 내용
            append(" letter_read  VARCHAR(10) ) ") // 받은 내용
        }

        sqLiteDatabase.execSQL(sb.toString())
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldV: Int, newV: Int) {
        if (newV > oldV) {
            sqLiteDatabase.execSQL("ALTER TABLE letterTB ADD COLUMN letter_read VARCHAR(10)")
        }

    }

}
