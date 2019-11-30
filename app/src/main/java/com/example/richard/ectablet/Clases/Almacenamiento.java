package com.example.richard.ectablet.Clases;

import android.Manifest;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Almacenamiento {

    private static FileOutputStream stappFilePosicion = null;
    private static FileOutputStream stappFileVelocidad = null;
    public static String filePosicion = "";
    public static String fileVelocidad = "";

    public static File myFilePosicion = null;
    public static File myFileVelocidad = null;

    //TODO: REGISTROS DE ERROR

    private static FileOutputStream stappFileErrorPosicion = null;
    public static String fileErrorPosicion = "";
    public static File myFileErrorPosicion = null;

    public static void crearDirectorio(String id)
    {
        File dir = new File("/storage/emulated/0/Android/data/com.stapp.ae.android.data"); ///storage/emulated/0/Android/data

        if(!dir.isDirectory()) //Comprueba si el directorio existe.
        {
            //Entra si el directorio no existe.
            if(!dir.mkdir()) {

            }
            else
            {
                //Si el directorio se crea por primera vez, crea un archivo inicializandolo con el ID del usuario.
                File root = new File(Environment.getExternalStorageDirectory(), "Android/data/com.stapp.ae.android.data"); //TODO: /storage/emulated/0/
                if (!root.exists()) {
                    root.mkdirs();
                }

                try {

                    //POSICION
                    filePosicion = "stappfilePosicion("+id+").txt";
                    myFilePosicion = new File(root, filePosicion);
                    myFilePosicion.createNewFile();
                    stappFilePosicion = new FileOutputStream(myFilePosicion);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stappFilePosicion));
                    //writer.append(id);
                    writer.newLine();

                    writer.flush();
                    writer.close();
                    stappFilePosicion.close();

                    //VELOCIDAD
                    fileVelocidad = "stappfilevelocidad("+id+").txt";
                    myFileVelocidad = new File(root, fileVelocidad);
                    myFileVelocidad.createNewFile();
                    stappFileVelocidad = new FileOutputStream(myFileVelocidad);
                    BufferedWriter writerVelocidad = new BufferedWriter(new OutputStreamWriter(stappFileVelocidad));
                    //writerVelocidad.append(id);
                    writerVelocidad.newLine();

                    writer.flush();
                    writer.close();
                    stappFileVelocidad.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            try {

                File root = new File(Environment.getExternalStorageDirectory(), "Android/data/com.stapp.ae.android.data"); //TODO: /storage/emulated/0/
                if (!root.exists()) {
                    root.mkdirs();
                }

                filePosicion = "stappfilePosicion("+id+").txt";
                fileVelocidad = "stappfilevelocidad("+id+").txt";
                myFilePosicion = new File(root, filePosicion);
                myFileVelocidad = new File(root, fileVelocidad);

                if(!myFilePosicion.exists())
                {
                    myFilePosicion.createNewFile();
                }

                if(!myFileVelocidad.exists())
                {
                    myFileVelocidad.createNewFile();
                }

                String a ="";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static JSONArray leerArchivo(String _idAuto, String fileDirectory, String fileName)
    {
        try {

            // File sdcard = Environment.getExternalStorageDirectory();
            File file = new File(fileDirectory);

            String idFileName = getFileNameId(fileName);

            StringBuilder textLista = new StringBuilder();
            BufferedReader br = null;

            br = new BufferedReader(new FileReader(file));
            String line;

            /*
            String id = br.readLine(); // consume first line and ignore
            if(id == null)
            {
                return  null;
            }*/

            if(!idFileName.equals(_idAuto))
            {
                String a = "";
                return  null;
            }

            while ((line = br.readLine()) != null) {
                textLista.append(line);

            }
            br.close();

            if(textLista.toString().length() > 0)
            {
                try {
                    //MainActivity.mostrarId(_idAuto, idFileName, fileName);
                    JSONArray listaJsonArray = new JSONArray(textLista.toString());
                    return listaJsonArray;

                } catch (JSONException e) {
                    e.printStackTrace();
                    Almacenamiento.crearRegistroErroresPosicion("ERRORLEYENDOARCHIVO", new JSONArray(new ArrayList<String>()));
                }

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void vaciarTxt()
    {
        try {

            FileOutputStream fileinput = new FileOutputStream(myFilePosicion);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileinput));

            writer.append("");

            writer.close();
            fileinput.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void EscribirEnArchivo(JSONArray listaJsonParametros) {
        try {

            File f = myFilePosicion;
            FileOutputStream fileinput = new FileOutputStream(myFilePosicion);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileinput));

            //writer.append(userId);
            //writer.newLine();

            writer.append(listaJsonParametros+"");
            //writer.newLine();

            writer.close();
            fileinput.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String getFileNameId(String fileName)
    {
        String[]fileNameSplitPart1 = fileName.split("\\(");
        String[]fileNameSplitPart2 = fileNameSplitPart1[1].split("\\)");
        String id = fileNameSplitPart2[0];

        return id;
    }

    public static void crearRegistroErroresPosicion(String tipoError, JSONArray registrosPosicion)
    {
        File dir = new File("/storage/emulated/0/Android/data/com.stapp.ae.android.data"); ///storage/emulated/0/Android/data
        if(!dir.isDirectory()) //Comprueba si el directorio existe.
        {
            //Entra si el directorio no existe.
            if(!dir.mkdir()) {

            }
            else
            {
                //Si el directorio se crea por primera vez, crea un archivo inicializandolo con el ID del usuario.
                File root = new File(Environment.getExternalStorageDirectory(), "Android/data/com.stapp.ae.android.data"); //TODO: /storage/emulated/0/
                if (!root.exists()) {
                    root.mkdirs();
                }

                try {


                    fileErrorPosicion = "stappFileErrorPosicion.txt";
                    myFileErrorPosicion = new File(root, fileErrorPosicion);
                    myFileErrorPosicion.createNewFile();
                    stappFileErrorPosicion = new FileOutputStream(myFileErrorPosicion,true);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stappFileErrorPosicion));
                    writer.append(tipoError);
                    writer.newLine();
                    writer.append(registrosPosicion+"");
                    writer.newLine();

                    writer.flush();
                    writer.close();
                    stappFileErrorPosicion.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            try {

                File root = new File(Environment.getExternalStorageDirectory(), "Android/data/com.stapp.ae.android.data"); //TODO: /storage/emulated/0/
                if (!root.exists()) {
                    root.mkdirs();
                }

                fileErrorPosicion = "stappFileErcrearRegistroErroresPosicionrorPosicion.txt";
                //fileVelocidad = "stappfilevelocidad("+id+").txt";
                myFileErrorPosicion = new File(root, fileErrorPosicion);
                //myFileVelocidad = new File(root, fileVelocidad);

                if(!myFileErrorPosicion.exists())
                {
                    myFileErrorPosicion.createNewFile();
                }

                stappFileErrorPosicion = new FileOutputStream(myFileErrorPosicion,true);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stappFileErrorPosicion));
                writer.append(tipoError);
                writer.newLine();
                writer.append(registrosPosicion+"");
                writer.newLine();

                writer.flush();
                writer.close();
                stappFileErrorPosicion.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
