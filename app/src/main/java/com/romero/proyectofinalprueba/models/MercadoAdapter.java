package com.romero.proyectofinalprueba.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.romero.proyectofinalprueba.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MercadoAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Jugador> jugadores;
    private LayoutInflater inflater;
    private float tamanioTexto = 14f;

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

    public void setTextSize(float sizeSp) {
        this.tamanioTexto = sizeSp;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_mercado, parent, false);
        }

        ImageView imagenJugador = convertView.findViewById(R.id.imgJugador);
        TextView posicion = convertView.findViewById(R.id.tvPosicion);
        TextView nombre = convertView.findViewById(R.id.tvNombre);
        TextView precio = convertView.findViewById(R.id.tvPrecio);
        TextView media = convertView.findViewById(R.id.tvMedia);
        ImageView imgFavorito = convertView.findViewById(R.id.imgFavourite);

        posicion.setTextSize(tamanioTexto);
        nombre.setTextSize(tamanioTexto);
        precio.setTextSize(tamanioTexto);
        media.setTextSize(tamanioTexto);

        Jugador jugador = jugadores.get(position);


        String imageUrl = jugador.getUrlImg();
        if (imageUrl.contains("github.com")) {
            imageUrl = imageUrl.replace("github.com", "raw.githubusercontent.com").replace("/blob/", "/");
        }

        Picasso.get().load(imageUrl).into(imagenJugador);


        posicion.setText(jugador.getPosicion());
        nombre.setText(jugador.getNombre());
        precio.setText(jugador.getMonedas() + "M");
        media.setText(jugador.getMedia() + "GRL");

        if (jugador.isFavorito()) {
            imgFavorito.setImageResource(R.drawable.baseline_favorite_24);
        } else {
            imgFavorito.setImageResource(R.drawable.baseline_favorite_border_24);
        }

        imgFavorito.setOnClickListener(v -> {
            jugador.setFavorito(!jugador.isFavorito());
            notifyDataSetChanged();
        });

        return convertView;
    }

}
