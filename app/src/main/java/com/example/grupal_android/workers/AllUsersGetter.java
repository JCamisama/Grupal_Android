package com.example.grupal_android.workers;

import com.example.grupal_android.models.User;
import com.example.grupal_android.utils.GlobalVariablesUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Clase que gestiona la obtención de usuarios en un endpoint remoto de manera sincrona.
 */
public class AllUsersGetter {

    private String getFileUrl = "";
    private String query = "";


    /**
     * Inicializa la URL que se usará para tomar información sobre los usuarios.
     */
    public AllUsersGetter() {
        String databaseServer = GlobalVariablesUtil.REMOTE_SERVER;
        String getUserFile = GlobalVariablesUtil.GET_USER_BY_ID_PHP;
        this.getFileUrl = databaseServer + "/" + getUserFile;
        this.query = "?username=";
    }


    /**
     * Busca un usuario según el username, devolviendo null si no lo encuentra.
     */
    public User getUser(String pUsername) {
        User user = null;

        HttpURLConnection urlConnection = null;
        String url = this.getFileUrl + this.query + pUsername; // Se construye la URL completa

        try {
            URL destino = new URL(url);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                // Se ha encontrado el recurso, por tanto, se creará el objeto User.
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                // La respuesta del servidor es un array en formato JSON
                JSONArray jsonArray = new JSONArray(result);

                String resultUsername = jsonArray.getJSONObject(0).getString("username");
                String resultPassword = jsonArray.getJSONObject(0).getString("password");

                inputStream.close();
                user = new User(resultUsername, resultPassword);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}
