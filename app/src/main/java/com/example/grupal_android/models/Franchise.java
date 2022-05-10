package com.example.grupal_android.models;

import android.graphics.Bitmap;

public class Franchise {
    String name;
    Bitmap logo;
    String type_ES;
    String type_EN;
    String description_ES;
    String description_EN;
    String url;

    public Franchise(String name, Bitmap logo, String type_ES, String type_EN, String description_ES, String description_EN, String url) {
        this.name = name;
        this.logo = logo;
        this.type_ES = type_ES;
        this.type_EN = type_EN;
        this.description_ES = description_ES;
        this.description_EN = description_EN;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }

    public String getType_ES() {
        return type_ES;
    }

    public void setType_ES(String type_ES) {
        this.type_ES = type_ES;
    }

    public String getType_EN() {
        return type_EN;
    }

    public void setType_EN(String type_EN) {
        this.type_EN = type_EN;
    }

    public String getDescription_ES() {
        return description_ES;
    }

    public void setDescription_ES(String description_ES) {
        this.description_ES = description_ES;
    }

    public String getDescription_EN() {
        return description_EN;
    }

    public void setDescription_EN(String description_EN) {
        this.description_EN = description_EN;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}


