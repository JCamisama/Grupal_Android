package com.example.grupal_android.models;

/**
 * Modelo de la entidad User.
 */
public class User {

    // Attributes
    private String username;
    private String password;


    // Constructors
    public User() {}

    public User(String pUsername, String pPassword) {
        this.username = pUsername;
        this.password = pPassword;
    }


    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    // toString() Method
    @Override
    public String toString() { return this.getUsername(); }


}
