package com.total.eyecare.ui.camera

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.total.eyecare.R

class DetailCameraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_camera)
        val ip: String = intent.getStringExtra("ip").toString()
        setCamera(ip)
    }
    private fun setCamera(ip: String){
        val webView =findViewById<WebView>(R.id.webView)
        val progressBar = findViewById<ProgressBar>(R.id.loading)


        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                view.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                view.visibility =View.VISIBLE
                progressBar.visibility = View.INVISIBLE
            }

        }
        webView.settings.javaScriptEnabled = true

        val settings = webView.settings
        settings.domStorageEnabled = true

        webView.loadUrl(ip)
    }
}