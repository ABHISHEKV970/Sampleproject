package com.example.sampleapp.View

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleapp.Models.Article
import com.example.sampleapp.Network.FetchData
import com.example.sampleapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var newsRv: RecyclerView
    private lateinit var adapter: NewsAdapter
    private lateinit var sortitems: MutableList<Article>
    private var isSorted = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val filter = findViewById<FloatingActionButton>(R.id.sort)

        newsRv = findViewById(R.id.news_Rv)
        newsRv.layoutManager = LinearLayoutManager(this)
        adapter = NewsAdapter()
        newsRv.adapter = adapter


        try {
            if (checkForInternet(this)) {
                api_call()
                filter.setOnClickListener {
                    sortIt()
                }
            } else {
                Toast.makeText(this, "No Internet Connection Found !", Toast.LENGTH_SHORT).show()
            }
        }
        catch (e : Exception)
        {
            Log.i("error",e.toString())
        }

    }

    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    private fun api_call() {

        val fetchData = FetchData { newsList ->
            withContext(Dispatchers.Main) {
                adapter.setData(newsList)
                sortitems = mutableListOf()
                for (article in newsList) {
                    val newArticle = Article(
                        article.author,
                        article.content,
                        article.description,
                        article.publishedAt,
                        article.source,
                        article.title,
                        article.url,
                        article.urlToImage,
                    )
                    sortitems.add(newArticle)
                }
            }
        }

        GlobalScope.launch {
            fetchData.fetchData()
        }

    }


    private fun sortIt() {

        isSorted = !isSorted
        val sortedItems = if (isSorted) {
            sortitems.sortedBy { it.publishedAt }
        } else {
            sortitems
        }
        adapter.sortData(sortedItems)
    }


    override fun onRestart() {
        super.onRestart()
        if (checkForInternet(this)) {
            api_call()
        } else {
            Toast.makeText(this, "No Internet Connection Found !", Toast.LENGTH_SHORT).show()
        }    }

}