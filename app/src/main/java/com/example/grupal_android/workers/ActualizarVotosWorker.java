package com.example.grupal_android.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.grupal_android.utils.GlobalVariablesUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ActualizarVotosWorker extends Worker {
    public ActualizarVotosWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /**
     *  Método que llamara al php que su funcion es meter una foto de sacada por el usuario
     */
    @NonNull
    @Override
    public Result doWork() {
        //String direccion = GlobalVariablesUtil.REMOTE_SERVER+"/"+GlobalVariablesUtil.UPDATE_VOTED;
        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/jmartin213/WEB" + "/" + GlobalVariablesUtil.UPDATE_VOTED;
        HttpURLConnection urlConnection;
        //Obtener los parámetros del Data
        String username = getInputData().getString("username");
        String nameFranchise = getInputData().getString("nameFranchise");
        String lat = getInputData().getString("lat");
        String lng = getInputData().getString("lng");
        int voted = getInputData().getInt("voted", 0);

        String parametros = "username=" + username + "&nameFranchise=" + nameFranchise + "&lat=" + lat + "&lng=" + lng + "&voted=" + voted;
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

        return Result.failure();
    }
}
