package com.example.grupal_android.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.grupal_android.utils.GlobalVariablesUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ActualizarPuntuacionWorker extends Worker {
    public ActualizarPuntuacionWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /**
     *  Método que llamara al php que su funcion es actualizar la puntuación total de una tienda
     */
    @NonNull
    @Override
    public Result doWork() {
        //String direccion = GlobalVariablesUtil.REMOTE_SERVER+"/"+GlobalVariablesUtil.INSERT_VOTED;
        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/jmartin213/WEB" + "/" + GlobalVariablesUtil.UPDATE_PUNTUATION;
        HttpURLConnection urlConnection;
        //Obtener los parámetros del Data
        String nameFranchise = getInputData().getString("nameFranchise");
        String lat = getInputData().getString("lat");
        String lng = getInputData().getString("lng");
        String points = getInputData().getString("points");

        String parametros = "nameFranchise=" + nameFranchise + "&lat=" + lat + "&lng=" + lng + "&points=" + points;
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
                return Result.success();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
