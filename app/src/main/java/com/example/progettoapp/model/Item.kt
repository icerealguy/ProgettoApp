package com.example.progettoapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Item {
    @SerializedName("id")
    @Expose
    var id: Id? = null

    @SerializedName("snippet")
    @Expose
    var snippet: Snippet? = null
}