package com.example.callmobile.LOGIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.callmobile.API.PostService;
import com.example.callmobile.API.response.DefaultResponseRegister;
import com.example.callmobile.FiltroActivity;
import com.example.callmobile.MainActivity;
import com.example.callmobile.R;

import java.io.File;
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

public class LoginActivity extends AppCompatActivity {
    private Button btnIngresar;
    private EditText etUsuario,etContrasena;

    private TextView tvIngresar;

    private TextView tvError;

    private ProgressBar progressBar;

    private static String usuario = "admin";
    private static String contrasena= "123";

    //Conexión
    private Retrofit retrofit;
    private HttpLoggingInterceptor loggingInterceptor;
    private OkHttpClient.Builder httpClientBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etContrasena = (EditText) findViewById(R.id.etContrasena);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);

        tvIngresar = (TextView) findViewById(R.id.tvIngresar);

        tvError = (TextView) findViewById(R.id.tvErrorLogin);

        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);

        progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN);

        //para cambiar el tipo de fuente
        /*Typeface fuente = Typeface.createFromAsset(getAssets(),"fonts/PIZZARIA.ttf");
        tvIngresar.setTypeface(fuente);*/

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(etUsuario.getText().toString().trim().length() == 0  && etContrasena.getText().toString().trim().length() == 0)
                {
                    Toast.makeText(getApplicationContext(),"Los campos están vacios",Toast.LENGTH_LONG).show();
                }else{
                    peticion();
                }
            }
        });
    }

    public void peticion(){
        loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);
        retrofit = new Retrofit.Builder()
                .baseUrl("http://gateway.callcenter-madisac.tk/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build();

        PostService client = retrofit.create(PostService.class);

        // agregamos los parámetros
        final String usuario = etUsuario.getText().toString().trim();
        final String contrasenia = etContrasena.getText().toString().trim();

        Call<DefaultResponseRegister> call = client.loginCall(usuario,contrasenia);

        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<DefaultResponseRegister>() {


            @Override
            public void onResponse(Call<DefaultResponseRegister> call, Response<DefaultResponseRegister> response) {
                //Toast.makeText(LoginActivity.this,"Usuario logeado correctamente",Toast.LENGTH_LONG);
                if(response.isSuccessful()){
                    SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);//mode private es para solo verlo en mi aplicación
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("idUsuarioLogueado",response.body().getMessage());
                    editor.commit();//guardamos nuestras preferencias

                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(LoginActivity.this, FiltroActivity.class);
                    startActivity(intent);
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    //Toast.makeText(LoginActivity.this,"Error al loguearse",Toast.LENGTH_LONG);
                    //Toast.makeText(getApplication(),"Error al loguearse",Toast.LENGTH_LONG);
                    tvError.setText("Error al loguearse");

                    Log.i("TAG","Error al loguearse");
                }

            }
            @Override
            public void onFailure(Call<DefaultResponseRegister> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                tvError.setText("No se pudo comunicar con el servicio");
                Log.e("TAG",t.getLocalizedMessage());
                //Toast.makeText(getApplicationContext(),"No se puedo conectar con el servicio",Toast.LENGTH_LONG);
            }
        });
    }

}
