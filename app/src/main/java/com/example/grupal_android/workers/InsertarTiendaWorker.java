package com.example.grupal_android.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.grupal_android.utils.GlobalVariablesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class InsertarTiendaWorker extends Worker {
    public InsertarTiendaWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/midoyaga002/WEB/"+"/"+ GlobalVariablesUtil.INSERT_SHOP;
        HttpURLConnection urlConnection;
        //Obtener los parámetros del Data
        String name = getInputData().getString("name");
        String lat = getInputData().getString("lat");
        String lng = getInputData().getString("lng");
        JSONObject parametrosJSON = new JSONObject();
        try {
            //Pasar como parámetros JSON
            parametrosJSON.put("name",name);
            parametrosJSON.put("lat",lat);
            parametrosJSON.put("lng",lng);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            //Abrir conexión
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametrosJSON);
            out.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                return Result.success();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.failure();
    }
}

