package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FinActivity extends AppCompatActivity {

    private int score;
    private int nbQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        nbQuestion = intent.getIntExtra("nbQuestion", 0);

        //affichage d'une page web entre les 2 boutons
        String url = "http://quandjepasselebac.education.fr/category/se-preparer-a-lapres-bac/";
        WebView maWebView = findViewById(R.id.maWebView);
        maWebView.setWebViewClient(new WebViewClient());
        maWebView.loadUrl(url);
    }

    @SuppressLint("StringFormatInvalid")
    public void partageScore(View view) {
        Intent versAppMessage = new Intent(Intent.ACTION_SENDTO);
        versAppMessage.setData(Uri.parse("smsto:"));    //protocol
        versAppMessage.putExtra("sms_body", String.format(getString(R.string.messageToFriend), score, nbQuestion));
        Intent choixAppMessage = Intent.createChooser(versAppMessage, getString(R.string.ChooseAppMessage));
        startActivity(choixAppMessage);
    }

    public void quitterApp(View view) {
        finish();
        System.exit(0);
    }
}