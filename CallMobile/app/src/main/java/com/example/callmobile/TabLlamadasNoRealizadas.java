package com.example.callmobile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.callmobile.API.PostService;
import com.example.callmobile.API.response.DataOperacion;
import com.example.callmobile.API.response.DefaultResponseRegister;
import com.example.callmobile.MODEL.Operacion;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.CONTEXT_IGNORE_SECURITY;
import static android.content.Context.TELEPHONY_SERVICE;

public class TabLlamadasNoRealizadas extends Fragment implements  View.OnClickListener{

    View view;
    private ViewPager viewPager;

    Button btnLlamar;
    private Context mContext = getActivity();
    private static final int REQUEST = 112;

    ListView miLista;

    //GRABACION DE AUDIO
    public MediaRecorder miGrabacion;
    private String outputFile = null;

    //SEGUNDO PLANO
    LongOperation runningTask;
    public int contador = 0;
    public int contador_final = 0;
    PhoneStateListener phoneStateListener;
    public String numeroTelefonico;
    public int operacionId;
    public String usuarioLlamada;
    public int estadoLlamada;
    //public int call = 1; //llamada activa o en ejecución

    //Conexión
    private Retrofit retrofit;
    private HttpLoggingInterceptor loggingInterceptor;
    private OkHttpClient.Builder httpClientBuilder;

    private List<Operacion> operaciones;
    private ListaLlamadaAdapter listaLlamadaAdapter;
    private Operacion operacion;


    private TextView tvIngresar;

    private String proyecto;
    private String cliente;

    private Spinner spEstadoOpLlamada;
    private Spinner spEstadoSecuOpLlamada;
    private String estadoOpLlamada;
    private String estadoSecuOpLlamada;
    private String nro_estado = "";
    private String nro_estado_secundario = "";

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
        view = inflater.inflate(R.layout.fragment_llamadas_no_realizadas, container, false);

        btnLlamar = (Button) view.findViewById(R.id.btnLlamar);
        btnLlamar.setEnabled(false);

        miLista = (ListView) view.findViewById(R.id.miLista);

