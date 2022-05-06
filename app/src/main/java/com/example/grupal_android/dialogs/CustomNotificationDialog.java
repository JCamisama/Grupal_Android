package com.example.grupal_android.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.grupal_android.R;
import com.example.grupal_android.utils.GlobalVariablesUtil;

/**
 * Esta clase se ocupa de generar los diálogos que se necesitarán en la
 * aplicación.
 */
public class CustomNotificationDialog extends DialogFragment {

    private String typeIdOfDialog; // Sirve para determinar qué mensaje se mostrará.

    public CustomNotificationDialog(String pType) {
        this.typeIdOfDialog = pType;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = this.getDialogBuilder();

        return builder.create();
    }


    /**
     * Construye el diálogo dependiendo del mensaje.
     */
    private AlertDialog.Builder getDialogBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.MyDialogStyle)
        );
        String message = this.getDialogMessage();
        builder.setMessage(message);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.out.println("S");
            }
        });

        return builder;
    }


    /**
     * Determina qué mensaje se debe mostrar dependiendo del tipo especificado
     * en la creación de la instancia.
     */
    private String getDialogMessage() {
        Resources res = getResources();
        String message = "";
        switch (this.typeIdOfDialog) {
            case GlobalVariablesUtil.WRONG_CREDENTIALS:
                message = res.getString(R.string.credenciales_incorrectas);
                break;
            case GlobalVariablesUtil.NO_EMPTY_FIELDS:
                message = res.getString(R.string.no_campos_vacios);
                break;
            case GlobalVariablesUtil.PASSWORDS_NOT_MATCH:
                message = res.getString(R.string.contraseñas_no_concuerdan);
                break;
            case GlobalVariablesUtil.USER_ALREADY_EXISTS:
                message = res.getString(R.string.usuario_ya_existe);
                break;
        }

        return message;
    }

}
