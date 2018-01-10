/**
 * Copyright Venuetize 2017
 * <EmWebViewActivity>
 * created by Venuetize
 */
package com.venue.initv2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class EmWebViewActivity extends Activity {

    public String web_url = "";
    String event_title = "";
    String link_type = "";
    public static Context ctx;
    WebView webview;
    TextView emkit_webview_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emkit_webview);
        emkit_webview_title = (TextView) findViewById(R.id.emkit_webview_title);

        ctx = this;
        webview = (WebView) findViewById(R.id.webview);

        if (getIntent() != null) {
            web_url = getIntent().getStringExtra("web_url");
            event_title = getIntent().getStringExtra("event_title");
            link_type = getIntent().getStringExtra("link_type");

        }
        setWebViewProperties();

    }

    private void setWebViewProperties() {


        WebSettings webSettings = webview.getSettings();

        //webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCachePath(getCacheDir().getAbsolutePath());
        webSettings.setAllowFileAccess(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setDatabasePath("/data/data/" + getPackageName()
                + "/databases/");
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setSupportZoom(false);
        webview.getSettings().setUseWideViewPort(true);
        webview.setVerticalScrollBarEnabled(true);
        webview.setHorizontalScrollBarEnabled(true);
        webview.setWillNotCacheDrawing(true);
        webview.setBackgroundColor(0xFFFFFFFF);
        webview.setWebViewClient(new MainWebViewClient());

        webview.loadUrl(web_url);

    }

    private class MainWebViewClient extends WebViewClient {

        public void onPageFinished(WebView webView, String url) {
            //pd.dismiss();
            if (webView != null && webView.getTitle() != null && webView.getTitle().trim().length() > 0)
                emkit_webview_title.setText(webView.getTitle());
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }
    }
}
