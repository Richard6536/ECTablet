package com.example.richard.ectablet.Activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.richard.ectablet.R;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.richard.ectablet.Clases.ActionBarActivity;
import com.example.richard.ectablet.Clases.Almacenamiento;
import com.example.richard.ectablet.Clases.HideStatusBarNavigation;
import com.example.richard.ectablet.Clases.MapBoxManager;
import com.example.richard.ectablet.Clases.SessionManager;
import com.example.richard.ectablet.Clases.Vehiculo;
import com.example.richard.ectablet.Fragments.d.BatteryFragment;
import com.example.richard.ectablet.Fragments.d.MapFragment;
import com.example.richard.ectablet.Fragments.d.StatsFragment;
import com.example.richard.ectablet.Services.BluetoothReceiveService;
import com.example.richard.ectablet.Services.LocationService;
import com.suke.widget.SwitchButton;

import org.json.JSONArray;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.UUID;

public class MainActivity extends AppCompatActivity{

    private static final int DISCOVERABLE_REQUEST_CODE = 0x1;

    HideStatusBarNavigation hideStatusBarNavigation = new HideStatusBarNavigation();
    public View mContentView;

    public TextView kmVelocidadText;

    final MapFragment mapFragment = new MapFragment();
    final BatteryFragment batteryFragment = new BatteryFragment();
    final StatsFragment statsFragment = new StatsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = mapFragment;

    private MapBoxManager mapBoxBManager = new MapBoxManager();
    SessionManager sessionController;

