package com.example.kkboxdemo.remote

import com.example.kkboxdemo.BuildConfig
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface KKBoxAuthApi {
    @FormUrlEncoded
    @POST("/oauth2/token")
    suspend fun getToken(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("client_id") clientId: String = BuildConfig.KKBOX_ID,
        @Field("client_secret") clientSecret: String = BuildConfig.KKBOX_SECRET
    ): TokenResponse
}
