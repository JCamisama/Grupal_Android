package com.example.grupal_android;

import android.media.Image;

public class Franquicia {

    private int logo;
    private String nombre;
    private String tipo;
    private String descripcion;
    private String url;


    public Franquicia(String nombre, String tipo, String descripcion, String url) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
