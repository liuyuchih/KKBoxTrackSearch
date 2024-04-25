package com.example.kkboxdemo.remote

import com.example.myapplication.remote.KKBoxSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface KKBoxApi {
    @GET("v1.1/search")
    suspend fun searchTracks(
        @Header("Authorization") authorization: String,
        @Query("q") query: String,
        @Query("type") type: String = "track",
        @Query("territory") territory: String = "TW",
    ): KKBoxSearchResponse

    @GET
    suspend fun fetchTracksByUrl(
        @Header("Authorization") authorization: String,
        @Url url: String,
    ): KKBoxSearchResponse
}