package com.example.michael.roboticscontrol;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    NetworkConnection client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        client = new NetworkConnection(this);
        Button netButton = (Button) findViewById(R.id.NetworkButton);
        Button talkButton = (Button) findViewById(R.id.talkButton);
        Button listenButton = (Button) findViewById(R.id.listenButton);
        netButton.setOnClickListener(this);
        listenButton.setOnClickListener(this);
        talkButton.setOnClickListener(this);
    }


    /**
     * Method to control what happens when a button in this activity is pressed.
     * @param v - what specifically was touched.
     */
    public void onClick(View v) {
        Log.v("**Log**", "Button Pressed");
        switch(v.getId()) {
            case R.id.talkButton: ;
                startActivity(new Intent(MainActivity.this, TTSActivity.class));
                break;

            case R.id.listenButton:
                startActivity(new Intent(MainActivity.this, STTActivity.class));
                break;

            case R.id.NetworkButton:
                client.setThingsUP();
        }

    }





    public static String getIpAddress() {
        String ipAddress = "Unable to Fetch IP..";
        try {
            Enumeration en;
            en = NetworkInterface.getNetworkInterfaces();
            while ( en.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface)en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()&&inetAddress instanceof Inet4Address) {
                        ipAddress=inetAddress.getHostAddress().toString();
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return ipAddress;
    }


}
