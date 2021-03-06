package com.example.progettoapp.retrofit;

import com.example.progettoapp.model.VideoDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {
    @GET("search")
    Call<VideoDetails> getVideoData(
            @Query("part") String part,
            @Query("channelId") String channelId,
            @Query("q") String q,
            @Query("key") String key
    );
}
