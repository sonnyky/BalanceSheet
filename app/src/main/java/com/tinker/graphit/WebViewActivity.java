package com.tinker.graphit;

/**
 * Created by SY on 2016/07/01.
 */
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://ancient-harbor-5436.herokuapp.com/howto_graphit");
        webView.setWebViewClient(new WebViewClient());
    }

}