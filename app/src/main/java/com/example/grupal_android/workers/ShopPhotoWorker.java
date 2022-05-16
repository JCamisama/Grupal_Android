package com.example.grupal_android.workers;


import android.content.Context;


import androidx.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;


import com.example.grupal_android.utils.GlobalVariablesUtil;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class ShopPhotoWorker extends Worker {
    public ShopPhotoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    /**
     *  Metodo encargado en conseguir la foto de una tienda concreta sabiendo el nombre, latitud y longitud
     */

    @NonNull
    @Override
    public Result doWork() {
        String direccion = GlobalVariablesUtil.REMOTE_SERVER+"/"+GlobalVariablesUtil.GET_SHOP_PHOTO;
        //String direccion = REMOTE_SERVER + "/" + GET_FRANCHISE_BY_NAME_PHP;
        HttpURLConnection urlConnection;
        String name = getInputData().getString("name");
        String lat = getInputData().getString("lat");
        String lng = getInputData().getString("lng");

        String parametros = "name="+name+"&lat="+lat+"&lng="+lng;

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
                Scanner s = new Scanner(urlConnection.getInputStream()).useDelimiter("\\A");
                String result = s.hasNext() ? s.next() : "";
                OutputStreamWriter fichero = new OutputStreamWriter(getApplicationContext().openFileOutput("foto.txt", Context.MODE_PRIVATE));
                fichero.write(result);
                fichero.close();
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