package com.example.callmobile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter {
    int count_tab;
    private String cliente;
    private String proyecto;
    public PageAdapter(FragmentManager fm,int count_tab) {
        super(fm);
        this.count_tab = count_tab;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    @Override
    public Fragment getItem(int position) {
        //creamos el bundle
        Bundle args = new Bundle();

        //colocamos el string
        args.putString("cliente",this.cliente);
        args.putString("proyecto",this.proyecto);
        switch (position){
            case 0:
                TabLlamadasNoRealizadas tabLlamadasNoRealizadas = new TabLlamadasNoRealizadas();
                tabLlamadasNoRealizadas.setArguments(args);
                tabLlamadasNoRealizadas.setRetainInstance(true);
                return tabLlamadasNoRealizadas;
            case 1:
                TabLlamadasRealizadas tabLlamadasRealizadas = new TabLlamadasRealizadas();
                tabLlamadasRealizadas.setArguments(args);
                tabLlamadasRealizadas.setRetainInstance(true);
                return tabLlamadasRealizadas;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count_tab;
    }
}
