package com.example.michael.roboticscontrol;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class NetworkConnection extends Thread {


    TTS tts;
    private Context context;

    // need to put this in a thread to constantly listen
    // host name and port number must match python server
    static String HOST = "10.200.44.202";
    static int PORT = 8089;
    static Socket myClient;
    DataInputStream input; // used to create an input stream to receive response
    // from the server
    static DataOutputStream output;
    static BufferedReader inputString;
    char inputChar = '6';


    NetworkConnection(Context context) {
        this.context = context;
        tts = new TTS(context);
        tts.onInit(1);
        tts.start();
    }

    public void setThingsUP() {
        try {

            myClient = new Socket(HOST, PORT); // opens a client socket
            inputString = new BufferedReader(new InputStreamReader(
                    myClient.getInputStream()));
            output = new DataOutputStream(myClient.getOutputStream());

            //start the listening thread
            (new NetworkConnection(this.context)).start();

            while (true) {
                String dec = "y";
                if (dec.equals("y")) {
                    String s = "hello";
                    byte[] b = s.getBytes("UTF-8");
                    output.write(b);
                    output.flush();
                    break;
                } else {
                    break;
                }
            }
            // inputString.close();
            // output.close();
            // myClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startCommand(String command) {
        try {
            byte[] b = command.getBytes("UTF-8");
            output.write(b);
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Thread that listens for messages from the python server
     */
    public void run() {
        try {
            input = new DataInputStream(myClient.getInputStream());
            while (true) {
                // System.out.println("Server: " + inputChar);
                if ((inputChar = (char) input.readByte()) != '6') {
                    System.out.println("Server: " + inputChar);
                    String wordsToSay = "Error, try again";
                    if (inputChar == '1') {
                        wordsToSay = "Yes Master, Anything for you";
                    }
                    if (inputChar == '2') {
                        wordsToSay = "There is nothing here";
                    }
                    if (inputChar == '3') {
                        wordsToSay = "I don't want to live this life anymore";
                    }
                    if (inputChar == '4') {
                        wordsToSay = "What is my purpose?";
                    }
                    Message sendMsg = tts.handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("TT", wordsToSay);
                    sendMsg.setData(b);
                    tts.handler.sendMessage(sendMsg);
                }
                inputChar = '6'; //set back to default val
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
