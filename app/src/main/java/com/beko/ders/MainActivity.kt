package com.beko.ders

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

// Beko Ders - AOF study app. The whole app (cards, quizzes, official AOF video links) ships inside
// the APK as assets/index.html, so it works offline; video links open in the YouTube app.
class MainActivity : Activity() {
    private lateinit var web: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        web = WebView(this)
        with(web.settings) { javaScriptEnabled = true; domStorageEnabled = true; textZoom = 100 }
        web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(v: WebView, r: WebResourceRequest): Boolean {
                val u = r.url
                if (u.scheme == "file") return false
                startActivity(Intent(Intent.ACTION_VIEW, u)); return true   // YouTube, web: outside the app
            }
        }
        // target=_blank links also come here
        web.settings.setSupportMultipleWindows(false)
        setContentView(web)
        if (b != null) web.restoreState(b) else web.loadUrl("file:///android_asset/index.html")
    }

    override fun onSaveInstanceState(o: Bundle) { super.onSaveInstanceState(o); web.saveState(o) }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // the page manages its own screens; back key = go to home screen of the page, then leave
        web.evaluateJavascript("(function(){try{if(document.querySelector('.hdr .ib[aria-label=Geri]')){home();return 1}}catch(e){}return 0})()") { r ->
            if (r != "1") moveTaskToBack(true)
        }
    }
}