        //Toast.makeText(getContext(),cliente,Toast.LENGTH_LONG).show();
        /*String[] values = new String []{"USUARIO 1","USUARIO 2","USUARIO 3","USUARIO 1","USUARIO 2","USUARIO 3"};*/

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,values);
        miLista.setAdapter(adapter);*/
        /** instanciamos nuestro listview **/
        lanzarPeticionOperacion(view);
        /*operaciones = new ArrayList<>();
        listaLlamadaAdapter = new ListaLlamadaAdapter(getContext(),operaciones);
        miLista.setAdapter(listaLlamadaAdapter)*/;//llenamos la lista
        /*miLista.setAdapter(new ListaLlamadaAdapter(getContext(),values));*/

        btnLlamar.setOnClickListener(this);

        return view;

    }

    private void lanzarPeticionOperacion(View view) {
        loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);
        retrofit = new Retrofit.Builder()
                .baseUrl("http://gateway.callcenter-madisac.tk/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();

        PostService client = retrofit.create(PostService.class);

        // agregamos los parámetros
        int usuario = 1;

        Call<DataOperacion> call = client.operacionCall(usuario,cliente,proyecto);
        call.enqueue(new Callback<DataOperacion>() {
            @Override
            public void onResponse(Call<DataOperacion> call, Response<DataOperacion> response) {
                operaciones = new ArrayList<>();
                operaciones = response.body().getResults();
                listaLlamadaAdapter = new ListaLlamadaAdapter(getContext(),operaciones);
                //listaLlamadaAdapter.setData(response.body().getResults());

                miLista.setAdapter(listaLlamadaAdapter);//llenamos la lista
                miLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for(int i=0; i< miLista.getChildCount();i++){
                            miLista.getChildAt(i).setBackgroundColor(Color.WHITE);
                        }
                        numeroTelefonico = operaciones.get(position).getRellTelefono();
                        operacionId = operaciones.get(position).getOpeId();
                        usuarioLlamada = operaciones.get(position).getRellDescripcion();
                        estadoLlamada = operaciones.get(position).getOpeEstadoLlamada();
                        view.setBackgroundColor(Color.LTGRAY);

                        btnLlamar.setEnabled(true);

                        Toast.makeText(getActivity(),"SELECCIONADO: "+ operaciones.get(position).getRellDescripcion(),Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void onFailure(Call<DataOperacion> call, Throwable t) {
                Log.e("TAG3",t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        Toast toast =  Toast.makeText(getActivity(),"Llamada", Toast.LENGTH_LONG);
        toast.show();
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},REQUEST);
        }
        else
        {
            boolean estado = false;
            //verificar si exite una item que esté con estado 4;
            if (operaciones.size() > 0){
                for (int i = 0 ; i < operaciones.size();i++){
                    if(operaciones.get(i).getOpeEstadoLlamada() == 4){
                        //Toast.makeText(getActivity(),operaciones.get(i).getOpeEstadoLlamada(),Toast.LENGTH_LONG).show();
                        usuarioLlamada = operaciones.get(i).getRellDescripcion();
                        estadoLlamada = operaciones.get(i).getOpeEstadoLlamada();
                        operacionId = operaciones.get(i).getOpeId();
                        estado = true;
                        mostrarVentanaCambiarEstado();
                        break;
                    }
                }
            }

            if(!estado) {
                if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_CALL_LOG}, 1001);
                }else{
                    /*** REALIZAR LLAMADA AUTOMÁTICA ***/
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:"+numeroTelefonico)); //el numero de telefono se tiene que traer desde la base de datos.
                    startActivity(i);

                    runningTask = new LongOperation(getActivity());//EJECUTAMOS LAS TAREAS EN SEGUNDO PLANO
                    runningTask.execute();
                    /***********************************/
                }
            }
        }
    }

    public void lanzarPeticion(int duracion2){
        loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);
        retrofit = new Retrofit.Builder()
                .baseUrl("http://gateway.callcenter-madisac.tk/")
                .addConverterFactory(GsonConverterFactory.create())//convierte de json a java y de java a json
                .client(httpClientBuilder.build())
                .build();

        PostService client = retrofit.create(PostService.class);//creamos la instancia del  cliente

        File file = new File(outputFile);//recogemos el archivo de audio
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("audio",file.getName(),requestFile);

        // agregamos los otros parametros
        String duracionString = String.valueOf(duracion2);
        RequestBody ope_duracion_llamada = RequestBody.create(MediaType.parse("multipart/form-data"), duracionString);
        RequestBody ope_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(operacionId));

        Call<DefaultResponseRegister> call = client.storeCall(body,ope_duracion_llamada,ope_id);
        //esto está en un hilo secundario
        call.enqueue(new Callback<DefaultResponseRegister>() {
            @Override
            public void onResponse(Call<DefaultResponseRegister> call, Response<DefaultResponseRegister> response) {
                if (operaciones.size() > 0){
                    for (int i = 0 ; i < operaciones.size();i++){
                        if(operaciones.get(i).getOpeId() == operacionId){
                            Log.d("TAG3","operacion: " + operacionId);
                            //Toast.makeText(getActivity(),operaciones.get(i).getOpeEstadoLlamada(),Toast.LENGTH_LONG).show();
                            operaciones.get(i).setOpeEstadoLlamada(4);
                            break;
                        }
                    }
                }
                Log.d("TAG1","Mensaje: " + response.body().getMessage());
            }

            @Override
            public void onFailure(Call<DefaultResponseRegister> call, Throwable t) {
                Log.d("TAG2","Error: " + t.getMessage());//informacion de porque ha surgido el error
            }
        });
    }

    public void mostrarVentanaCambiarEstado()
    {
        if(getActivity() != null){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_cambiar_estado_operacion,null);
            dialogBuilder.setView(dialogView);
            final TextView orderTextView = dialogView.findViewById(R.id.tvDialog);
            orderTextView.setText("Estado de Llamada");

            final TextView tvUsuarioLlamada = dialogView.findViewById(R.id.tvUsuarioLlamada);
            tvUsuarioLlamada.setText(usuarioLlamada);

            final TextView textEstadosSecuOpLlamada = (TextView) dialogView.findViewById(R.id.tvEstadosSecuOpLlamada);
            final Button btnGuardar = (Button) dialogView.findViewById(R.id.btnGuardarEstado);

            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
                    httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);
                    retrofit = new Retrofit.Builder()
                            .baseUrl("http://gateway.callcenter-madisac.tk/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClientBuilder.build())
                            .build();

                    PostService client = retrofit.create(PostService.class);

                    if(estadoOpLlamada.equals("EJECUTADA")){

                        Toast.makeText(getActivity(),"seleccionaste estado primario: " + estadoOpLlamada +", estado secundario: "+ estadoSecuOpLlamada,Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(),"seleccionaste estado primario: " + estadoOpLlamada,Toast.LENGTH_LONG).show();
                    }

                    switch (estadoOpLlamada){
                        case "EJECUTADA":
                            nro_estado = "2";
                            break;
                        case "FINALIZADA":
                            nro_estado = "3";
                            break;
                    }

                    if(nro_estado.equals("2")){
                        switch (estadoSecuOpLlamada){
                            case "NO CONTESTA":
                                nro_estado_secundario = "1";
                                break;
                            case "NRO. EQUIVOCADO":
                                nro_estado_secundario = "2";
                                break;
                            case "NO EXISTE":
                                nro_estado_secundario = "3";
                                break;
                            case "EN REUNIÓN":
                                nro_estado_secundario = "4";
                                break;
                            default:
                                nro_estado_secundario ="";
                                break;
                        }
                    }else{
                        nro_estado_secundario = "";
                    }

                    Call<DefaultResponseRegister> call = client.cambiarEstadoOperacionCall(operacionId,nro_estado,nro_estado_secundario);
                    call.enqueue(new Callback<DefaultResponseRegister>() {
                        @Override
                        public void onResponse(Call<DefaultResponseRegister> call, Response<DefaultResponseRegister> response) {
                            //lanzarPeticionOperacion(view);
                            //codigo para refrescar la actividad y por consecuente los fragmentos
                            new Handler().post(new Runnable() {

                                @Override
                                public void run()
                                {
                                    Intent intent = getActivity().getIntent();
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    getActivity().overridePendingTransition(0, 0);
                                    getActivity().finish();

                                    getActivity().overridePendingTransition(0, 0);
                                    startActivity(intent);
                                }
                            });
                            Log.i("TAG1",response.body().getMessage());
                        }
                        @Override
                        public void onFailure(Call<DefaultResponseRegister> call, Throwable t) {
                            Log.i("TAG1",t.getMessage());
                        }
                    });
                }
            });

            spEstadoOpLlamada = (Spinner) dialogView.findViewById(R.id.spEstadosOpLlamada);
            //Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
            R.array.estados_op_llamada, android.R.layout.simple_spinner_item);
            //Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Apply the adapter to the spinner
            spEstadoOpLlamada.setAdapter(adapter);

            spEstadoOpLlamada.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(parent.getContext(), (String) parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                    estadoOpLlamada = (String)parent.getItemAtPosition(position);
                    if(estadoOpLlamada.equals("EJECUTADA")){
                        spEstadoSecuOpLlamada.setVisibility(View.VISIBLE);
                        textEstadosSecuOpLlamada.setVisibility(View.VISIBLE);
                        spEstadoSecuOpLlamada.setEnabled(true);

                        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),
                                R.array.estados_secu_op_llamada, android.R.layout.simple_spinner_item);
                        // Specify the layout to use when the list of choices appears
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Apply the adapter to the spinner
                        spEstadoSecuOpLlamada.setAdapter(adapter2);
                    }else{
                        spEstadoSecuOpLlamada.setVisibility(View.INVISIBLE);
                        textEstadosSecuOpLlamada.setVisibility(View.INVISIBLE);
                        spEstadoSecuOpLlamada.setEnabled(false);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spEstadoSecuOpLlamada = (Spinner) dialogView.findViewById(R.id.spEstadosSecuOpLlamada);
            //spEstadoSecuOpLlamada.setEnabled(false);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),
            R.array.estados_secu_op_llamada, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spEstadoSecuOpLlamada.setAdapter(adapter2);

            spEstadoSecuOpLlamada.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(parent.getContext(), (String) parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                    estadoSecuOpLlamada = (String)parent.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        }

    }

    private final class LongOperation extends AsyncTask<Void, Integer, String> {

        Activity mContex;
        public  LongOperation(Activity contex)
        {
            this.mContex=contex;
        }


        @Override
        protected void onPreExecute() {
            contador = 0;
            contador_final = 0;

            /*** REALIZAR LA GRABACION ***/
            outputFile = Environment.getExternalStorageDirectory().
                    getAbsolutePath() + "/"+numeroTelefonico+".mp3";
            miGrabacion = new MediaRecorder(); //creamos una instancia de la clase MEDIA RECORDER
            //AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            //miGrabacion.  AudioManager.MODE_IN_CALL;
            miGrabacion.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);//definimos el microfono como medio de audio, estuvo en .MIC
            miGrabacion.setAudioEncodingBitRate(32);
            miGrabacion.setAudioSamplingRate(44100);
            miGrabacion.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            miGrabacion.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            miGrabacion.setOutputFile(outputFile);

            try {
                    miGrabacion.prepare();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            /***********************************/
            miGrabacion.start();
            //Toast.makeText(mContex, "Grabación iniciada correctamente", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(Void... params) {
            /*for(;contador <= 30 ;contador++){
                try {
                    Thread.sleep(1000);
                    publishProgress(contador);
                } catch (InterruptedException e) {
                    // cuando el segundo plano ha sido cancelado
                }
            }*/
            while(contador != -1){
                try {
                    Thread.sleep(1000);//dormir al segundo hilo 1 segundo
                    publishProgress(contador);
                    contador++;
                } catch (InterruptedException e) {
                    // cuando el segundo plano ha sido cancelado
                }
            }
            return "EJECUTANDO LA GRABACION";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Toast.makeText(mContex, "conteo grabacion: "+values[0], Toast.LENGTH_LONG).show();
            phoneStateListener = new PhoneStateListener() {
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    super.onCallStateChanged(state, incomingNumber);
                    if (state == TelephonyManager.CALL_STATE_RINGING) {
                        /*contador_final = contador;
                        contador = -2;*/
                    } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                        if(contador_final == 0){
                            contador_final = contador;
                        }
                        contador = -2;
                    } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                        // A call is dialing, active or on hold
                        //miGrabacion.start();
                        //contador = 0;
                    }
                }
            };
            TelephonyManager mgr = (TelephonyManager) mContex.getSystemService(TELEPHONY_SERVICE);
            if (mgr != null) {
                mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (miGrabacion != null) {
                miGrabacion.stop();//detenemos la grabacion
                miGrabacion.release();//pasamos la grabacion ha estado finalizado
                miGrabacion = null;//volvemos la grabacion a null
            }
            Toast.makeText(mContex, outputFile, Toast.LENGTH_LONG).show();
            lanzarPeticion(contador_final);
            mostrarVentanaCambiarEstado();
        }
    }

}