    //todo:*************** PERMISOS: SERÁN REMOVIDOS A UNA CLASE A PARTE MÁS ADELANTE
    int PERMISSION_ALL = 1;
    private static String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE ,
            Manifest.permission.ACCESS_FINE_LOCATION};
    //todo:*************** PERMISOS: SERÁN REMOVIDOS A UNA CLASE A PARTE MÁS ADELANTE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //Mapbox Access token
        mapBoxBManager.getMapBoxAccessToken(getApplicationContext());
        sessionController = new SessionManager(getApplicationContext());
        mContentView = findViewById(R.id.container);

        ActionBarActivity actionBarActivity = new ActionBarActivity();
        actionBarActivity.view = mContentView;
        actionBarActivity.actionBar = getSupportActionBar();


        /*
        com.suke.widget.SwitchButton switchButton = (com.suke.widget.SwitchButton)
                findViewById(R.id.switch_button);

        switchButton.setChecked(true);
        switchButton.isChecked();
        switchButton.toggle();     //switch state
        switchButton.toggle(true);//switch without animation
        switchButton.setShadowEffect(true);//disable shadow effect
        switchButton.setEnabled(true);//disable button
        switchButton.setEnableEffect(true);//disable the switch animation
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
            }
        });
*/
        ImageView iv = (ImageView) findViewById(R.id.vector_battery_status);
        iv.animate().rotation(90).start();

        kmVelocidadText = (TextView) findViewById(R.id.txtKmVelocidad);

        int width = 120;
        int height = 90;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
        iv.setLayoutParams(params);

        //Define navegación inferior e iconos en la vista principal
        DefineBottomNavigationView();

        //Se agregan los fragments al FragmentManager
        AddFragmentsToBeginTransaction();


        //Se inicia LocalBroadcastManager para que el BluetoothReceiveService envie datos a la actividad
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("intentKey"));

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiverPosition, new IntentFilter("intentKey2"));

        //Se solicitan permisos al usuario para hacer uso del Bluetooth
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivityForResult(discoverableIntent, DISCOVERABLE_REQUEST_CODE);

        enableLocationPlugin();

    }

    private void AddFragmentsToBeginTransaction(){
        fm.beginTransaction().add(R.id.container, statsFragment, "3").hide(statsFragment).commit();
        fm.beginTransaction().add(R.id.container, batteryFragment, "2").hide(batteryFragment).commit();
        fm.beginTransaction().add(R.id.container,mapFragment, "1").commit();
    }
    private void DefineBottomNavigationView(){
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {

            final View iconView = menuView.getChildAt(i).findViewById(com.google.android.material.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, displayMetrics);
            iconView.setLayoutParams(layoutParams);

        }

       /*
        navigation.setRotation(90f);
        //navigation.getLayoutParams().width=480;
        navigation.requestLayout();
        navigation.setY(-500f);
        navigation.setX(-910f);
        // navigation.requestLayout();
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i);
            iconView.setRotation(-90f);
        }
        */
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_map:
                    fm.beginTransaction().hide(active).show(mapFragment).commit();
                    active = mapFragment;
                    return true;

                case R.id.action_battery:
                    fm.beginTransaction().hide(active).show(batteryFragment).commit();
                    active = batteryFragment;
                    return true;

                case R.id.action_stats:
                    fm.beginTransaction().hide(active).show(statsFragment).commit();
                    active = statsFragment;
                    return true;
            }
            return false;
        }
    };


    private void enableLocationPlugin() {
        // Check if permissions are enabled and if not request

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED || permissionLocation != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        else
        {
            //Inicializa Mapas, posición & crea directorios si los permisos se han concedido
            InicializarDirectorios();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        InicializarDirectorios();
    }

    private Intent getNotificationIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    public void InicializarDirectorios(){

        HashMap<String, String> datos = sessionController.obtenerDatosUsuario();
        String vehiculoId = datos.get(SessionManager.KEY_VEHICULOID);

        Almacenamiento.crearDirectorio(vehiculoId);
        JSONArray listaPosicion = Almacenamiento.leerArchivo(vehiculoId, Almacenamiento.myFilePosicion.toString(), Almacenamiento.filePosicion);

        try {
            if (listaPosicion != null) {
                Vehiculo.listaDatosAEnviar = listaPosicion;
            }

        } catch (Exception e) {
            String err = e.getMessage();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onResume() {
        super.onResume();

        hideStatusBarNavigation.hideUI(mContentView, getSupportActionBar());

        Intent locationIntent = new Intent(getBaseContext(), LocationService.class);
        startService(locationIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //TODO: START BLUETOOTH
    //****************BLUETOOTH******************

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //addViewOnUiThread("TrackingFlow ");
        Log.d("BT","Creating thread to start listening...");

        //Intent intent = new Intent(getBaseContext(), LocationService.class);
        Intent intent = new Intent(getBaseContext(), BluetoothReceiveService.class);
        startService(intent);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Recibe datos directamente desde el BluetoothReceiveService
            //Se envían a los fragments correspondientes

            String voltaje = intent.getStringExtra("VOLTAJE");
            String corriente = intent.getStringExtra("CORRIENTE");
            String estimacionsompa = intent.getStringExtra("ESTIMACIONSOMPA");
            String confintervalsompa1 = intent.getStringExtra("CONFINTERVALSOMPA1");
            String confintervalsompa2 = intent.getStringExtra("CONFINTERVALSOMPA2");
            String fecha = intent.getStringExtra("FECHA");

            Bundle args = new Bundle();
            args.putString("VOLTAJE", voltaje);
            args.putString("CORRIENTE", corriente);
            args.putString("ESTIMACIONSOMPA", estimacionsompa);
            args.putString("CONFINTERVALSOMPA1", confintervalsompa1);
            args.putString("CONFINTERVALSOMPA2", confintervalsompa2);

            args.putString("FECHA", fecha);

            statsFragment.putArguments(args);

        }
    };

    private BroadcastReceiver mMessageReceiverPosition = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Recibe datos directamente desde el BluetoothReceiveService
            //Se envían a los fragments correspondientes

            String velocidad = intent.getStringExtra("VELOCIDAD");
            kmVelocidadText.setText(velocidad);
        }
    };

    //****************BLUETOOTH******************
    //TODO: END BLUETOOTH
}