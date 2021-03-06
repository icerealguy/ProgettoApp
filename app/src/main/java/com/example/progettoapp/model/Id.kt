package com.example.progettoapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Id {
    @SerializedName("kind")
    @Expose
    var kind: String? = null

    @SerializedName("videoId")
    @Expose
    var videoId: String? = null

    @SerializedName("playlistId")
    @Expose
    var playlistId: String? = null
}