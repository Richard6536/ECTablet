package com.example.richard.ectablet.Activity;

import android.hardware.usb.UsbDevice;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.richard.ectablet.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import me.aflak.arduino.Arduino;
import me.aflak.arduino.ArduinoListener;

public class ArduinoTestActivity extends AppCompatActivity implements ArduinoListener {

    public Arduino arduino;
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino_test);
        textView = (TextView)findViewById(R.id.txtMensaje);

        arduino = new Arduino(this);

        display("Ok");
    }

    @Override
    protected void onStart() {
        super.onStart();
        arduino.setArduinoListener(this);
    }

    @Override
    public void onArduinoAttached(UsbDevice device) {
        display("Arduino attached!");
        arduino.open(device);
    }

    @Override
    public void onArduinoDetached() {

    }

    @Override
    public void onArduinoMessage(byte[] bytes) {
        display("Received: "+new String(bytes));
    }

    @Override
    public void onArduinoOpened() {
        String str = "Hello World !";
        arduino.send(str.getBytes());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        arduino.unsetArduinoListener();
        arduino.close();
    }

    public void display(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append(message+"\n");
            }
        });
    }

    public void getDataFromPIWifi (){

        /*
                from RaspberryPI ****************

                import socket
                import time

                UDP_IP = "192.168.43.160"
                UDP_PORT = 5005

                test_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

                last_time = 0
                running = True
                while running:
                    try:
                        current_time = time.time()
                        if(current_time > last_time + 2):
                            last_time = current_time
                            test_socket.connect(("192.168.43.160", 80))
                            test_socket.sendto("WHOOP", (UDP_IP, UDP_PORT))
                            print("sent")

                    except Exception:
                        running = False
                        test_socket.close()
                        raise
                test_socket.close()
        * */
        new Thread(new Runnable() {
            public void run() {
                try {
                    DatagramSocket clientsocket= new DatagramSocket(5005);
                    byte[] receivedata = new byte[5];
                    while(true) {
                        DatagramPacket recv_packet = new DatagramPacket(receivedata, receivedata.length);

                        Log.d("UDP", "S: Receiving...");
                        clientsocket.receive(recv_packet);
                        String receivedstring = new String(recv_packet.getData());
                        Log.d("UDP", " Received String: " + receivedstring);
                        InetAddress ipaddress = recv_packet.getAddress();
                        int port = recv_packet.getPort();
                        Log.d("UDP", "IPAddress : " + ipaddress.toString());
                        Log.d("UDP", "Port : " + Integer.toString(port));
                    }
                } catch (SocketException e) {
                    Log.e("UDP", "Socket Error", e);
                } catch (IOException e) {
                    Log.e("UDP", "IO Error", e);
                }
            }
        }).start();
    }
}
