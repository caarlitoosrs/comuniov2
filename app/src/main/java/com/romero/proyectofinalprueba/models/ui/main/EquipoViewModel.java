package com.romero.proyectofinalprueba.models.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.romero.proyectofinalprueba.models.Equipo;
import com.romero.proyectofinalprueba.models.Jugador;

import java.util.ArrayList;

public class EquipoViewModel extends ViewModel {

    private final MutableLiveData<Equipo> equipoSeleccionado = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Jugador>> jugadores = new MutableLiveData<>(new ArrayList<>());

    public void setEquipoSeleccionado(Equipo equipo) {
        equipoSeleccionado.setValue(equipo);
    }

    public LiveData<Equipo> getEquipoSeleccionado() {
        return equipoSeleccionado;
    }

    public void setListaJugadores(ArrayList<Jugador> lista) {
        jugadores.setValue(lista);
    }

    public ArrayList<Jugador> getListaJugadores() {
        return jugadores.getValue();
    }

}
