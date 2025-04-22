package com.romero.proyectofinalprueba.models.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.romero.proyectofinalprueba.models.Equipo;

public class EquipoViewModel extends ViewModel {

    private final MutableLiveData<Equipo> equipoSeleccionado = new MutableLiveData<>();

    public void setEquipoSeleccionado(Equipo equipo) {
        equipoSeleccionado.setValue(equipo);
    }

    public LiveData<Equipo> getEquipoSeleccionado() {
        return equipoSeleccionado;
    }
}
