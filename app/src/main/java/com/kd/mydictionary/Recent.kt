package com.kd.mydictionary

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recent(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    val recent : String,
    val isFav:Boolean
)
