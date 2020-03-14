package com.example.callmobile;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.callmobile.API.PostService;
import com.example.callmobile.API.response.DataOperacion;
import com.example.callmobile.MODEL.Operacion;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TabLlamadasRealizadas extends Fragment {

    //Conexión
    private Retrofit retrofit;
    private HttpLoggingInterceptor loggingInterceptor;
    private OkHttpClient.Builder httpClientBuilder;

    private List<Operacion> operaciones;
    private ListaLlamadaFinalizadaAdapter listaLlamadaFinalizadaAdapter;

    private String proyecto;
    private String cliente;

    ListView miListaLlamadasRealizadas;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // los argumentos del bundle siempre deben estar en el onCreate
        if(getArguments() != null){
            cliente = getArguments().getString("cliente","no hay cliente");
            proyecto = getArguments().getString("proyecto", "no hay proyecto");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_llamadas_realizadas, container, false);
        miListaLlamadasRealizadas = (ListView) view.findViewById(R.id.miListaLlamadasRealizadas);
        lanzarPeticionLlamadasFinalizadas(view);
        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void lanzarPeticionLlamadasFinalizadas(View view){

        loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);
        retrofit = new Retrofit.Builder()
                .baseUrl("http://gateway.callcenter-madisac.tk/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();

        PostService client = retrofit.create(PostService.class);

        Call<DataOperacion> call = client.operacionLLamadasRalizadasCall(1,cliente,proyecto);
        call.enqueue(new Callback<DataOperacion>() {
            @Override
            public void onResponse(Call<DataOperacion> call, Response<DataOperacion> response) {
                operaciones = new ArrayList<>();
                operaciones = response.body().getResults();
                listaLlamadaFinalizadaAdapter = new ListaLlamadaFinalizadaAdapter(getContext(),operaciones);
                //listaLlamadaAdapter.setData(response.body().getResults());

                miListaLlamadasRealizadas.setAdapter(listaLlamadaFinalizadaAdapter);//llenamos la lista.
            }
            @Override
            public void onFailure(Call<DataOperacion> call, Throwable t) {

            }
        });
    }
}
