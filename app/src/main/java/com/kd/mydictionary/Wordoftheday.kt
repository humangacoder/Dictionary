package com.kd.mydictionary

data class Wordoftheday(
    val word : String?,
    val definitions : ArrayList<meaning>?
)

data class meaning (
    val text : String,
    val partOfSpeech : String
)