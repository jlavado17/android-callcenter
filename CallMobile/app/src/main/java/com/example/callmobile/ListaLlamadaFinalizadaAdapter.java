package com.example.callmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.callmobile.MODEL.Operacion;

import java.util.List;

public class ListaLlamadaFinalizadaAdapter extends BaseAdapter {

    List<Operacion> operaciones;
    Context contexto;

    private static LayoutInflater inflater= null;

    public ListaLlamadaFinalizadaAdapter (Context context, List<Operacion> operaciones)
    {
        this.operaciones = operaciones;
        contexto = context;
        inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return operaciones.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView tvDescripcion;
        TextView tvCodigo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View fila;
        final Operacion operacion = operaciones.get(position);
        //creamos el objeto de lista
        fila = inflater.inflate(R.layout.activity_lista_llamada_finalizada, null);

        holder.tvDescripcion=(TextView) fila.findViewById(R.id.tvNombreLlamadaFinalizada);//referenciamos con el objeto de la vista
        holder.tvCodigo = (TextView) fila.findViewById(R.id.tvCodigoLlamadaFinalizada);//referenciamos con el objeto de la vista

        final String descripcion = operacion.getRellDescripcion();
        final String codigo = operacion.getRellCodigo();

        holder.tvDescripcion.setText(descripcion);//llenamos el objeto de la lista
        holder.tvCodigo.setText(codigo);//llenamos el objeto de la lista
        /*fila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(contexto, "Seleccionaste "+ nombre_completo,
                        Toast.LENGTH_LONG).show();
            }
        });*/
        return fila;
    }
}
