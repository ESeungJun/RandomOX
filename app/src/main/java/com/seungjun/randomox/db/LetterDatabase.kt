package com.seungjun.randomox.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [LetterDBData::class], version = 3)
abstract class LetterDatabase: RoomDatabase() {

    abstract fun letterDao() : LetterDBDao


    companion object{
        private var instance : LetterDatabase? = null

        private val migration = object : Migration(2, 3){
            override fun migrate(database: SupportSQLiteDatabase) {

                val sb = StringBuffer().apply {
                    append(" CREATE TABLE letterTB_2 (")
                    append(" _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ") // 인덱스
                    append(" letter_req_date  TEXT NOT NULL, ") // 받은 날짜
                    append(" letter_req_text  TEXT NOT NULL, ") // 받은 내용
                    append(" letter_read TEXT NOT NULL ) ") // 받은 내용
                }

                database.execSQL(sb.toString())

                database.execSQL("INSERT INTO letterTB_2(_id, letter_req_date, letter_req_text, letter_read) SELECT * FROM letterTB")
                database.execSQL("DROP TABLE letterTB")
            }

        }

        fun getDBInstance(context: Context): LetterDBDao
                = provideDatabase(context).letterDao()

        private fun provideDatabase(context: Context): LetterDatabase{

            if(instance == null){
                instance = Room.databaseBuilder(context.applicationContext,
                        LetterDatabase::class.java, "randomox.db")
                        .addMigrations(migration)
                        .build()

            }

            return instance!!
        }

    }
}