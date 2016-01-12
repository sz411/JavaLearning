package com.fang.auctionclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fang.auctionclient.R;
import com.fang.auctionclient.ui_base.BaseActivity;

/**
 * Created by Administrator on 2015/12/30.
 */
public class WebActivity extends BaseActivity{

    WebView mWebView;
    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_index);
        closeActionBar();
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        mWebView = (WebView) findViewById(R.id.webView);
        mIntent = getIntent();
    }

    @Override
    protected void initView() {
        String url_extra=mIntent.getStringExtra("url");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl(url_extra);
    }
}
