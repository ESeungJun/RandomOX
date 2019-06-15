package com.seungjun.randomox.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Flowable

@Dao
interface LetterDBDao {

    @Query("SELECT * FROM letterTB_2 ORDER BY letter_req_date DESC")
    fun getAllData() : Flowable<List<LetterDBData>>

    @Insert
    fun addLetterData(data : LetterDBData)

    @Update
    fun changeReadState(data : LetterDBData)
}