package com.example.progettoapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Medium {
    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("width")
    @Expose
    var width: Int? = null

    @SerializedName("height")
    @Expose
    var height: Int? = null
}