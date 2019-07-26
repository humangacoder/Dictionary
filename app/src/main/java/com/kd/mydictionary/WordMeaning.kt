package com.kd.mydictionary

data class WordMeaning(
    val partOfSpeech : String? = null,
    val text :String? = null,
    val word : String? = null,
    val exampleUses : ArrayList<examples>? = null
)

data class examples(
    val text :String? = null
)
