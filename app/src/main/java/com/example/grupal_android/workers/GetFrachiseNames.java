package com.example.grupal_android.workers;



import static com.example.grupal_android.utils.GlobalVariablesUtil.GET_FRANCHISE_NAMES_PHP;
import static com.example.grupal_android.utils.GlobalVariablesUtil.REMOTE_SERVER;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetFrachiseNames extends Worker {
    public GetFrachiseNames(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /**
     * Método que hara una llamada al php encargado de conseguir todos los nombres de las franquicias
     */
    @NonNull
    @Override
    public Result doWork() {
//        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/midoyaga002/WEB/get_frachises_names.php";
        String direccion = REMOTE_SERVER + "/" + GET_FRANCHISE_NAMES_PHP;

        HttpURLConnection urlConnection;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                JSONArray jsonArray = new JSONArray(result);
                ArrayList<String> lista = new ArrayList<>();
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    String nombre = jsonArray.getJSONObject(i).getString("name");
                    lista.add(nombre);
                }
                inputStream.close();

                Data resultados = new Data.Builder()
                        .putString("datos",lista.toString())
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



