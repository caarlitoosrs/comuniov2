package com.romero.proyectofinalprueba.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romero.proyectofinalprueba.R;


public class FragmentEquipo extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equipo, container, false);

        //fragmento de sonido
        Fragment sonidoFragment = new FragmentSonido();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_sonido_container, sonidoFragment)
                .commit();

        //fragmento de video
        Fragment videoFragment = new FragmentVideo();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_video_container, videoFragment)
                .commit();
        return view;
    }



}