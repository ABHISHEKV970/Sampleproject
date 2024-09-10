package com.example.sampleapp.View

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.sampleapp.R

class WebViewActivity : AppCompatActivity() {

    var articleUrl = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        try {
            articleUrl = intent.getStringExtra("url").toString()
            val webView: WebView = findViewById(R.id.webview)
            webView.webViewClient = WebViewClient()
            webView.loadUrl(articleUrl)
        } catch (e: Exception) {
            Log.d("ERROR", e.toString())
        }
    }



}