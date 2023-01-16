package com.example.controling_robots;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.controling_robots.databinding.ActivityMainBinding;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    public static int port;
    public static String ip;

    private enum Direction {
        LEFT, RIGHT, UP, DOWN
    }

    public static void send(String ip, int port, byte[] data) throws IOException {
        InetAddress address = InetAddress.getByName(ip);
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        DatagramSocket socket = new DatagramSocket();
        socket.send(packet);
        socket.close();
    }

    public static void encode(Direction dir) throws IOException {
        switch (dir) {
            case LEFT:
                send(ip, port, "LEFT".getBytes(StandardCharsets.US_ASCII));
                break;
            case UP:
                send(ip, port, "UP".getBytes(StandardCharsets.US_ASCII));
                break;
            case DOWN:
                send(ip, port, "DOWN".getBytes(StandardCharsets.US_ASCII));
                break;
            case RIGHT:
                send(ip, port, "RIGHT".getBytes(StandardCharsets.US_ASCII));
                break;
            default:
                break;

        }
    }


    Button b_forward;
    Button b_back;
    Button b_right;
    Button b_left;
    Button enter;

    protected EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        b_forward = findViewById(R.id.b_forward);
        b_back = findViewById(R.id.b_back);
        b_left = findViewById(R.id.b_left);
        b_right = findViewById(R.id.b_right);
        enter = findViewById(R.id.enter);

        editText = (EditText) findViewById(R.id.edit_text);

        b_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread udpSend = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MainActivity.encode(Direction.UP);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                });
                udpSend.start();
            }
        });
        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread udpSend = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MainActivity.encode(Direction.DOWN);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                });
                udpSend.start();
            }
        });
        b_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread udpSend = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MainActivity.encode(Direction.RIGHT);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                });
                udpSend.start();
            }
        });
        b_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread udpSend = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MainActivity.encode(Direction.LEFT);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                });
                udpSend.start();
            }
        });



        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                editText.clearFocus();

                CharSequence s = editText.getText();
                // Split the string at the ":" character to get the ip and port values
                String[] values = s.toString().split(":");
                if (values.length != 2) {

                }

                MainActivity.ip = values[0];
                try{
                    MainActivity.port = Integer.parseInt(values[1]);
                }catch(Exception e){

                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}