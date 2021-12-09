package com.example.mynextmeal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class Browser extends AppCompatActivity {
    WebView browser;
    String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        browser = findViewById(R.id.webview);
        URL = getIntent().getStringExtra("url");
        browser.loadUrl("http://www.google.com?q="+URL);
    }
}