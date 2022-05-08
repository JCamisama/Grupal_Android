package com.example.grupal_android.managers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.grupal_android.R;
import com.example.grupal_android.utils.GlobalVariablesUtil;

/**
 * Gestor de sesiones de la aplicación.
 */
public class SessionManager {

    private static SessionManager instance = null;
    private Context myContext = null;
    private CustomPreferencesManager preferenceManager = null;
    private String userPreferencesKey = "";
    private boolean isSessionOngoing = false;


    /*** Singleton Pattern ***/
    private SessionManager(Context pContext) {
        this.myContext = pContext;
        this.preferenceManager = CustomPreferencesManager.getInstance(pContext);
        this.userPreferencesKey = GlobalVariablesUtil.CURRENT_USER;
        this.isSessionOngoing = !this.preferenceManager.getString(userPreferencesKey).equals("");
    }

    public static synchronized SessionManager getInstance(Context pContext) {
        if (SessionManager.instance == null) {
            SessionManager.instance = new SessionManager(pContext);
        }

        return SessionManager.instance;
    }
    /*************************/


    /**
     * Inicia la sesión del usuario indicado guardando su username en las preferencias y
     * lanzando una notificación al usuario.
     */
    public void startUserSession(String pUsername) {
        this.preferenceManager.setStringInPreferences(this.userPreferencesKey, pUsername);
        this.isSessionOngoing = true;
//        this.throwNotification();
    }


    /**
     * Finaliza una sesión iniciada anteriormente.
     */
    public void endUserSession() {
        this.preferenceManager.setStringInPreferences(this.userPreferencesKey, "");
        this.isSessionOngoing = false;
    }


    /**
     * Método que determina si hay una sesión iniciada.
     */
    public boolean checkIfSessionOngoing() {
        return this.isSessionOngoing;
    }


    /**
     * Método que determina el username del usuario portador de la sesión actual.
     */
    public String getCurrentUsername() {
        return this.preferenceManager.getString(userPreferencesKey);
    }


//    /**
//     * Construye y lanza la notificación de login.
//     */
//    private void throwNotification() {
//        NotificationManager notificationManager =
//                (NotificationManager) this.myContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationCompat.Builder builder =
//                new NotificationCompat.Builder(this.myContext, "pianopediaNotif");
//
//        Resources resources = this.myContext.getResources();
//        String notificationTitle = resources.getString(R.string.new_session_notification_title);
//        String notificationText = resources.getString(R.string.new_session_notification_text);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Construcción del NotificationChannel para versiones iguales o porteriores a Oreo.
//            NotificationChannel channel = new NotificationChannel(
//                    "pianopediaNotif", notificationTitle,
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            channel.setDescription(notificationText);
//            channel.enableLights(true);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        builder.setSmallIcon(android.R.drawable.stat_notify_more)
//                .setContentTitle(notificationTitle)
//                .setContentText(notificationText)
//                .setAutoCancel(true);
//
//        notificationManager.notify(1, builder.build());
//    }


}
