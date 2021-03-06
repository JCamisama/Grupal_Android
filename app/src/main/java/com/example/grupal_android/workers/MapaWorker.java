package com.example.grupal_android.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.grupal_android.utils.GlobalVariablesUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MapaWorker extends Worker {
    public MapaWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }
    /**
     * Método que devolvera todas las ubicaciones de una franquicia de tiendas gracias un php
     */
    @NonNull
    @Override
    public Result doWork() {
        String direccion = GlobalVariablesUtil.REMOTE_SERVER +"/"+GlobalVariablesUtil.GET_UBIS;
        HttpURLConnection urlConnection;
        String tienda = getInputData().getString("tienda");
        String parametros = "tienda="+tienda;

        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                //Lee la respuesta y la prepara para poder volver devolver una respuesta adecuada para ActividadMapa
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;

                }
                result = result.replace("[", "");
                result = result.replace("]", "");
                result = "["+result+"]";
                JSONArray jsonArray = new JSONArray(result);
                ArrayList<String> listaN = new ArrayList<>();
                ArrayList<String> listaLat = new ArrayList<>();
                ArrayList<String> listaLon = new ArrayList<>();

                for(int i = 0; i < jsonArray.length(); i++) {
                    String nombre = jsonArray.getJSONObject(i).getString("nameFranchise");
                    String longitud = jsonArray.getJSONObject(i).getString("lng");
                    String latitud = jsonArray.getJSONObject(i).getString("lat");
                    listaN.add(nombre);
                    listaLat.add(latitud);
                    listaLon.add(longitud);
                }
                inputStream.close();

                Data resultados = new Data.Builder()
                        .putString("datosN",listaN.toString())
                        .putString("datosLat",listaLat.toString())
                        .putString("datosLon",listaLon.toString())
                        .build();
                return Result.success(resultados);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Result.failure();
    }
}
