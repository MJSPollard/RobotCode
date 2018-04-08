package com.example.michael.roboticscontrol;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TTSActivity extends AppCompatActivity implements View.OnClickListener {
    EditText talkText;
    TTS tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tts);
        talkText = (EditText) findViewById(R.id.talkText);
        Button makeTalkButton = (Button) findViewById(R.id.makeTalkButton);
        makeTalkButton.setOnClickListener(this);
        tts = new TTS(this);
        tts.start();
    }

    /**
     * Method to control what happens when a button in this activity is pressed.
     * @param v - what specifically was touched.
     */
    public void onClick(View v) {
        Toast.makeText(this, "onClick", Toast.LENGTH_SHORT).show();
        switch (v.getId()) {
            case R.id.makeTalkButton:
                String input = talkText.getText().toString();
                Message sendMsg = tts.handler.obtainMessage();
                Bundle b = new Bundle();
                b.putString("TT", input);
                sendMsg.setData(b);
                tts.handler.sendMessage(sendMsg);
                break;
        }
    }
}
