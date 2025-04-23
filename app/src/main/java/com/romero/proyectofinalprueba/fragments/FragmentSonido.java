package com.romero.proyectofinalprueba.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.romero.proyectofinalprueba.R;
import com.romero.proyectofinalprueba.models.ui.main.EquipoViewModel;

import org.jetbrains.annotations.Nullable;

public class FragmentSonido extends Fragment {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private ImageButton btnPlayPause;
    private Handler handler = new Handler(Looper.getMainLooper());  //Actualiza el proceso del seekbar
    private Runnable updateSeekBar; //Actualiza el proceso del seekbar
    private EquipoViewModel equipoViewModel; //ViewModel compartido para obetener el equipo y su audio
    private boolean isPreparado = false;

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup cont, Bundle b) {
        View view = inf.inflate(R.layout.fragment_sonido, cont, false);
        seekBar = view.findViewById(R.id.seekBarAudio);
        btnPlayPause = view.findViewById(R.id.btnPlayPause);
        btnPlayPause.setImageResource(R.drawable.baseline_play_arrow_24);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle b) {
        super.onViewCreated(v, b);

        //Inicializa el ViewModel
        equipoViewModel = new ViewModelProvider(requireActivity()).get(EquipoViewModel.class);

        // 1) Runnable para actualizar la SeekBar
        updateSeekBar = () -> {
            if (mediaPlayer != null) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(updateSeekBar, 500);
            }
        };

        // 2) Observa el equipo y crea el MediaPlayer dentro del observer
        equipoViewModel.getEquipoSeleccionado().observe(getViewLifecycleOwner(), equipo -> {
            if (equipo == null) return;

            // Libera el anterior
            if (mediaPlayer != null) {
                handler.removeCallbacks(updateSeekBar);
                mediaPlayer.release();
            }

            // Crea el MediaPlayer con la canción del equipo
            int audioRes = equipo.getCancionResId();
            mediaPlayer = MediaPlayer.create(getContext(), audioRes);

            mediaPlayer.setOnPreparedListener(mp -> {
                seekBar.setMax(mp.getDuration());// solo inicializamos SeekBar, no arrancamos la reproducción
                isPreparado = true;
            });

            //Se ejecuta cuando mediaplayer termina de reproducir la psita
            mediaPlayer.setOnCompletionListener(mp -> {
                btnPlayPause.setImageResource(R.drawable.baseline_play_arrow_24);
                handler.removeCallbacks(updateSeekBar); // Detiene la actualización periódica de la SeekBar
            });
        });

        //Es cuando el usuario pulsa play/pause(boton cambia)
        btnPlayPause.setOnClickListener(btn -> {
            if (mediaPlayer == null || !isPreparado) return;

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause(); //Si se esta reproduciendo el audio, lo pausamos
                btnPlayPause.setImageResource(R.drawable.baseline_play_arrow_24);
                handler.removeCallbacks(updateSeekBar);
            } else {
                mediaPlayer.start();
                btnPlayPause.setImageResource(R.drawable.baseline_pause_24);
                handler.post(updateSeekBar);
            }
        });

        // 4) SeekBar draggable, sirve para arrastrar la barra y cambiar la posicion de reproduccion del audio
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar sb, int prog, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(prog);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar sb) { } //Se llama cuando pones el pulgar en la barra para arrastrarla
            @Override public void onStopTrackingTouch(SeekBar sb) { } //Se llama cuando sueltas el pulgar para terminar de arrastrar la barra
        });
    }


    //En este caso sirve para que cuando cambies de fragmento se detenga la reproduccion
    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlayPause.setImageResource(R.drawable.baseline_play_arrow_24);
        }
    }

    //Elimina callbacks pendientes del Handler para que no siga actualizando la seekbar, además libera el Mediaplayer para evitar fugas de memoria
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            handler.removeCallbacks(updateSeekBar);
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}