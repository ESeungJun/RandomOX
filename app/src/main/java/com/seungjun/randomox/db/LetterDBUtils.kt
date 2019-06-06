package com.seungjun.randomox.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import com.seungjun.randomox.utils.D

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

object LetterDBUtils {

    val TAG = "LetterDBUtils"

    var db: SQLiteDatabase? = null


    val allData: ArrayList<LetterDBData>
        get() {
            val list = ArrayList<LetterDBData>()

            val query = "SELECT * FROM " + RandomOxDBHelper.LETTER_TB_NAME + " ORDER BY letter_req_date DESC "

            try {
                db!!.beginTransaction()
                val cursor = db!!.rawQuery(query, null)

                while (cursor.moveToNext()) {
                    val data = LetterDBData().apply {
                        _id = cursor.getInt(cursor.getColumnIndex("_id"))
                        letter_req_date = cursor.getString(cursor.getColumnIndex("letter_req_date"))
                        letter_req_text = cursor.getString(cursor.getColumnIndex("letter_req_text"))
                        letter_read = cursor.getString(cursor.getColumnIndex("letter_read"))
                    }

                    list.add(data)
                }

                db!!.setTransactionSuccessful()

            } catch (e: Exception) {
                e.printStackTrace()

            } finally {
                db!!.endTransaction()
            }

            return list
        }


    fun saveLetterData(text: String) {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()

        val values = ContentValues().apply {
            put("letter_req_date", dateFormat.format(date))
            put("letter_req_text", text)
            put("letter_read", "N")
        }

        D.log(TAG, "db Insert")

        db!!.insert(RandomOxDBHelper.LETTER_TB_NAME, null, values)
    }

    fun allDeleteLetter() {
        db!!.delete(RandomOxDBHelper.LETTER_TB_NAME, null, null)
    }


    fun updateLetterReadState(letter_id: Int, read: String) {

        val where = "_id = $letter_id"

        val values = ContentValues().apply {
            put("letter_read", read)
        }

        D.log(TAG, "db update")

        db!!.update(RandomOxDBHelper.LETTER_TB_NAME, values, where, null)


    }


}
