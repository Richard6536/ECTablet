package com.example.richard.ectablet.Clases;

import android.os.AsyncTask;
import android.util.Log;

import com.example.richard.ectablet.Activity.LoginActivity;
import com.example.richard.ectablet.Activity.MainActivity;

import org.json.JSONArray;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class Vehiculo {

    public static JSONArray listaDatosAEnviar = new JSONArray();

    public static class ActualizarDatos extends AsyncTask<String,String, JSONObject>
    {

        @Override
        protected JSONObject doInBackground(String... parametros) {

            String JsonResponse = "";
            String datos = parametros[0];
            String vehiculoId = parametros[1];
            String LLAVE = parametros[2];
            String flotaId = parametros[3];

            HttpURLConnection urlConnection = null;
            //Parámetros
            BufferedReader reader = null;
            OutputStream os = null;

            JSONArray stringToJsonArr = null;
            JSONObject jsonObjectDatos = null;

            try {
                stringToJsonArr = new JSONArray(datos);

                for(int a = 0; a<stringToJsonArr.length(); a++)
                {
                    JSONObject jsonObject = null;
                    jsonObject = stringToJsonArr.getJSONObject(a);
                    listaDatosAEnviar.put(jsonObject);

                }

                jsonObjectDatos = new JSONObject();
                jsonObjectDatos.put("VehiculoId", vehiculoId);
                jsonObjectDatos.put("Llave", LLAVE);
                jsonObjectDatos.put("FlotaId", flotaId);
                jsonObjectDatos.put("ListaDatos", stringToJsonArr);

            } catch (JSONException e) {
                e.printStackTrace();
                Almacenamiento.crearRegistroErroresPosicion("ErrorJSonPosicion", listaDatosAEnviar);
                listaDatosAEnviar = new JSONArray(new ArrayList<String>());
            }

            Almacenamiento.crearRegistroErroresPosicion("Entro_AsyncTask", stringToJsonArr);

            Log.d("Session", "params: "+datos);
            try {

                URL url = new URL("http://autoelectrico.tk/odata/Autos/ActualizarDatos");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();

                os = new BufferedOutputStream(urlConnection.getOutputStream());
                os.write(jsonObjectDatos.toString().getBytes());
                os.flush();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine = "";
                while ((inputLine = reader.readLine()) != null) {
                    buffer.append(inputLine);
                }

                JsonResponse = buffer.toString();
                JSONObject resultadoJSON = new JSONObject(JsonResponse);

                Log.d("Session", "resultadoJSON: " + resultadoJSON);

                return resultadoJSON;

            } catch (IOException e) {
                Log.d("Session", "Error1: " + e.getMessage());
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                Log.d("Session", "Error2: " + e.getMessage());
                e.printStackTrace();
                return null;
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
        }

        protected void onPostExecute(JSONObject respuestaOdata) {
            try
            {

                String tipoRespuesta = respuestaOdata.getString("TipoRespuesta");
                String mensaje = respuestaOdata.getString("Mensaje");

                Almacenamiento.crearRegistroErroresPosicion("Termino_Asynctask: "+tipoRespuesta+": ", listaDatosAEnviar);
                //se borran los registros del txt
                Almacenamiento.vaciarTxt();

                if(listaDatosAEnviar == new JSONArray(new ArrayList<String>()))
                {
                    Almacenamiento.crearRegistroErroresPosicion("Lista posicion vacia, respuesta: "+mensaje, listaDatosAEnviar);
                }


                if(tipoRespuesta.equals("OK"))
                {

                    listaDatosAEnviar = new JSONArray(new ArrayList<String>());
                }
                else if(tipoRespuesta.equals("ERROR"))
                {
                    Almacenamiento.crearRegistroErroresPosicion(tipoRespuesta+": "+mensaje, listaDatosAEnviar);
                    listaDatosAEnviar = new JSONArray(new ArrayList<String>());
                }
                else
                {
                    //"CatchWebService"
                    Almacenamiento.crearRegistroErroresPosicion("catch web service", listaDatosAEnviar);

                    //Storage.crearRegistroErroresPosicion("catch web service", listaDatosAEnviar);
                    Almacenamiento.EscribirEnArchivo(listaDatosAEnviar);
                    //se escribe en el txt listaDatosAEnviar
                }

                //tapon = false;
            }
            catch (Exception e)
            {
                Log.e(TAG, "onPostExecute: "+e.getMessage());
                //Almacenamiento.crearRegistroErroresPosicion("catch mensaje o null", listaDatosAEnviar);
                Almacenamiento.EscribirEnArchivo(listaDatosAEnviar);
            }
        }
    }

    public static class ActualizarPosicion extends AsyncTask<String,String,JSONObject>
    {
        @Override
        protected JSONObject doInBackground(String... parametros) {

            JSONObject datos = null;
            String JsonResponse = "";
            Log.d(TAG, "ActualizarPosicionAsyncTask_Parametros " + parametros);

            String p = parametros[0];
            Log.d(TAG, "ActualizarPosicionAsyncTask_userId");

            //TODO: TERCER MENSAJE
            //ejecutarMetodoPos("mensajeEntroAsyncPosicion");
            try {

                JSONArray jsonArray = new JSONArray(p);

                datos = new JSONObject();
                //datos.put("Id", userId);
                datos.put("VehiculoId", 2);
                datos.put("ListaDatos", jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            HttpURLConnection urlConnection = null;
            //Parámetros
            BufferedReader reader = null;
            OutputStream os = null;

            try {
                URL url = new URL("http://autoelectrico.tk/odata/Autos/ActualizarDatosRaspberryDirecto");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(6000);
                urlConnection.setReadTimeout(6000);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();

                os = new BufferedOutputStream(urlConnection.getOutputStream());
                os.write(datos.toString().getBytes());
                os.flush();
                Log.d("TAG", "datosActualizado" + datos+"");

                InputStream inputStream = urlConnection.getInputStream();

                //TODO: CUARTO MENSAJE
                //ejecutarMetodoPos("mensajePasoInputStream");

                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine = "";
                while ((inputLine = reader.readLine()) != null)
                {
                    buffer.append(inputLine);
                }

                String m = buffer.toString();
                JSONObject resultadoJSON = new JSONObject(m);
                return resultadoJSON;

            } catch (IOException e) {
                e.printStackTrace();
                //TODO QUINTO MENSAJE
                //ejecutarMetodoPos("mensajeEntroCatchPosicion");

                try{
                    JSONObject jsonObjectError500 = new JSONObject();
                    jsonObjectError500.put("TipoRespuesta","ERROR");
                    jsonObjectError500.put("Mensaje","Error 500. Ha ocurrido un problema con el servidor, intente más tarde.");

                    return  jsonObjectError500;

                }catch (JSONException a){
                    a.getStackTrace();
                }
            }
            catch (JSONException e) {
                Log.d("Session", "Error2: " + e.getMessage());
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("TAG", "Error closing stream", e);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject respuestaOdata)
        {

            respuestaWebService(respuestaOdata);
            /*
            try
            {
                MainActivity cc = (MainActivity) ControllerActivity.activiyAbiertaActual;
                cc.respuestaWebService(respuestaOdata);
            }
            catch (Exception e)
            {
                Log.e(TAG, "onPostExecute: "+e.getMessage());
            }
            */
        }

        public void respuestaWebService(JSONObject respuesta){
            try {

                String tipoRespuesta = respuesta.getString("TipoRespuesta");
                if(tipoRespuesta.equals("OK")){
                    try {
                        Log.d("RESPUESTA: ", tipoRespuesta);
                    }
                    catch (Exception e){
                        e.getStackTrace();
                        Log.e("RESPUESTA: ", tipoRespuesta, e);
                    }
                }
                else if(tipoRespuesta.equals("ERROR")){
                    String errorMensaje = respuesta.getString("Mensaje");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
