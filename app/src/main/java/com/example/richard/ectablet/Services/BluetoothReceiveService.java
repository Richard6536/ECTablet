package com.example.richard.ectablet.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.richard.ectablet.Activity.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

public class BluetoothReceiveService extends Service {

    private static String btAdress = "00:00:00:00:00:00";
    private static final UUID MY_UUID = UUID.fromString("08C2B2EF-7C87-3D00-0CDC-9A2ADC420BFF");
    public BluetoothDevice device;

    private static final int DISCOVERABLE_REQUEST_CODE = 0x1;
    private boolean CONTINUE_READ_WRITE = true;

    public BluetoothReceiveService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this)

                .setContentTitle("My Awesome App")
                .setContentText("Doing some work...")
                .setContentIntent(pendingIntent).build();
        startForeground(1337, notification);

        new Thread(reader).start();
    }

    private BluetoothSocket socket;
    private InputStream is;
    private OutputStreamWriter os;

    private Runnable reader = new Runnable() {
        public void run() {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            try {
                BluetoothServerSocket serverSocket = adapter.listenUsingRfcommWithServiceRecord("RosieProject", MY_UUID);
                Log.d("BT","Listening...");
                Log.d("BT","Socket accepted...");

                int bufferSize = 1024;
                int bytesRead = -1;
                byte[] buffer = new byte[bufferSize];
                Log.d("BT","Keep reading the messages while connection is open...");
                //Keep reading the messages while connection is open...
                while(CONTINUE_READ_WRITE){
                    socket = serverSocket.accept();
                    //addViewOnUiThread("TrackingFlow. Socket accepted...");
                    is = socket.getInputStream();
                    os = new OutputStreamWriter(socket.getOutputStream());

                    final StringBuilder sb = new StringBuilder();
                    bytesRead = is.read(buffer);
                    if (bytesRead != -1) {
                        String result = "";
                        while ((bytesRead == bufferSize) && (buffer[bufferSize-1] != 0)){
                            result = result + new String(buffer, 0, bytesRead - 1);
                            bytesRead = is.read(buffer);
                        }
                        result = result + new String(buffer, 0, bytesRead - 1);
                        sb.append(result);

                        os = new OutputStreamWriter(socket.getOutputStream());
                        os.write("ok");

                        /*
                        int numb = Integer.parseInt(result);
                        if(numb % 10 == 0){
                            os.write("No se permite el numero " + numb);
                        }
                        else{
                            os.write("ok");
                        }*/
                        os.flush();
                        os.close();


                        is.close();
                        socket.close();
                    }

                    Log.d("BT","Read: " + sb.toString());

                    sendMessageToActivity(sb.toString());
                    //addViewOnUiThread("TrackingFlow. Read: " + sb.toString());
                    //Show message on UIThread
                    /*
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mensaje.setText(sb.toString());
                            //Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();
                        }
                    });*/
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("BT","Error: " + e.getMessage());
            }
        }
    };

    private void sendMessageToActivity(String msg) {
        Intent intent = new Intent("intentKey");
        // You can also include some extra data.
        intent.putExtra("key", msg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
