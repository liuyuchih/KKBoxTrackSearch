package com.example.kkboxdemo

import android.app.Application
import com.example.kkboxdemo.remote.Constants
import com.example.kkboxdemo.remote.KKBoxApi
import com.example.kkboxdemo.remote.KKBoxAuthApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication: Application() {

    //AUTH
    private fun providesAuthRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.KKBOX_ACCOUNT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val authApi by lazy {
        val retrofit = providesAuthRetrofit()
        retrofit.create(KKBoxAuthApi::class.java)
    }

    //API
    private fun providesApiRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.KKBOX_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val api by lazy {
        val retrofit = providesApiRetrofit()
        retrofit.create(KKBoxApi::class.java)
    }

    val repository by lazy {
        KKboxRepository(authApi, api)
    }
}