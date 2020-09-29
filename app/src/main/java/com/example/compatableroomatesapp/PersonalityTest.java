package com.example.compatableroomatesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PersonalityTest extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personality_test);

        webView = (WebView) findViewById(R.id.personalityTestLink);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.truity.com/test/type-finder-personality-test-new");

    }
}