package com.example.callmobile.API.response;

import com.example.callmobile.MODEL.Cliente;

import java.util.List;

public class DataCliente {
    private List<Cliente> results;

    public List<Cliente> getResults() {
        return results;
    }

    public void setResults(List<Cliente> results) {
        this.results = results;
    }
}
