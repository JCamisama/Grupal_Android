package com.example.grupal_android.workers;



import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.grupal_android.managers.FranchiseManager;
import com.example.grupal_android.models.Franchise;

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

public class GetFranchisesWorker extends Worker {
    public GetFranchisesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/midoyaga002/WEB/get_franchise_by_name.php";
        HttpURLConnection urlConnection;
        String name = getInputData().getString("name");

        String parametros = "name="+name;

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
                Log.d("log",result);
                JSONArray jsonArray = new JSONArray(result);
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    String namephp = jsonArray.getJSONObject(i).getString("name");
                    Bitmap logo = (Bitmap) jsonArray.getJSONObject(i).get("logo");
                    String type_ES = jsonArray.getJSONObject(i).getString("type_ES");
                    String type_EN = jsonArray.getJSONObject(i).getString("type_EN");
                    String description_ES = jsonArray.getJSONObject(i).getString("description_ES");
                    String description_EN = jsonArray.getJSONObject(i).getString("description_EN");
                    String url = jsonArray.getJSONObject(i).getString("url");

                    Franchise franchise = new Franchise(namephp,logo,type_ES,type_EN,description_ES,description_EN,url);

                    FranchiseManager.getInstance(getApplicationContext()).addFranquise(franchise);

                }
                inputStream.close();

                return Result.success();
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

