package com.example.grupal_android.workers;



import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.grupal_android.utils.GlobalVariablesUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetVotedWorker extends Worker {
    public GetVotedWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /**
     * Método encargado en llamar al php encargado de conseguir el todos los atributos de una franquicia por el nombre de la misma
     */

    @NonNull
    @Override
    public Result doWork() {
        //String direccion = GlobalVariablesUtil.REMOTE_SERVER+"/"+GlobalVariablesUtil.GET_VOTED;
        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/jmartin213/WEB"+"/"+GlobalVariablesUtil.GET_VOTED;
        HttpURLConnection urlConnection;
        String username = getInputData().getString("username");
        String nameFranchise = getInputData().getString("nameFranchise");
        String lat = getInputData().getString("lat");
        String lng = getInputData().getString("lng");

        String parametros = "username=" + username + "&nameFranchise=" + nameFranchise + "&lat=" + lat + "&lng=" + lng;

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
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();

                Data resultados = new Data.Builder()
                        .putString("voted",result)
                        .build();

                return Result.success(resultados);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.failure();
    }

}

