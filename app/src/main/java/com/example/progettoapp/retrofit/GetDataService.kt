package com.example.progettoapp.retrofit

import com.example.progettoapp.model.VideoDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetDataService {
    @GET("search")
    fun getVideoData(
            @Query("part") part: String?,
            @Query("channelId") channelId: String?,
            @Query("q") q: String?,
            @Query("key") key: String?
    ): Call<VideoDetails?>?
}