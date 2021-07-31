package com.pa.models.dao

import androidx.room.*
import com.pa.models.Result


@Dao
interface ResultDataDao {
    /*@Insert(onConflict = REPLACE)
    fun insert(task: Result): Long

    @Delete
    fun delete(task: Result)

    @Delete
    fun delete(taskList: ArrayList<Result>)

    @Query("Select * from pa_itunes")
    fun getAllTracks(): List<Result>*/

    @Insert
    fun insertAll(taskList: ArrayList<Result>)

}