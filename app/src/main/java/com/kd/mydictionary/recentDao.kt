package com.kd.mydictionary

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface recentDao {

    @Insert
    fun insertrecent(recent : Recent)

    @Delete
    fun deleterecent(recent: Recent)

    @Query("SELECT * FROM  Recent WHERE isFav = 0 ORDER BY id DESC")
    fun getAllrecent() : LiveData<List<Recent>>

    @Query("SELECT * FROM  Recent WHERE isFav = 1 ORDER BY id DESC")
    fun getAllfav() : LiveData<List<Recent>>

    @Query("DELETE FROM Recent WHERE isFav = 0")
    fun deleteallrecent()

    @Query("DELETE FROM Recent WHERE isFav = 1")
    fun deleteallFav()



}