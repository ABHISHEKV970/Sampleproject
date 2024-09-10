package com.example.sampleapp.Network

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.sampleapp.Models.Article
import com.example.sampleapp.Models.Source
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class FetchData(private val callback: suspend (List<Article>) -> Unit) {
    suspend fun fetchData() {

        val result = withContext(Dispatchers.IO) {
            val url =
                URL("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json")
            val connection = url.openConnection() as HttpURLConnection
            val result = StringBuilder()

            try {
                val reader = connection.inputStream.bufferedReader()
                reader.forEachLine { result.append(it) }
                reader.close()
            } finally {
                connection.disconnect()
            }

            result.toString()
        }

        val articles = mutableListOf<Article>()

        try {
            val jsonObject = JSONObject(result)
            val jsonArray = jsonObject.getJSONArray("articles")

            for (i in 0 until jsonArray.length()) {
                val jsonObj = jsonArray.getJSONObject(i)

                val author = jsonObj.getString("author")
                val content = jsonObj.getString("content")
                val description = jsonObj.getString("description")
                val publishedAt = jsonObj.getString("publishedAt")

                val sourceObj = jsonObj.getJSONObject("source")
                val sourceId = sourceObj.getString("id")
                val sourceName = sourceObj.getString("name")
                val source = Source(sourceId, sourceName)

                val title = jsonObj.getString("title")
                val url = jsonObj.getString("url")
                val urlToImage = jsonObj.getString("urlToImage")

                val article = Article(
                    author,
                    content,
                    description,
                    publishedAt,
                    source,
                    title,
                    url,
                    urlToImage
                )
                articles.add(article)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        withContext(Dispatchers.Main) {
            callback(articles)
        }
    }
}