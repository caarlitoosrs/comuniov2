package com.romero.proyectofinalprueba.fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.romero.proyectofinalprueba.R;
import com.romero.proyectofinalprueba.models.ui.main.EquipoViewModel;

import org.jetbrains.annotations.Nullable;


public class FragmentVideo extends Fragment {

    private VideoView videoView;
    private EquipoViewModel equipoViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        videoView = view.findViewById(R.id.videoViewEquipo);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MediaController mediaController = new MediaController(requireContext());
        mediaController.setAnchorView(videoView);
        equipoViewModel = new ViewModelProvider(requireActivity()).get(EquipoViewModel.class);
        equipoViewModel.getEquipoSeleccionado().observe(getViewLifecycleOwner(), equipo -> {

            // Construir la URI para el video basado en el equipo seleccionado
            String videoPath = "android.resource://" + getActivity().getPackageName() + "/" + equipo.getVideoResId();
            videoView.setVideoURI(Uri.parse(videoPath));
            videoView.setMediaController(mediaController);
            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.start();
                }
            });

        });
    }
}