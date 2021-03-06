package com.example.progettoapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Thumbnails {
    @SerializedName("default")
    @Expose
    var default: Default? = null

    @SerializedName("medium")
    @Expose
    var medium: Medium? = null

    @SerializedName("high")
    @Expose
    var high: High? = null
}