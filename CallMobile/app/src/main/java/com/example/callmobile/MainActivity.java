package com.example.callmobile;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.UnicodeSetSpanner;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.callmobile.API.PostService;
import com.example.callmobile.API.response.DefaultResponseRegister;
import com.example.callmobile.LOGIN.LoginActivity;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.IOException;

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


public class MainActivity extends AppCompatActivity {

    private Context mContext = MainActivity.this;
    private static final int REQUEST = 112;
    private ViewPager viewPager;

    private String cliente;
    private String proyecto;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtenemos el bundle
        Bundle bundle = getIntent().getExtras();
        //obtenemos los textos
        cliente = bundle.getString("clienteActual");
        proyecto = bundle.getString("proyectoActual");

        //Toast.makeText(this,proyecto, Toast.LENGTH_LONG).show();

        //creamos el bundle
        //Bundle args = new Bundle();

        //colocamos el string
        //args.putString("clienteActual",cliente);
        //args.putString("proyecto",proyecto);

        //TabLlamadasNoRealizadas tabLlamadasNoRealizadas = new TabLlamadasNoRealizadas();
        //tabLlamadasNoRealizadas.setArguments(args);

        //permisos

        if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_CALL_LOG},REQUEST);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Call Mobile");
        //toolbar.setSubtitle("Bienvenido");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("No Realizadas"));
        tabLayout.addTab(tabLayout.newTab().setText("Realizadas"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        pageAdapter.setCliente(cliente);
        pageAdapter.setProyecto(proyecto);
        viewPager = (ViewPager) findViewById(R.id.pager);

        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String message = "";
        switch (item.getItemId()){
            case R.id.logout:
                message = "Sesión Cerrada";
                /** Nos dirigimos al logout **/
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                /*****************************/
                break;
            default:
                message = "Opción Desconocida";
                break;
        }
        Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }
}


