package com.example.richard.ectablet.Clases;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.richard.ectablet.Activity.LoginActivity;
import com.example.richard.ectablet.Activity.SeleccionarVehiculoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "AndroidExamplePref";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static final String KEY = "llave";
    public static final String KEY_VEHICULOID = "vehiculoId";
    public static final String KEY_FLOTAID = "flotaId";
    public static final String KEY_NOMBRE_USUARIO = "nombreUsuario";
    public static final String KEY_PATENTE = "patente";
    public static final String KEY_ESTADO_RUTA = "estadoRuta";

    public SessionManager(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void levantarSesion(String llave, int vehiculoId, int flotaId, String nombreUsuario, String patente, String estadoRuta)
    {
        if(llave != null && !llave.isEmpty()){
            editor.putBoolean(IS_USER_LOGIN, true);
            editor.putString(KEY, llave);
            editor.putString(KEY_VEHICULOID, vehiculoId+"");
            editor.putString(KEY_FLOTAID, flotaId+"");
            editor.putString(KEY_NOMBRE_USUARIO, nombreUsuario);
            editor.putString(KEY_PATENTE, patente);
        }

        editor.putString(KEY_ESTADO_RUTA, estadoRuta);
        editor.commit();

    }

    public boolean checkLogin()
    {
        if(!this.isUserLoggedIn())
        {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);

            return false;
        }

        return true;
    }

    public boolean checkLogin2()
    {
        if(!this.isUserLoggedIn())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public HashMap<String, String> obtenerDatosUsuario()
    {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY, pref.getString(KEY, null));
        user.put(KEY_VEHICULOID, pref.getString(KEY_VEHICULOID, null));
        user.put(KEY_FLOTAID, pref.getString(KEY_FLOTAID, null));
        user.put(KEY_NOMBRE_USUARIO, pref.getString(KEY_NOMBRE_USUARIO, null));
        user.put(KEY_PATENTE, pref.getString(KEY_PATENTE, null));
        user.put(KEY_ESTADO_RUTA, pref.getString(KEY_ESTADO_RUTA, null));
        return user;
    }

    public void logoutUser()
    {
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    //-----------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------

    public static class ValidarLogin extends AsyncTask<String,String, JSONObject>
    {

        @Override
        protected JSONObject doInBackground(String... parametros) {

            String JsonResponse = "";
            String datos = parametros[0];
            HttpURLConnection urlConnection = null;
            //Parámetros
            BufferedReader reader = null;
            OutputStream os = null;

            Log.d("Session", "params: "+datos);
            try {
                URL url = new URL("http://autoelectrico.tk/odata/Autos/IniciarSesion");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();

                os = new BufferedOutputStream(urlConnection.getOutputStream());
                os.write(datos.toString().getBytes());
                os.flush();
                Log.d("Session", "pass1: " + "pass");
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                }
                Log.d("Session", "pass2: " + "pass2");
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine = "";
                while ((inputLine = reader.readLine()) != null)
                {
                    buffer.append(inputLine);
                }

                JsonResponse = buffer.toString();
                JSONObject resultadoJSON = new JSONObject(JsonResponse);
                Log.d("Session", "resultadoJSON: " + resultadoJSON);

                return resultadoJSON;

            } catch (IOException e) {
                Log.d("Session", "Error1: " + e.getMessage());
                e.printStackTrace();
            } catch (JSONException e) {
                Log.d("Session", "Error2: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.d("Session", "Error3: " + e.getMessage());
                    }
                }
            }

            return null;
        }

        protected void onPostExecute(JSONObject respuestaOdata) {
            try {

                LoginActivity iSession = (LoginActivity) ControllerActivity.activiyAbiertaActual;
                iSession.dataReceiveFromSessionManager(respuestaOdata);
            }
            catch (Exception e) {
                String error = "";
            }
        }
    }

    public static class SeleccionarVehiculo extends AsyncTask<String,String, JSONObject>
    {

        @Override
        protected JSONObject doInBackground(String... parametros) {

            String JsonResponse = "";
            String datos = parametros[0];
            HttpURLConnection urlConnection = null;
            //Parámetros
            BufferedReader reader = null;
            OutputStream os = null;

            Log.d("Session", "params: "+datos);
            try {
                URL url = new URL("http://autoelectrico.tk/odata/Autos/SeleccionarVehiculo");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();

                os = new BufferedOutputStream(urlConnection.getOutputStream());
                os.write(datos.toString().getBytes());
                os.flush();
                Log.d("Session", "pass1: " + "pass");
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                }
                Log.d("Session", "pass2: " + "pass2");
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine = "";
                while ((inputLine = reader.readLine()) != null)
                {
                    buffer.append(inputLine);
                }

                JsonResponse = buffer.toString();
                JSONObject resultadoJSON = new JSONObject(JsonResponse);
                Log.d("Session", "resultadoJSON: " + resultadoJSON);

                return resultadoJSON;

            } catch (IOException e) {
                Log.d("Session", "Error1: " + e.getMessage());
                e.printStackTrace();
            } catch (JSONException e) {
                Log.d("Session", "Error2: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.d("Session", "Error3: " + e.getMessage());
                    }
                }
            }

            return null;
        }

        protected void onPostExecute(JSONObject respuestaOdata) {
            try {

                SeleccionarVehiculoActivity iSession = (SeleccionarVehiculoActivity) ControllerActivity.activiyAbiertaActual;
                iSession.dataReceiveFromSessionManagerSeleccionarVehiculo(respuestaOdata);
            }
            catch (Exception e) {
                String error = "";
            }
        }
    }

    public static class ConfirmacionLlave extends AsyncTask<String,String, JSONObject>
    {

        @Override
        protected JSONObject doInBackground(String... parametros) {

            String JsonResponse = "";
            String datos = parametros[0];
            HttpURLConnection urlConnection = null;
            //Parámetros
            BufferedReader reader = null;
            OutputStream os = null;

            Log.d("Session", "params: "+datos);
            try {
                URL url = new URL("http://autoelectrico.tk/odata/Autos/ConfirmacionLlave");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();

                os = new BufferedOutputStream(urlConnection.getOutputStream());
                os.write(datos.toString().getBytes());
                os.flush();
                Log.d("Session", "pass1: " + "pass");
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                }
                Log.d("Session", "pass2: " + "pass2");
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine = "";
                while ((inputLine = reader.readLine()) != null)
                {
                    buffer.append(inputLine);
                }

                JsonResponse = buffer.toString();
                JSONObject resultadoJSON = new JSONObject(JsonResponse);
                Log.d("Session", "resultadoJSON: " + resultadoJSON);

                return resultadoJSON;

            } catch (IOException e) {
                Log.d("Session", "Error1: " + e.getMessage());
                e.printStackTrace();
            } catch (JSONException e) {
                Log.d("Session", "Error2: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.d("Session", "Error3: " + e.getMessage());
                    }
                }
            }

            return null;
        }

        protected void onPostExecute(JSONObject respuestaOdata) {
            try {

                SeleccionarVehiculoActivity iSession = (SeleccionarVehiculoActivity) ControllerActivity.activiyAbiertaActual;
                iSession.dataReceiveFromSessionManagerConfirmacionAcceso(respuestaOdata);
            }
            catch (Exception e) {
                String error = "";
            }
        }
    }
}
