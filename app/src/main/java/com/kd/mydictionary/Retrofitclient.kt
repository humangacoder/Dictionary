package com.kd.mydictionary

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.xml.datatype.DatatypeConstants.SECONDS
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


object Retrofitclient {


    val wordsApi = retrofit()
        .create(WordApi::class.java)

    val wordApi = retrofitword()
        .create(WordApi::class.java)

    private fun okHttpClient() = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

    private fun retrofit():Retrofit {
        Log.d("PUI","okhttp3 ${okHttpClient()}")
       return Retrofit.Builder()
            .baseUrl("https://api.wordnik.com/v4/words.json/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient())
            .build()
    }

    private fun retrofitword():Retrofit {
        Log.d("PUI","okhttp3 ${okHttpClient()}")
        return Retrofit.Builder()
            .baseUrl("https://api.wordnik.com/v4/word.json/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient())
            .build()
    }



}