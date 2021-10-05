package com.example.jsonapp

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONArray


class BookDetails {


    @SerializedName("date")
    var date: String? = null

    @SerializedName("eur")
    var eur: JsonObject? = null
}