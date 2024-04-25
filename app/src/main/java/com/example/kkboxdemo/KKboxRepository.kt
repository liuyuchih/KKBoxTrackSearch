package com.example.kkboxdemo

import com.example.kkboxdemo.remote.KKBoxApi
import com.example.kkboxdemo.remote.KKBoxAuthApi

class KKboxRepository constructor(
    val authApi: KKBoxAuthApi,
    val api: KKBoxApi
) {

}



