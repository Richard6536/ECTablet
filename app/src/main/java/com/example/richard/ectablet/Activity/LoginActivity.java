package com.example.richard.ectablet.Activity;

import android.content.Intent;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.richard.ectablet.Clases.ControllerActivity;
import com.example.richard.ectablet.Clases.HideStatusBarNavigation;
import com.example.richard.ectablet.Clases.SessionManager;
import com.example.richard.ectablet.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    HideStatusBarNavigation hideStatusBarNavigation = new HideStatusBarNavigation();
    public View mContentView;

    Button btnIniciarSesion;
    SessionManager sessionController;

    public EditText txtNombreUsuario, txtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ControllerActivity.activiyAbiertaActual = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mContentView = findViewById(R.id.container);

        txtNombreUsuario = (EditText)findViewById(R.id.txtNombreUsuario);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        btnIniciarSesion = (Button)findViewById(R.id.btnIniciarSesion);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtNombreUsuario.getText().toString().equals("") || txtPassword.getText().toString().equals("")){
                    //Debe llenar los campos
                }
                else{
                    JSONObject datos = new JSONObject();

                    try {

                        datos.put("NombreUsuario", txtNombreUsuario.getText());
                        datos.put("Password",txtPassword.getText());

                        //Enviar datos al webservice
                        new SessionManager.ValidarLogin().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, datos.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideStatusBarNavigation.hideUI(mContentView, getSupportActionBar());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {

        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    public void dataReceiveFromSessionManager(JSONObject str) {
        try {

            String respuesta = str.getString("TipoRespuesta");
            if(respuesta.equals("OK")){

                int flotaId = str.getInt("FlotaId");
                String listaAutos = str.getString("RespuestaArray");

                //viewPager.setCurrentItem(1);

                //Bundle args = new Bundle();
                //args.putInt("flotaId", flotaId);
                //args.putString("listaAutos", listaAutos);
                //seleccionarVehiculoFragment.putArguments(args);

                String nombreUsuario = txtNombreUsuario.getText().toString();
                String password = txtPassword.getText().toString();

                Intent intent = new Intent(LoginActivity.this, SeleccionarVehiculoActivity.class);
                intent.putExtra("flotaId", flotaId);
                intent.putExtra("txtNombreUsuario", nombreUsuario);
                intent.putExtra("txtPassword", password);
                intent.putExtra("listaAutos", listaAutos);
                startActivity(intent);

            }
            else{
                //Ha ocurrido un error
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
