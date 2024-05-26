package com.example.mad_project.common

import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.lang.Thread.sleep

fun fetchJson(url: String, processJson: (String) -> Unit) {
    val request = Request.Builder()
        .url(url)
        .build()
    Log.i("API FATCH",url)
    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            Log.i("API FATCH ERROR",e.stackTraceToString())
        }

        override fun onResponse(call: Call, response: Response) {
            try {
                val jsonData = response.body?.string()
                    processJson(jsonData ?: "")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i("error fatching data",e.toString())
            }
        }
    })
}

fun getallImages(obj: String, callback: (List<String>) -> Unit) {
    val links = mutableListOf<String>()
    val url = "https://customsearch.googleapis.com/customsearch/v1?q=${obj.replace(" ","+")}+Place&lr=countryAT&cx=b3cb193df5c1640f1&searchType=image&key=AIzaSyD2h-OIYYajhCr15PrjIm6U1IqHkMJ968k";
    fetchJson(url) { json ->
        val jsonObject = JSONObject(json)
        if (jsonObject.has("items")) {
            val items = jsonObject.getJSONArray("items")

            for (i in 0 until items.length()) {
                val item = items.getJSONObject(i)
                val link = item.getString("link")
                links.add(link)
                Log.i("Image URL", link) // Log the image URL
            }
        } else {
            Log.e("getallImages", "No 'items' in the JSON response")
        }
        callback(links)
    }
}