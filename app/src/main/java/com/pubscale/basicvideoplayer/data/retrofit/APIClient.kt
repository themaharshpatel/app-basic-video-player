package com.pubscale.basicvideoplayer.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {

    val client by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://raw.githubusercontent.com/")
            .build()
    }

}