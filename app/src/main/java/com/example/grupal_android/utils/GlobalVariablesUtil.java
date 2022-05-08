package com.example.grupal_android.utils;

/**
 * Clase que registra las variables globales utilizadas en la aplicación.
 */
public class GlobalVariablesUtil {
    // Dialog types
    public static final String WRONG_CREDENTIALS = "wrongCreds";
    public static final String NO_EMPTY_FIELDS = "noEmptyFields";
    public static final String PASSWORDS_NOT_MATCH = "pwNotMatch";
    public static final String USER_ALREADY_EXISTS = "userAlreadyExists";


    // Preferences
    public static final String CURRENT_USER = "currentUser";


    // Keys para guardar datos en preferencias
    public static final String INPUTTED_USER = "inputUser";
    public static final String INPUTTED_PASSWORD = "inputPassword";
    public static final String REGISTER_USER = "registerInputUser";
    public static final String REGISTER_PASSWORD = "registerInputPassword";
    public static final String REGISTER_REPEAT_PASSWORD = "registerInputRepeat";

    // Remote database PHP file URIs
    public static final String REMOTE_SERVER =  "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/jcagudelo001/WEB";
    public static final String GET_USER_BY_ID_PHP = "select_user_by_username_grupal.php";
    public static final String INSERT_USER_PHP = "insert_user_grupal.php";

}