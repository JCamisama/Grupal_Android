package com.example.grupal_android.workers;



import static com.example.grupal_android.utils.GlobalVariablesUtil.FRANCHISE_NAME;
import static com.example.grupal_android.utils.GlobalVariablesUtil.GET_FRANCHISE_BY_NAME_PHP;
import static com.example.grupal_android.utils.GlobalVariablesUtil.GET_FRANCHISE_NAMES_PHP;
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

import com.example.grupal_android.managers.FranchiseManager;
import com.example.grupal_android.models.Franchise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetFranchiseByNameWorker extends Worker {
    public GetFranchiseByNameWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
//        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/midoyaga002/WEB/get_franchise_by_name.php";
        String direccion = REMOTE_SERVER + "/" + GET_FRANCHISE_BY_NAME_PHP;
        HttpURLConnection urlConnection;
        String name = getInputData().getString(FRANCHISE_NAME);

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

                JSONObject jsonObject = new JSONObject(result);

                byte[] imageData = Base64.decode(jsonObject.getString("logo"), Base64.DEFAULT);

                String franchiseName = jsonObject.getString("name");
                Bitmap logo = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                String type_ES = jsonObject.getString("type_ES");
                String type_EN = jsonObject.getString("type_EN");
                String description_ES = jsonObject.getString("description_ES");
                String description_EN = jsonObject.getString("description_EN");
                String url = jsonObject.getString("url");

                Franchise franchise = new Franchise(franchiseName,logo,type_ES,type_EN,description_ES,description_EN,url);
                FranchiseManager.getInstance(getApplicationContext()).addFranquise(franchise);

                Log.i("GetFranchiseByName", "FRANCHISE --> " + franchise.getName());
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

