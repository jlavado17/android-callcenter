package com.example.callmobile.MODEL;

import com.google.gson.annotations.SerializedName;

public class Cliente {
    @SerializedName("cli_id")
    private int cliId;
    @SerializedName("cli_razon_social")
    private String cliRazonSocial;

    public Cliente(int cliId,String cliRazonSocial){
        this.cliId = cliId;
        this.cliRazonSocial = cliRazonSocial;
    }

    public int getCliId() {
        return cliId;
    }

    public void setCliId(int cliId) {
        this.cliId = cliId;
    }

    public String getCliRazonSocial() {
        return cliRazonSocial;
    }

    public void setCliRazonSocial(String cliRazonSocial) {
        this.cliRazonSocial = cliRazonSocial;
    }
}
