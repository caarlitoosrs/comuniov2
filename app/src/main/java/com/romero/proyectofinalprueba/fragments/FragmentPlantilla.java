package com.romero.proyectofinalprueba.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.romero.proyectofinalprueba.R;
import com.romero.proyectofinalprueba.models.Jugador;
import com.romero.proyectofinalprueba.models.ui.main.SharedViewModel;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class FragmentPlantilla extends Fragment {


    private ImageView imgDelantero, imgMediapunta, imgMediocentro, imgDefensa, imgPortero;
    private TextView tvValor;
    private SharedViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plantilla, container, false);

        imgDelantero = view.findViewById(R.id.imgDelantero);
        imgMediapunta = view.findViewById(R.id.imgMediapunta);
        imgMediocentro = view.findViewById(R.id.imgMediocentro);
        imgDefensa = view.findViewById(R.id.imgDefensa);
        imgPortero = view.findViewById(R.id.imgPortero);
        tvValor = view.findViewById(R.id.tvValorEquipo);

        SharedPreferences prefs = requireActivity().getSharedPreferences("configuraciones", Context.MODE_PRIVATE);
        float textoSizeSp = prefs.getFloat("texto_size_sp", 18f);

        tvValor.setTextSize(textoSizeSp);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        //Muestra el valor total de la plantilla en el textView
        viewModel.getValorTotalEquipo().observe(getViewLifecycleOwner(), valor -> {
            tvValor.setText("Valor del equipo: " + valor + "M");
        });

        //Muestra los jugadores comprados y las imágenes
        viewModel.getJugadoresComprados().observe(getViewLifecycleOwner(), jugadores -> {
            if (jugadores.containsKey("Delantero")) {
                Picasso.get()
                        .load(jugadores.get("Delantero").getUrlImg()) // Aquí se pasa la URL
                        .into(imgDelantero); // Carga la imagen en el ImageView
            }
            if (jugadores.containsKey("Mediapunta")) {
                Picasso.get()
                        .load(jugadores.get("Mediapunta").getUrlImg())
                        .into(imgMediapunta);
            }
            if (jugadores.containsKey("Mediocampista")) {
                Picasso.get()
                        .load(jugadores.get("Mediocampista").getUrlImg())
                        .into(imgMediocentro);
            }
            if (jugadores.containsKey("Defensa")) {
                Picasso.get()
                        .load(jugadores.get("Defensa").getUrlImg())
                        .into(imgDefensa);
            }
            if (jugadores.containsKey("Portero")) {
                Picasso.get()
                        .load(jugadores.get("Portero").getUrlImg())
                        .into(imgPortero);
            }
        });


        return view;
    }

    public void setTextSize(float sizeSp) {

        SharedPreferences prefs = requireActivity().getSharedPreferences("configuraciones", Context.MODE_PRIVATE);
        prefs.edit().putFloat("texto_size_sp", sizeSp).apply();

        TextView tvValorEquipo = getView().findViewById(R.id.tvValorEquipo);
        tvValorEquipo.setTextSize(sizeSp);
    }

}