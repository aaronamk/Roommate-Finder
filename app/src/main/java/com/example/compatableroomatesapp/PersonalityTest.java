package com.example.compatableroomatesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class PersonalityTest extends AppCompatActivity implements View.OnClickListener{

    private WebView webView;
    private EditText editPersonalityType;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personality_test);

        webView = (WebView) findViewById(R.id.personalityTestLink);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://openpsychometrics.org/tests/OEJTS/1.php");

        editPersonalityType = (EditText) findViewById(R.id.editPersonalityType);

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.nextButton:
                updatePersonality();
                break;
        }
    }

    private void updatePersonality()
    {
        final String personalityType = editPersonalityType.getText().toString().trim();

        if(personalityType.isEmpty())
        {
            editPersonalityType.setError("Personality Type is required!");
            editPersonalityType.requestFocus();
        }
    }
}