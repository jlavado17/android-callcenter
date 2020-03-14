package com.example.callmobile.MODEL;

import com.google.gson.annotations.SerializedName;

public class Operacion {
    @SerializedName("ope_id")
    private int opeId;
    @SerializedName("rell_telefono")
    private String rellTelefono;
    @SerializedName("rell_nombre")
    private String rellNombre;
    @SerializedName("rell_apellidos")
    private String rellApellidos;
    @SerializedName("ope_duracion_llamada")
    private int opeDuracionLlamada;
    @SerializedName("ope_nombre_archivo")
    private String opeNombreArchivo;
    @SerializedName("locu_id")
    private int locuId;

    @SerializedName("ope_estado_llamada")
    private int opeEstadoLlamada;

    @SerializedName("rell_codigo")
    private String rellCodigo;
    @SerializedName("rell_descripcion")
    private String rellDescripcion;



    public int getOpeId() {
        return opeId;
    }

    public void setOpeId(int opeId) {
        this.opeId = opeId;
    }

    public String getRellTelefono() {
        return rellTelefono;
    }

    public void setRellTelefono(String rellTelefono) {
        this.rellTelefono = rellTelefono;
    }

    public String getRellNombre() {
        return rellNombre;
    }

    public void setRellNombre(String rellNombre) {
        this.rellNombre = rellNombre;
    }

    public String getRellApellidos() {
        return rellApellidos;
    }

    public void setRellApellidos(String rellApellidos) {
        this.rellApellidos = rellApellidos;
    }

    public int getOpeDuracionLlamada() {
        return opeDuracionLlamada;
    }

    public void setOpeDuracionLlamada(int opeDuracionLlamada) {
        this.opeDuracionLlamada = opeDuracionLlamada;
    }

    public String getOpeNombreArchivo() {
        return opeNombreArchivo;
    }

    public void setOpeNombreArchivo(String opeNombreArchivo) {
        this.opeNombreArchivo = opeNombreArchivo;
    }

    public int getLocuId() {
        return locuId;
    }

    public void setLocuId(int locuId) {
        this.locuId = locuId;
    }

    public int getOpeEstadoLlamada() {
        return opeEstadoLlamada;
    }

    public void setOpeEstadoLlamada(int opeEstadoLlamada) {
        this.opeEstadoLlamada = opeEstadoLlamada;
    }

    public String getRellCodigo() {
        return rellCodigo;
    }

    public void setRellCodigo(String rellCodigo) {
        this.rellCodigo = rellCodigo;
    }

    public String getRellDescripcion() {
        return rellDescripcion;
    }

    public void setRellDescripcion(String rellDescripcion) {
        this.rellDescripcion = rellDescripcion;
    }
}
