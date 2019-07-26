package com.kd.mydictionary

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Recent::class], version = 1)
abstract class RecentDatabase : RoomDatabase(){

    abstract  fun recentdao() : recentDao



}
