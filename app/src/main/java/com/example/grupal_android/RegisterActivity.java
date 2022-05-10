package com.example.grupal_android;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.grupal_android.dialogs.CustomNotificationDialog;
import com.example.grupal_android.utils.GlobalVariablesUtil;

public class RegisterActivity extends MainActivity {

    // Se toman las claves usadas en las preferencias para no perder información.
    private String userKey = GlobalVariablesUtil.REGISTER_USER;
    private String passwordKey = GlobalVariablesUtil.REGISTER_PASSWORD;
    private String repeatPasswordKey = GlobalVariablesUtil.REGISTER_REPEAT_PASSWORD;
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText repeatPasswordInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        super.initializeMenuBar();
        this.initializeElements();

        Log.d("Mamasita", super.frachiseManager.getFranchises().toString());
        Log.d("Mamasita", super.frachiseManager.getFranchise(0).getLogo().toString());
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
     * Registra al usuario y lo redirige a la ventana Home si las credenciales
     * son adecuadas.
     * Posibles casos incorrectos: Campos vacíos, usuario existente,
     * repetición de contraseña incorrecta.
     */
    public void onClickRegister(View view) {
        boolean isThereAnyEmptyField = this.checkIfAnyFieldIsEmpty();
        boolean doesUserAlreadyExist = this.checkIfUserAlreadyExists();
        boolean doPasswordsMatch = this.checkIfPasswordsMatch();
        String dialogMesage = "";

        if (isThereAnyEmptyField) {
            dialogMesage = GlobalVariablesUtil.NO_EMPTY_FIELDS;
            this.showDialog(dialogMesage);
        }
        else if (doesUserAlreadyExist) {
            dialogMesage = GlobalVariablesUtil.USER_ALREADY_EXISTS;
            this.showDialog(dialogMesage);
        }
        else if (!doPasswordsMatch) {
            dialogMesage = GlobalVariablesUtil.PASSWORDS_NOT_MATCH;
            this.showDialog(dialogMesage);
        }
        else {
            this.registerUser();
            this.setUserInPreferencesAndGoToHomePage();
        }
    }


    /**
     * Toma las instancias de los inputs de texto disponibles en la ventana.
     */
    private void initializeElements() {
        this.usernameInput = (EditText) findViewById(R.id.registerUsernameInput);
        this.passwordInput = (EditText) findViewById(R.id.registerPasswordInput);
        this.repeatPasswordInput = (EditText) findViewById(R.id.registerRepeatPasswordInput);
        this.initializeCurrentUserInput();
    }


    /**
     * Toma los valores guardados en las preferencias, evitando la pérdida de
     * información al girar el dispositivo o recibir interrupciones externas.
     */
    private void initializeCurrentUserInput() {
        String username = super.preferencesManager.getString(this.userKey);
        String password = super.preferencesManager.getString(this.passwordKey);
        String repeatPassword = super.preferencesManager.getString(this.repeatPasswordKey);

        if (!username.equals("")) {
            this.usernameInput.setText(username);
            this.passwordInput.setText(password);
            this.repeatPasswordInput.setText(repeatPassword);
        }
    }


    /**
     * Toma los valores de los inputs y los guarda temporalmente en las
     * preferencias.
     */
    private void storeCurrentUserInput() {
        String username = this.usernameInput.getText().toString();
        String password = this.passwordInput.getText().toString();
        String repeatPassword = this.repeatPasswordInput.getText().toString();
        super.preferencesManager.setStringInPreferences(userKey, username);
        super.preferencesManager.setStringInPreferences(passwordKey, password);
        super.preferencesManager.setStringInPreferences(repeatPasswordKey, repeatPassword);
    }


    /**
     * Comprueba si alguno de los campos de inserción están vacíos.
     */
    private boolean checkIfAnyFieldIsEmpty() {
        String username = this.usernameInput.getText().toString();
        String password = this.passwordInput.getText().toString();
        String repeatPassword = this.repeatPasswordInput.getText().toString();
        boolean isAnyFieldEmpty = username.equals("")
                || password.equals("")
                || repeatPassword.equals("");

        return isAnyFieldEmpty;
    }


    /**
     * Comprueba si el usuario que se pretende registrar ya existe
     * en la base de datos.
     */
    private boolean checkIfUserAlreadyExists() {
        String username = this.usernameInput.getText().toString();

        return super.checkIfUserExists(username);
    }


    /**
     * Comprueba si los campos de contraseñas coinciden.
     */
    private boolean checkIfPasswordsMatch() {
        String password = this.passwordInput.getText().toString();
        String repeatPassword = this.repeatPasswordInput.getText().toString();

        return password.equals(repeatPassword);
    }


    /**
     * Este método se encarga de mostrar qué error de credenciales ha
     * ocurrido en el proceso de registro.
     */
    private void showDialog(String pDialogType) {
        DialogFragment registerDialog= new CustomNotificationDialog(pDialogType);
        registerDialog.show(getSupportFragmentManager(), "registerDialog");
    }


    /**
     * Registra al usuario en la base de datos.
     */
    public void registerUser() {
        String username = this.usernameInput.getText().toString();
        String password = this.passwordInput.getText().toString();

        Log.i("RegisterActivity", "THE USER WOULD BE REGISTERED BY NOW!!!");
        super.addUser(username, password);
    }


    /**
     * Guarda el usuario en las preferencias para describir la sesión,
     * limpia los valores insertados en los campos de texto y
     * redirige al usuario a la ventana Home.
     */
    private void setUserInPreferencesAndGoToHomePage() {
        String username = this.usernameInput.getText().toString();
        super.sessionManager.startUserSession(username);
        super.preferencesManager.clearAllInputtedTextInFields();

        Intent intent = new Intent(getApplication(), MainActivity.class);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    @Override
    public void onDestroy() {
        this.storeCurrentUserInput();
        super.onDestroy();
    }
}