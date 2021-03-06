package com.example.progettoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.progettoapp.adapter.VideoDetailsAdapter;
import com.example.progettoapp.model.Item;
import com.example.progettoapp.model.VideoDetails;
import com.example.progettoapp.retrofit.GetDataService;
import com.example.progettoapp.retrofit.RetrofitInstance;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VideoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private final String TAG= VideoActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video2);
        Intent intent = getIntent();
        String cosa = intent.getStringExtra("cosa");
        Log.e(TAG,"COSA "+cosa);

        GetDataService dataService = RetrofitInstance.getRetrofit().create(GetDataService.class);

        Call<VideoDetails> videoDetailsRequest = dataService
                .getVideoData("snippet","UCFIQb2xlJsleWC9fpFvIaGg", cosa, getString(R.string.key));

        videoDetailsRequest.enqueue(new Callback<VideoDetails>() {
            @Override
            public void onResponse(Call<VideoDetails> call, Response<VideoDetails> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        Log.e(TAG,"Response Successfull");
                        Toast.makeText(VideoActivity.this,"LOADING...", Toast.LENGTH_LONG).show();

                        Log.e(TAG,"body   "+response.body() );
                        Log.e(TAG,"tostring   "+response.toString() );
                        Log.e(TAG,"code   "+response.code() );
                        Log.e(TAG,"errbody   "+response.errorBody() );
                        Log.e(TAG,"message   "+response.message() );
                        Log.e(TAG,"raw   "+response.raw());

                        setUpRecyclerView(response.body().getItems());
                    }
                    else
                        Log.e(TAG,"body is null");
                }else {
                    try {
                        Log.e(TAG, "NO RESPONSE = "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoDetails> call, Throwable t) {
                Toast.makeText(VideoActivity.this,t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG.concat("API REQ FAILED"), t.getMessage());
            }
        });
    }


    private void setUpRecyclerView(List<Item> items) {

        recyclerView = findViewById(R.id.recyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(VideoActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        VideoDetailsAdapter videoDetailsAdapter = new VideoDetailsAdapter(VideoActivity.this, items);
        recyclerView.setAdapter(videoDetailsAdapter);

    }

}