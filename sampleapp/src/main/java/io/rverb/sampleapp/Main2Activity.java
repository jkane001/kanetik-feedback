package io.rverb.sampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.rverb.feedback.Rverbio;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Rverbio.initialize(this, "jkane001@gmail.com");
    }

    public void sendHelpClicked(View v) {
        Rverbio.getInstance().sendHelp(this);
    }
}
