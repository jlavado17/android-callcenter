package com.example.callmobile.MODEL;

import com.google.gson.annotations.SerializedName;

public class Proyecto {
    @SerializedName("proye_id")
    private int proyeId;
    @SerializedName("proye_descripcion")
    private String proyeDescripcion;

    public Proyecto(int proyeId,String proyeDescripcion){
        this.proyeId = proyeId;
        this.proyeDescripcion = proyeDescripcion;
    }

    public int getProyeId() {
        return proyeId;
    }

    public void setProyeId(int proyeId) {
        this.proyeId = proyeId;
    }

    public String getProyeDescripcion() {
        return proyeDescripcion;
    }

    public void setProyeDescripcion(String proyeDescripcion) {
        this.proyeDescripcion = proyeDescripcion;
    }
}
