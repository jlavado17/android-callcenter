package com.example.callmobile.API.response;


import com.example.callmobile.MODEL.Proyecto;

import java.util.List;

public class DataProyecto {
    private List<Proyecto> results;

    public List<Proyecto> getResults() {
        return results;
    }

    public void setResults(List<Proyecto> results) {
        this.results = results;
    }
}
