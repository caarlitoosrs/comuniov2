package com.romero.proyectofinalprueba.models;

import java.util.ArrayList;

public class EquipoResponse {

    private ArrayList<Equipo> equipos;
    private ArrayList<Jugador> jugadores;

    public ArrayList<Equipo> getEquipos() {
        return equipos;
    }

    public void setEquipos(ArrayList<Equipo> equipos) {
        this.equipos = equipos;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }
}
