package com.example.grupal_android.workers;

import static com.example.grupal_android.utils.GlobalVariablesUtil.REMOTE_SERVER;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ShopPhotoWorker extends Worker {
    public ShopPhotoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/midoyaga002/WEB/get_shop_photo.php";
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