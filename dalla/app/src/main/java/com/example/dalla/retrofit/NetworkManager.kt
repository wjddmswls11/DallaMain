package com.example.dalla.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkManager {
    private const val BASE_URL = "http://00.00.000.00:0000"

    fun retrofit(): DallaRetrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DallaRetrofit::class.java)
    }
}
