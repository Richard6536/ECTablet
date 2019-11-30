package com.example.richard.ectablet.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.example.richard.ectablet.Activity.MainActivity;
import com.example.richard.ectablet.Clases.Vehiculo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

public class BluetoothReceiveService extends Service {

    private static String btAdress = "00:00:00:00:00:00";
    private static final UUID MY_UUID = UUID.fromString("08C2B2EF-7C87-3D00-0CDC-9A2ADC420BFF");
    public BluetoothDevice device;
    BluetoothServerSocket serverSocket;

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
                serverSocket = adapter.listenUsingRfcommWithServiceRecord("RosieProject", MY_UUID);
                Log.d("0092bluet","Listening...");
                Log.d("0092bluet","Socket accepted...");

                int bufferSize = 1008;
                int bytesRead = -1;
                int bytesFinalRead = 0;

                byte[] buffer = new byte[bufferSize];
                Log.d("0092bluet","Keep reading the messages while connection is open...");
                //Keep reading the messages while connection is open...
                while(CONTINUE_READ_WRITE){
                    socket = serverSocket.accept();
                    //addViewOnUiThread("TrackingFlow. Socket accepted...");
                    is = socket.getInputStream();
                    String result = "";
                    final StringBuilder sb = new StringBuilder();

                    bytesRead = is.read(buffer);
                    if (bytesRead != -1) {

                        while (bytesRead == bufferSize){
                            result = result + new String(buffer, 0, bytesRead);
                            bytesRead = is.read(buffer);
                        }

                        result = result + new String(buffer, 0, bytesRead);
                        sb.append(result);

                        os = new OutputStreamWriter(socket.getOutputStream());
                        os.write("ok");

                        os.flush();
                        os.close();

                    }

                    Log.d("0092bluet","Read: " + sb.toString());

                    try {

                        JSONArray jsonArrayEnviar = new JSONArray();
                        JSONArray jsonArray = new JSONArray(sb.toString());
                        String voltaje = "";
                        String fecha = "";

                        for(int x = 0; x <jsonArray.length(); x++){
                            String str = jsonArray.getString(x);
                            JSONObject jsonObject = new JSONObject(str);

                            fecha = jsonObject.getString("Fecha");
                            String estimacionSoc = jsonObject.getString("EstimacionSoc");
                            String confIntervalSoc1 = jsonObject.getString("ConfIntervalSoc1");
                            String confIntervalSoc2 = jsonObject.getString("ConfIntervalSoc2");
                            String estimacionSompa = jsonObject.getString("EstimacionSompa");
                            String confIntervalSompa1 = jsonObject.getString("ConfIntervalSompa1");
                            String confIntervalSompa2 = jsonObject.getString("ConfIntervalSompa2");
                            String estimacionRin = jsonObject.getString("EstimacionRin");
                            String confIntervalRin1 = jsonObject.getString("ConfIntervalRin1");
                            String confIntervalRin2 = jsonObject.getString("ConfIntervalRin2");
                            String corriente = jsonObject.getString("Corriente");
                            voltaje = jsonObject.getString("Voltaje");

                            JSONObject jsonObjectEnviar = new JSONObject();
                            jsonObjectEnviar.put("FechaHoraString", fecha);
                            jsonObjectEnviar.put("EstimacionSoc", estimacionSoc);
                            jsonObjectEnviar.put("ConfIntervalSoc1", confIntervalSoc1);
                            jsonObjectEnviar.put("ConfIntervalSoc2", confIntervalSoc2);
                            jsonObjectEnviar.put("EstimacionSompa", estimacionSompa);
                            jsonObjectEnviar.put("ConfIntervalSompa1", confIntervalSompa1);
                            jsonObjectEnviar.put("ConfIntervalSompa2", confIntervalSompa2);
                            jsonObjectEnviar.put("EstimacionRin", estimacionRin);
                            jsonObjectEnviar.put("ConfIntervalRin1", confIntervalRin1);
                            jsonObjectEnviar.put("ConfIntervalRin2", confIntervalRin2);
                            jsonObjectEnviar.put("Corriente", corriente);
                            jsonObjectEnviar.put("Voltaje", voltaje);

                            jsonArrayEnviar.put(jsonObjectEnviar);

                        }

                        int v = 0;
                        new Vehiculo.ActualizarPosicion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, jsonArrayEnviar.toString());
                        sendMessageToActivity(voltaje, fecha);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                is.close();
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("0092bluet","Error: " + e.getMessage());
            }
        }
    };


    private void sendMessageToActivity(String voltaje, String fecha) {
        Intent intent = new Intent("intentKey");
        // You can also include some extra data.
        intent.putExtra("VOLTAJE", voltaje);
        intent.putExtra("FECHA", fecha);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
