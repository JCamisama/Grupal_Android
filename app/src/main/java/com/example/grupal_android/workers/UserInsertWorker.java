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


/**
 * Clase que gestiona el registro de nuevos usuarios en un endpoint remoto de manera asíncrona.
 */
public class UserInsertWorker extends Worker {

    private String insertFileUrl = "";

    public UserInsertWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        String databaseServer = GlobalVariablesUtil.REMOTE_SERVER;
        String insertPhpFile = GlobalVariablesUtil.INSERT_USER_PHP;
        this.insertFileUrl = databaseServer + "/" + insertPhpFile;
    }


    /**
     * Realiza el registro del usuario según las credenciales introducidas.
     */
    @NonNull
    @Override
    public Result doWork() {

        HttpURLConnection urlConnection;
        String username = getInputData().getString("username");
        String password = getInputData().getString("password");

        // Preparando los parámetros de la petición POST
        String parametros = "username=" + username
                + "&password=" + password;
        try {
            URL destino = new URL(this.insertFileUrl);
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
