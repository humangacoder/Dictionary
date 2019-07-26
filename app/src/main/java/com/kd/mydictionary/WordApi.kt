package com.kd.mydictionary

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import kotlin.collections.ArrayList

interface WordApi {


    @GET("wordOfTheDay")
    suspend fun getwordoftheday(@Query("date")date: String,
                                @Query("api_key")  api_key : String) : Response<Wordoftheday>

    @GET("randomWord")
    suspend fun getrandomwords(@Query("hasDictionaryDef") hasDictionaryDef : Boolean,
                               @Query("maxCorpusCount") maxCorpusCount : Int,
                               @Query("minDictionaryCount")minDictionaryCount : Int,
                               @Query("maxDictionaryCount")maxDictionaryCount : Int,
                               @Query("minLength") minLength : Int,
                               @Query("maxLength") maxLength:Int,
                               @Query("api_key")api_key : String) : Response<RandomWord>


    @GET("{word}/definitions")
    suspend fun getwordmeaning(@Path("word") word : String,
                               @Query("limit") limit : Int,
                               @Query("includeRelated") includeRelated : Boolean,
                               @Query("useCanonical") useCanonical : Boolean,
                               @Query("includeTags") includeTags : Boolean,
                               @Query("api_key") api_key : String) : Response<ArrayList<WordMeaning>>

   @GET("{word}/topExample")
   suspend fun getexamples(@Path("word") word : String,
                            @Query("useCanonical") useCanonical: Boolean,
                            @Query("api_key") api_key: String) : Response<Example2>

    @GET("{word}/relatedWords")
    suspend fun getrelatedwords(@Path("word")word: String,
                                @Query("useCanonical")useCanonical: Boolean,
                                @Query("relationshipTypes")relationshipTypes : String,
                                @Query("limitPerRelationshipType")limitPerRelationshipType : Int,
                                @Query("api_key")api_key: String) : Response<ArrayList<Synonym>>



}