package com.example.progettoapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    var retrofit: Retrofit? = null
        get() {
            if (field == null) {
                field = Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL).build()
            }
            return field
        }
        private set
    private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"
}