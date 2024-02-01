package com.ammar.fyp.ModelClasses.FeaturesJsonHandling;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Properties {

    @SerializedName("ICON_PROPERTY")
    @Expose
    private String iconProperty;
    @SerializedName("Id")
    @Expose
    private Double id;

    public String getIconProperty() {
        return iconProperty;
    }

    public void setIconProperty(String iconProperty) {
        this.iconProperty = iconProperty;
    }

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

}