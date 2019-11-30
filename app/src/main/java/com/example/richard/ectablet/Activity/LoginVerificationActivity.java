package com.example.richard.ectablet.Activity;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.richard.ectablet.Clases.ControllerActivity;
import com.example.richard.ectablet.Clases.SessionManager;
import com.example.richard.ectablet.R;

public class LoginVerificationActivity extends AppCompatActivity {

    SessionManager sessionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_verification);
        sessionController = new SessionManager(getApplicationContext());
        ControllerActivity.activiyAbiertaActual = this;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //registerReceiver(receiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));

        if(sessionController.checkLogin() == true) {

            sessionController.levantarSesion("",0,0,"","","true");
            Intent intent = new Intent(LoginVerificationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
        else {
            Intent intent = new Intent(LoginVerificationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
