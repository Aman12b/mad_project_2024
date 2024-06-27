package com.example.mad_project.common

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun fetchJson(url: String, processJson: (String?, String?) -> Unit) {
    val request = Request.Builder()
        .url(url)
        .build()
    Log.i("API FETCH", url)
    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            Log.i("API FETCH ERROR", e.stackTraceToString())
            processJson(null, "Error fetching data: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            try {
                val jsonData = response.body?.string()
                if (jsonData.isNullOrEmpty()) {
                    processJson(null, "No data received from the server")
                } else {
                    processJson(jsonData, null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i("Error fetching data", e.toString())
                processJson(null, "Error processing data: ${e.message}")
            }
        }
    })
}

fun getallImages(obj: String, callback: (List<String>) -> Unit): MutableList<String> {
    val links = mutableListOf<String>()
    val url = "https://customsearch.googleapis.com/customsearch/v1?q=${obj.replace(" ","+")}+Place&lr=countryAT&cx=b3cb193df5c1640f1&searchType=image&key=AIzaSyD2h-OIYYajhCr15PrjIm6U1IqHkMJ968k";
    fetchJson(url) { jsonData, errorMessage ->
        if (errorMessage != null) {
            // Handle error
            Log.e("API FETCH ERROR", errorMessage)
        } else {
            val jsonObject = JSONObject(jsonData)
            if (jsonObject.has("items")) {
                val items = jsonObject.getJSONArray("items")

                for (i in 0 until items.length()) {
                    val item = items.getJSONObject(i)
                    val link = item.getString("link")
                    links.add(link)
                    Log.i("Image URL", link)
                }
            } else {
                Log.e("getallImages", "No 'items' in the JSON response")
            }
        }
        callback(links)
    }
    return links
}

suspend fun getWikiInfo(placeName: String, callback: (String) -> Unit) {
    withContext(Dispatchers.IO) {
        val words = placeName.split(" ")
        val combinations = generateCombinations(words)

        val limitedCombinations = combinations.take(3)

        val combinationsToTry = mutableListOf(placeName)
        combinationsToTry.addAll(limitedCombinations)

        for ((index, combination) in combinationsToTry.withIndex()) {
            val url = URL("https://en.wikipedia.org/api/rest_v1/page/summary/${combination.replace(" ", "_")}")
            Log.e("getWikiInfo", url.toString())
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode == 200) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = reader.readText()
                reader.close()
                Log.e("getWikiInfo", response)
                val jsonObject = JSONObject(response)

                val extract = jsonObject.getString("extract")
                callback(extract)
                connection.disconnect()
                return@withContext // Exit function after successful callback
            }

            // Delay between requests to avoid spamming the API
            if (index < combinationsToTry.size - 1) {
                delay(500) // Adjust the delay duration as needed (in milliseconds)
            }

            connection.disconnect()
        }

        // If no successful response is found, callback with empty string
        callback("We apologize, but there is no further information available about the object at this time. " +
                "If you have any other questions or need assistance with something else, please don't hesitate to reach out to us. " +
                "We're here to help!")
    }
}

fun generateCombinations(words: List<String>): List<String> {
    val combinations = mutableListOf<String>()
    val n = words.size
    for (i in n downTo 1) {
        for (j in 0..(n - i)) {
            combinations.add(words.subList(j, j + i).joinToString(" "))
        }
    }
    return combinations
}

//----- Flight API -----
fun flightJson(url: String, processJson: (String) -> Unit) {
    val request = Request.Builder()
        .url(url)
        .build()
    Log.i("API FETCH", url)
    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            Log.i("API FETCH ERROR", e.stackTraceToString())
        }

        override fun onResponse(call: Call, response: Response) {
            try {
                val jsonData = response.body?.string()
                processJson(jsonData ?: "")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i("Error fetching data", e.toString())
            }
        }
    })
}