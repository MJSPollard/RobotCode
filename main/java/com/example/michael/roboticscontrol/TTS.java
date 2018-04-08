package com.example.michael.roboticscontrol;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class TTS extends Thread implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private Context context;
    public Handler handler;
    private String last;

    TTS(Context context) {
        this.context = context;
        tts = new TextToSpeech(context, this);
        last = "";

    }

    /**
     * Method that initializes the type of speech to be spoken
     * @param status
     */
    public void onInit(int status) {
        int result = 0;
        if (status == TextToSpeech.SUCCESS) {
            result = tts.setLanguage(Locale.US);
            tts.setPitch((float) 3);
            tts.setSpeechRate((float) 0);
        }

        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Toast.makeText(context, "Language or Data not working", Toast.LENGTH_LONG).show();
        }

    }


    @SuppressLint("HandlerLeak")
    public void run() {
        Looper.prepare();
        handler = new Handler() {
            public void handleMessage(Message msg) {
                String response = msg.getData().getString("TT");
                Log.v("*****SPEECH*****", response);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    speakOut(response);
                } else {
                    Toast.makeText(context, "Version Error 1", Toast.LENGTH_SHORT).show();
                }
            }
        };

        Looper.loop();
    }

    /**
     * Method that makes the program speak to the user.
     * @param text
     */
    public void speakOut(String text) {
        if(last != text) {
            last = text;

            //speak requires Android Version 21 or higher so it does a check and if true, speaks
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                Toast.makeText(context, "Version Error 2", Toast.LENGTH_SHORT).show();
            }

            //makes the program wait while it is speaking
            while (tts.isSpeaking()) try {
                Thread.sleep(200);
            } catch (Exception e) {
                System.out.println("Thread Error");
            }
        }

    }
}
