package com.example.callmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.callmobile.API.PostService;
import com.example.callmobile.API.response.DataCliente;
import com.example.callmobile.API.response.DataOperacion;
import com.example.callmobile.API.response.DataProyecto;
import com.example.callmobile.API.response.DefaultResponseRegister;
import com.example.callmobile.LOGIN.LoginActivity;
import com.example.callmobile.MODEL.Cliente;
import com.example.callmobile.MODEL.Operacion;
import com.example.callmobile.MODEL.Proyecto;

import org.parceler.Parcels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FiltroActivity extends AppCompatActivity{

    //Conexión
    private Retrofit retrofit;
    private HttpLoggingInterceptor loggingInterceptor;
    private OkHttpClient.Builder httpClientBuilder;

    Spinner spCliente,spProyecto;

    //arrays
    private List<Cliente> clientes;
    private String clienteActual;
    private List<Proyecto> proyectos;
    private String proyectoActual;

    TabLlamadasNoRealizadas fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        spCliente = (Spinner) findViewById(R.id.spCliente);
        // Create an ArrayAdapter using the string array and a default spinner layout
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                //R.array.clientes, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
       // spCliente.setAdapter(adapter);

        spProyecto = (Spinner) findViewById(R.id.spProyecto);
        //spProyecto.setEnabled(false);
        // Create an ArrayAdapter using the string array and a default spinner layout
        //ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                //R.array.proyectos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        //spProyecto.setAdapter(adapter2);

        Button btnFiltrar = (Button) findViewById(R.id.btnFiltrar);

        clientes = new ArrayList<Cliente>();
        peticionLlenarCliente(); //llenamos el spinner de clientes
        spCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(parent.getContext(), (String) parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                clienteActual = (String)parent.getItemAtPosition(position);//se guarda el cliente actual
                peticionLlenarProyecto((String)parent.getItemAtPosition(position));//llenamos el spinner de proyecto
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        proyectos = new ArrayList<Proyecto>();
        spProyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                proyectoActual = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //peticionLlenarProyecto(clientes.get(0).getCliRazonSocial());//como inicial hace esto
        //spProyecto.setOnItemClickListener((AdapterView.OnItemClickListener) this);


        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Inicializamos el Bundle
                Bundle bundle = new Bundle();

                Intent intent = new Intent(FiltroActivity.this, MainActivity.class);
                // Agregamos la información al Bundle
                bundle.putString("clienteActual", clienteActual);
                bundle.putString("proyectoActual", proyectoActual);
                // Agregamos el Bundle al Intent e iniciamos el main activity
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void peticionLlenarCliente(){
        loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);
        retrofit = new Retrofit.Builder()
                .baseUrl("http://gateway.callcenter-madisac.tk/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();

        PostService client = retrofit.create(PostService.class);

        //conseguimos el locutor logueado
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        final String locu_id = preferences.getString("idUsuarioLogueado","No existe usuario logueado");
        ///////

        Call<DataCliente> call = client.spinnerClienteCall(Integer.parseInt(locu_id));

        call.enqueue(new Callback<DataCliente>() {

            @Override
            public void onResponse(Call<DataCliente> call, Response<DataCliente> response) {
                if(response.isSuccessful()){
                    for(int i=0; i < response.body().getResults().size(); i++){
                        Cliente cliente = new Cliente(response.body().getResults().get(i).getCliId(),
                                response.body().getResults().get(i).getCliRazonSocial());
                        clientes.add(cliente);
                    }
                    llenarSpinnerCliente();
                }
            }
            @Override
            public void onFailure(Call<DataCliente> call, Throwable t) {
            }
        });
    }

    public void peticionLlenarProyecto(String cliente){
        proyectos.clear(); ; //vaciamos el array
        loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);
        retrofit = new Retrofit.Builder()
                .baseUrl("http://gateway.callcenter-madisac.tk/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();

        PostService client = retrofit.create(PostService.class);

        //conseguimos el locutor logueado
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        final String locu_id = preferences.getString("idUsuarioLogueado","No existe usuario logueado");
        ///////

        //Toast.makeText(this,cliente,Toast.LENGTH_LONG).show();
        Call<DataProyecto> call = client.spinnerProyectoCall(Integer.parseInt(locu_id),cliente);
        call.enqueue(new Callback<DataProyecto>() {
            @Override
            public void onResponse(Call<DataProyecto> call, Response<DataProyecto> response) {
                if(response.isSuccessful()){
                    for(int i=0; i < response.body().getResults().size(); i++){
                        Proyecto proyecto = new Proyecto(response.body().getResults().get(i).getProyeId(),
                                response.body().getResults().get(i).getProyeDescripcion());
                        proyectos.add(proyecto);
                        llenarSpinnerProyecto();
                    }

                }
            }
            @Override
            public void onFailure(Call<DataProyecto> call, Throwable t) {
            }
        });
    }

    public void llenarSpinnerCliente(){
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < clientes.size(); i++) {
            lables.add(clientes.get(i).getCliRazonSocial());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCliente.setAdapter(spinnerAdapter);
    }

    public void llenarSpinnerProyecto(){
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < proyectos.size(); i++) {
            lables.add(proyectos.get(i).getProyeDescripcion());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProyecto.setAdapter(spinnerAdapter);
    }
}
