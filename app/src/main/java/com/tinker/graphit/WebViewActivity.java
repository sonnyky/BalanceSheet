package com.tinker.graphit;

/**
 * Created by SY on 2016/07/01.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {

    private WebView webView;
    private String urlToLoad;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        Intent callingIntent = getIntent();
        urlToLoad = callingIntent.getStringExtra("url");
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(urlToLoad);
        webView.setWebViewClient(new WebViewClient());
    }

}