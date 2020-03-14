package com.example.callmobile.API;
import android.provider.ContactsContract;

import com.example.callmobile.API.response.DataCliente;
import com.example.callmobile.API.response.DataOperacion;
import com.example.callmobile.API.response.DataProyecto;
import com.example.callmobile.API.response.DefaultResponseRegister;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PostService {
    @Multipart
    @POST("gateway/operacion_llamadas/actualizar_operacion")
    Call<DefaultResponseRegister> storeCall(
            @Part MultipartBody.Part audio,
            @Part("ope_duracion_llamada") RequestBody ope_duracion_llamada,
            @Part("ope_id") RequestBody ope_id
    );

    @FormUrlEncoded
    @POST("gateway/login")
    Call<DefaultResponseRegister> loginCall(
            @Field("usuario") String usuario,
            @Field("contrasenia") String contrasenia
    );

    @FormUrlEncoded
    @POST("gateway/operacion_llamadas/listar/id")
    Call<DataOperacion> operacionCall(
            @Field("locu_id") int id,
            @Field("cli_razon_social") String cliente,
            @Field("proye_descripcion") String proyecto
    );

    @FormUrlEncoded
    @POST("gateway/operacion_llamadas/llamadas_finalizadas/listar/id")
    Call<DataOperacion> operacionLLamadasRalizadasCall(
            @Field("locu_id") int id,
            @Field("cli_razon_social") String cliente,
            @Field("proye_descripcion") String proyecto
    );

    @FormUrlEncoded
    @POST("gateway/cliente/llenar_spinner")
    Call<DataCliente> spinnerClienteCall(
            @Field("locu_id") int locu_id
    );

    @FormUrlEncoded
    @POST("gateway/proyecto/llenar_spinner")
    Call<DataProyecto> spinnerProyectoCall(
            @Field("locu_id") int locu_id,
            @Field("cli_razon_social") String cli_razon_social
    );

    @FormUrlEncoded
    @POST("gateway/operacion_llamadas/cambiar_estado")
    Call<DefaultResponseRegister> cambiarEstadoOperacionCall(
            @Field("ope_id") int ope_id,
            @Field("estado_principal") String estado_principal,
            @Field("estado_secundario") String estado_secundario
    );
}
