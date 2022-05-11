package com.example.grupal_android;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.grupal_android.dialogs.CustomNotificationDialog;
import com.example.grupal_android.utils.GlobalVariablesUtil;

public class LoginActivity extends MainActivity {

    // Se toman las claves usadas en las preferencias para no perder información.
    private String userKey = GlobalVariablesUtil.INPUTTED_USER;
    private String passwordKey = GlobalVariablesUtil.INPUTTED_PASSWORD;
    private EditText usernameInput;
    private EditText passwordInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        super.initializeMenuBar();
        this.initializeElements();
    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        this.storeCurrentUserInput();
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.initializeCurrentUserInput();
    }



    /**
     * Se encarga de mostrar la ventana Register al clickar el texto.
     */
    public void onRegisterTextClicked(View view) {
        Log.i("LoginActivity", "I CLICKED ON THE REGISTER TEXT!!!");
        Intent intent = new Intent(getApplication(), RegisterActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * Gestiona la petición de login. Si las credenciales son correctas,
     * manda al usuario a la ventana Home. De lo contrario, muestra un diálogo
     * como notificación.
     */
    public void onClickLogin(View view) {
        boolean areCredentialsCorrect = this.checkIfCredentialsAreCorrect();
        if (areCredentialsCorrect) {
            this.setUserInPreferencesAndGoToHomePage();
        }
        else {
            this.showWrongCredentialsDialog();
        }
    }


    /**
     * Toma las instancias de los inputs de texto disponibles en la ventana.
     */
    public void initializeElements() {
        this.usernameInput = (EditText) findViewById(R.id.loginUsernameInput);
        this.passwordInput = (EditText) findViewById(R.id.loginPasswordInput);
        this.initializeCurrentUserInput();
    }


    /**
     * Toma los valores de los inputs y los guarda temporalmente en las
     * preferencias.
     */
    private void storeCurrentUserInput() {
        String username = this.usernameInput.getText().toString();
        String password = this.passwordInput.getText().toString();
        super.preferencesManager.setStringInPreferences(userKey, username);
        super.preferencesManager.setStringInPreferences(passwordKey, password);
    }


    /**
     * Toma los valores guardados en las preferencias, evitando la pérdida de
     * información al girar el dispositivo o recibir interrupciones externas.
     */
    private void initializeCurrentUserInput() {
        String username = super.preferencesManager.getString(this.userKey);
        String password = super.preferencesManager.getString(this.passwordKey);

        if (!username.equals("")) {
            this.usernameInput.setText(username);
            this.passwordInput.setText(password);
        }
    }


    /**
     * Método que comprueba si las credenciales introducidas son correctas.
     */
    public boolean checkIfCredentialsAreCorrect() {
        String username = this.usernameInput.getText().toString();
        String password = this.passwordInput.getText().toString();

        return super.checkIfUserCredentialsAreCorrect(username, password);
    }


    /**
     * Guarda el usuario actual en las preferencias, resetea los inputs y
     * redirige a la ventana Home de la aplicación
     */
    private void setUserInPreferencesAndGoToHomePage() {
        String username = this.usernameInput.getText().toString();
        super.sessionManager.startUserSession(username);
        super.preferencesManager.clearAllInputtedTextInFields();
        /*Descomentar para probar el ShopActivity
        Intent intent = new Intent(getApplication(), ShopActivity.class); // Por ahora, que navegue a Main
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent.putExtra("nameFranchise","test").putExtra("lat","1.20")
        .putExtra("lng","3.40"));
        finish();
        */
        Intent intent = new Intent(getApplication(), FranquiciaListActivity.class); // Cambiar a la lista de Franquicias
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }


    /**
     * Muestra el diálogo que indica una inserción errónea de credenciales.
     */
    private void showWrongCredentialsDialog() {
        String dialogType = GlobalVariablesUtil.WRONG_CREDENTIALS;
        DialogFragment wrongCredsDialog= new CustomNotificationDialog(dialogType);
        wrongCredsDialog.show(getSupportFragmentManager(), "wrongCredsDialog");
    }


    @Override
    public void onDestroy() {
        this.storeCurrentUserInput();
        super.onDestroy();
    }
}