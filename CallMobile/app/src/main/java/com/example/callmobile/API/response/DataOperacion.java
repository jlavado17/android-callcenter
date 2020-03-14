package com.example.callmobile.API.response;

import com.example.callmobile.MODEL.Operacion;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.List;

public class DataOperacion implements Serializable {

    private List<Operacion>results;

    public List<Operacion> getResults() {
        return results;
    }

    public void setResults(List<Operacion> results) {
        this.results = results;
    }

}
