package com.romero.proyectofinalprueba.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.romero.proyectofinalprueba.R;

import java.util.ArrayList;

public class MercadoAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Jugador> jugadores;//Hay que cambiar el arrayList
    private LayoutInflater inflater;

    public MercadoAdapter(Context context, ArrayList<Jugador> jugadores) {
        this.context = context;
        this.jugadores = jugadores;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return jugadores.size();
    }

    @Override
    public Object getItem(int position) {
        return jugadores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_mercado, parent, false);
        }

        ImageView imagenJugador = convertView.findViewById(R.id.imgJugador);
        TextView posicion = convertView.findViewById(R.id.tvPosicion);
        TextView nombre = convertView.findViewById(R.id.tvNombre);
        TextView precio = convertView.findViewById(R.id.tvPrecio);
        TextView media = convertView.findViewById(R.id.tvMedia);
        ImageView imgFavorito = convertView.findViewById(R.id.imgFavourite);

        Jugador jugador = jugadores.get(position);

        imagenJugador.setImageResource(jugador.getUrlImg());
        posicion.setText(jugador.getPosicion());
        nombre.setText(jugador.getNombre());
        precio.setText(String.valueOf(jugador.getMonedas())+"M");
        media.setText(String.valueOf(jugador.getMedia())+"GRL");

        if(jugador.isFavorito()){
            imgFavorito.setImageResource(R.drawable.baseline_favorite_24);
        }else{
            imgFavorito.setImageResource(R.drawable.baseline_favorite_border_24);
        }

        imgFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jugador.setFavorito(!jugador.isFavorito());
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void actualizarLista(ArrayList<Jugador> nuevaLista) {
        jugadores.clear();  // Limpiar la lista actual
        jugadores.addAll(nuevaLista); // Agregar nuevos datos
        notifyDataSetChanged(); // Notificar cambios
    }



}
