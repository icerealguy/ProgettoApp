package com.example.progettoapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class YoutubeVideo : YouTubeBaseActivity() {
    var playBtn: Button? = null
    var youTubePlayerView: YouTubePlayerView? = null
    var onInitializedListener: YouTubePlayer.OnInitializedListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_video)
        val intent = intent.extras
        val videoID = intent!!.getString("videoID")
        val url = "https://www.youtube.com/watch?v=$videoID"
        Log.e("YoutubeVideo", "ID VIDEO=$videoID\n Url $url")
        playBtn = findViewById(R.id.playBtn)
        youTubePlayerView = findViewById(R.id.youtubeplayerview)
        onInitializedListener = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(provider: YouTubePlayer.Provider, youTubePlayer: YouTubePlayer, b: Boolean) {
                youTubePlayer.loadVideo(videoID)
            }

            override fun onInitializationFailure(provider: YouTubePlayer.Provider, youTubeInitializationResult: YouTubeInitializationResult) {}
        }
        playBtn!!.setOnClickListener(View.OnClickListener { youTubePlayerView!!.initialize(getString(R.string.key), onInitializedListener) })
    }
}