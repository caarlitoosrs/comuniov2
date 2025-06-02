package com.romero.proyectofinalprueba.models.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.romero.proyectofinalprueba.models.Jugador;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Integer> saldoActual = new MutableLiveData<>(200);
    private MutableLiveData<Integer> valorTotalEquipo = new MutableLiveData<>(0);
    private final MutableLiveData<HashMap<String, Jugador>> jugadoresComprados = new MutableLiveData<>(new HashMap<>());
    private final MutableLiveData<Set<String>> filtrosSeleccionados = new MutableLiveData<>(new HashSet<>());


    public LiveData<Integer> getSaldoActual() {
        return saldoActual;
    }

    public LiveData<Integer> getValorTotalEquipo() {
        return valorTotalEquipo;
    }

    public LiveData<HashMap<String, Jugador>> getJugadoresComprados() {
        return jugadoresComprados;
    }

    //Actualiza el saldo despues de comprar
    public void actualizarSaldo(int nuevoSaldo) {
        saldoActual.setValue(nuevoSaldo);
    }

    //Agrega jugador a la plantilla
    public void agregarJugador(String posicion, Jugador jugador) {
        HashMap<String, Jugador> jugadores = jugadoresComprados.getValue();
        if (jugadores != null && !jugadores.containsKey(posicion)) {
            jugadores.put(posicion, jugador);
            jugadoresComprados.setValue(jugadores);

            // Sumar el valor del jugador al total del equipo
            int nuevoValorTotal = valorTotalEquipo.getValue() + jugador.getMonedas();
            valorTotalEquipo.setValue(nuevoValorTotal);
        }
    }

    public LiveData<Set<String>> getFiltrosSeleccionados() {
        return filtrosSeleccionados;
    }

    public void setFiltrosSeleccionados(Set<String> filtros) {
        filtrosSeleccionados.setValue(filtros);
    }
}