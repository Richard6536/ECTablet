package com.example.richard.ectablet.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.richard.ectablet.Adapters.CardviewAutosAdapter;
import com.example.richard.ectablet.Adapters.SpinnerAdapter;
import com.example.richard.ectablet.Clases.ControllerActivity;
import com.example.richard.ectablet.Clases.HideStatusBarNavigation;
import com.example.richard.ectablet.Clases.SessionManager;
import com.example.richard.ectablet.R;
import com.example.richard.ectablet.Services.LocationService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SeleccionarVehiculoActivity extends AppCompatActivity {

    HideStatusBarNavigation hideStatusBarNavigation = new HideStatusBarNavigation();
    public View mContentView;
    public RecyclerView recyclerView_autos;
    public int autoIdSeleccionado, flotaId;

    public String llave, patente;
    public String txtNombreUsuario, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_vehiculo);
        ControllerActivity.activiyAbiertaActual = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mContentView = findViewById(R.id.container);

        flotaId = getIntent().getIntExtra("flotaId",0);
        txtNombreUsuario = getIntent().getStringExtra("txtNombreUsuario");
        txtPassword = getIntent().getStringExtra("txtPassword");
        String listaAutos = getIntent().getStringExtra("listaAutos");

        try{
            recyclerView_autos = (RecyclerView)findViewById(R.id.recyclerView_autos);
            recyclerView_autos.setHasFixedSize(true);
            GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
            //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView_autos.setLayoutManager(mGridLayoutManager);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        agregarVehiculosASpinner(listaAutos);
    }

    @Override
    protected void onResume() {
        super.onResume();

        hideStatusBarNavigation.hideUI(mContentView, getSupportActionBar());
    }

    public void agregarVehiculosASpinner(String listaAutos)
    {
        try {
            JSONArray lista = new JSONArray(listaAutos);
            ArrayList<SpinnerAdapter> listVehiculosAdapter = new ArrayList<>();
            for(int x = 0; x <lista.length(); x++)
            {
                JSONObject proveedor = null;

                try {
                    proveedor = lista.getJSONObject(x);
                    autoIdSeleccionado = proveedor.getInt("Id");
                    String nombre = proveedor.getString("NombreVehiculo");
                    String patente = proveedor.getString("Patente");
                    listVehiculosAdapter.add(new SpinnerAdapter(autoIdSeleccionado, nombre, patente));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            CardviewAutosAdapter adapter = new CardviewAutosAdapter(getApplicationContext(), listVehiculosAdapter, new CardviewAutosAdapter.OnItemClickListener() {
                @Override
                public void onItemClicked(int position, int itemPosition, SpinnerAdapter autoSeleccionado) {

                    autoIdSeleccionado = autoSeleccionado.getId();
                    //String patente = autoSeleccionado.getPatente();
                    //String nombreVehiculo = autoSeleccionado.getNombre();

                    Log.d("SeleccionVehiculo", "result: " + autoSeleccionado.getNombre());
                    //levantarSesion();


                    JSONObject datos = new JSONObject();

                    try {

                        datos.put("NombreUsuario", txtNombreUsuario);
                        datos.put("Password",txtPassword);
                        datos.put("FlotaId", flotaId);
                        datos.put("VehiculoId",autoIdSeleccionado);

                        //Enviar datos al webservice
                        new SessionManager.SeleccionarVehiculo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


            recyclerView_autos.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception a){
            a.printStackTrace();
        }
    }


    public void dataReceiveFromSessionManagerSeleccionarVehiculo(JSONObject str) {
        try {

            String respuesta = str.getString("TipoRespuesta");
            if(respuesta.equals("OK")){

                int vehiculoId = str.getInt("AutoId");
                patente = str.getString("Patente");
                flotaId = str.getInt("FlotaId");
                llave = str.getString("Llave");

                JSONObject datos = new JSONObject();

                try {

                    datos.put("NombreUsuario", txtNombreUsuario);
                    datos.put("Password",txtPassword);
                    datos.put("FlotaId", flotaId);
                    datos.put("VehiculoId",vehiculoId);
                    datos.put("Llave",llave);

                    //Enviar datos al webservice
                    new SessionManager.ConfirmacionLlave().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                //Ha ocurrido un error
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void dataReceiveFromSessionManagerConfirmacionAcceso(JSONObject str) {

        try {

            String respuesta = str.getString("TipoRespuesta");
            if(respuesta.equals("OK")){
                Toast.makeText(getApplicationContext(),"TODO "+ respuesta, Toast.LENGTH_LONG);
                SessionManager session = new SessionManager(getApplicationContext());

                session.levantarSesion(llave,autoIdSeleccionado,flotaId,txtNombreUsuario,patente,"");

                Intent intent = new Intent(SeleccionarVehiculoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                //Ha ocurrido un error
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
