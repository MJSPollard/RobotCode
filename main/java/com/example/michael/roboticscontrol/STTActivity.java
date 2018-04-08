package com.example.michael.roboticscontrol;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class STTActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView speechText;
    private Button speechButton;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    NetworkConnection client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stt);

        client = new NetworkConnection(this);
        speechText = (TextView) findViewById(R.id.inputSpeech);
        speechButton = (Button) findViewById(R.id.promptListenButton);
        speechButton.setOnClickListener(this);
    }

    public void onClick(View v){
        promptUserForSpeech();
    }

    public void promptUserForSpeech() {
        Intent getSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        getSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        getSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        getSpeech.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "SAY WORDS");
        try {
            startActivityForResult(getSpeech, REQ_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            System.out.println("******ACTIVITY ERROR******");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speechText.setText(result.get(0));
                    client.startCommand(result.get(0));
                }
                break;
            }

        }
    }
}
