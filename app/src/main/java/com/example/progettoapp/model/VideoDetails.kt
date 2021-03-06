package com.example.progettoapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VideoDetails {
    @SerializedName("items")
    @Expose
    var items: List<Item>? = null
}