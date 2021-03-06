package com.example.progettoapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettoapp.adapter.VideoDetailsAdapter
import com.example.progettoapp.model.Item
import com.example.progettoapp.model.VideoDetails
import com.example.progettoapp.retrofit.GetDataService
import com.example.progettoapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class VideoActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private val TAG = VideoActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video2)
        val intent = intent
        val cosa = intent.getStringExtra("cosa")
        Log.e(TAG, "COSA $cosa")
        val dataService = RetrofitInstance.retrofit?.create(GetDataService::class.java)
        if (dataService != null) {
            dataService
                    .getVideoData("snippet", "UCFIQb2xlJsleWC9fpFvIaGg", cosa, getString(R.string.key))?.enqueue(object : Callback<VideoDetails?> {
                        override fun onResponse(call: Call<VideoDetails?>, response: Response<VideoDetails?>) {
                            if (response.isSuccessful) {
                                if (response.body() != null) {
                                    Log.e(TAG, "Response Successfull")
                                    Toast.makeText(this@VideoActivity, "LOADING...", Toast.LENGTH_LONG).show()
                                    Log.e(TAG, "body   " + response.body())
                                    Log.e(TAG, "tostring   $response")
                                    Log.e(TAG, "code   " + response.code())
                                    Log.e(TAG, "errbody   " + response.errorBody())
                                    Log.e(TAG, "message   " + response.message())
                                    Log.e(TAG, "raw   " + response.raw())
                                    response.body()!!.items?.let { setUpRecyclerView(it) }
                                } else Log.e(TAG, "body is null")
                            } else {
                                try {
                                    Log.e(TAG, "NO RESPONSE = " + response.errorBody()!!.string())
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        override fun onFailure(call: Call<VideoDetails?>, t: Throwable) {
                            Toast.makeText(this@VideoActivity, t.message, Toast.LENGTH_LONG).show()
                            Log.e(TAG + "API REQ FAILED", t.message)
                        }
                    })
        }
    }

    private fun setUpRecyclerView(items: List<Item>) {
        recyclerView = findViewById(R.id.recyclerview)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@VideoActivity)
        recyclerView!!.setLayoutManager(layoutManager)
        val videoDetailsAdapter = VideoDetailsAdapter(this@VideoActivity, items)
        recyclerView!!.setAdapter(videoDetailsAdapter)
    }
}