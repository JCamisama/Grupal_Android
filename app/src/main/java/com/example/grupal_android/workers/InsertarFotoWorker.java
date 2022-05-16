package com.example.grupal_android.workers;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.grupal_android.utils.GlobalVariablesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class InsertarFotoWorker extends Worker {
    public InsertarFotoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String direccion = GlobalVariablesUtil.REMOTE_SERVER+"/"+GlobalVariablesUtil.INSERT_IMAGE;
        HttpURLConnection urlConnection;
        //Obtener los parámetros del Data
        String uriString = getInputData().getString("uriString");
        int anchoDestino = getInputData().getInt("ancho",100);
        int altoDestino = getInputData().getInt("alto",100);
        String nameFranchise = getInputData().getString("nameFranchise");
        String lat = getInputData().getString("lat");
        String lng = getInputData().getString("lng");
        //Transformar en bitmap desde la uri
        Uri uriimagen = Uri.parse(uriString);
        String image = null;
        byte[] fototransformada = null;
        try {
            Bitmap bitmapFoto = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uriimagen);
            //Recortar
            int anchoImagen = bitmapFoto.getWidth();
            int altoImagen = bitmapFoto.getHeight();
            float ratioImagen = (float) anchoImagen / (float) altoImagen;
            float ratioDestino = (float) anchoDestino / (float) altoDestino;
            int anchoFinal = anchoDestino;
            int altoFinal = altoDestino;
            if (ratioDestino > ratioImagen) {
                anchoFinal = (int) ((float)altoDestino * ratioImagen);
            } else {
                altoFinal = (int) ((float)anchoDestino / ratioImagen);
            }
            Bitmap bitmapredimensionado = Bitmap.createScaledBitmap(bitmapFoto,anchoFinal,altoFinal,true);

            //Guardar en la base de datos como Base64
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapredimensionado.compress(Bitmap.CompressFormat.PNG, 100, stream);
            fototransformada = stream.toByteArray();
            image = Base64.encodeToString(fototransformada, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject parametrosJSON = new JSONObject();
        try {
            //Pasar como parámetros JSON
            parametrosJSON.put("image",image);
            parametrosJSON.put("nameFranchise",nameFranchise);
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
