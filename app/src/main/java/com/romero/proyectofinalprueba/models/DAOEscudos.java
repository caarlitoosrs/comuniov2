package com.romero.proyectofinalprueba.models;

import com.romero.proyectofinalprueba.R;

import java.util.ArrayList;

public class DAOEscudos {

    private ArrayList<Equipo> equipos;
    private ArrayList<Jugador> jugador;

    public DAOEscudos() {
        this.equipos = new ArrayList<>();
        this.jugador = new ArrayList<>();
        cargarEquipos();
        cargarJugadores();
    }

    private void cargarEquipos() {
       equipos.add(new Equipo("Alcala", R.drawable.alcala, R.raw.alcala, R.raw.videoalcala));
        equipos.add(new Equipo("Arsenal", R.drawable.arsenal, R.raw.arsenal, R.raw.videoarsenal));
        equipos.add(new Equipo("Milan", R.drawable.milan, R.raw.milan, R.raw.videomilan));
        equipos.add(new Equipo("Real Madrid", R.drawable.realmadrid, R.raw.rm, R.raw.videorm));
        equipos.add(new Equipo("Roma", R.drawable.roma, R.raw.roma, R.raw.videoroma));
    }

    private void cargarJugadores(){
        /*PORTEROS*/
        jugador.add(new Jugador(R.drawable.aaron, 10, 82, "Portero", "Ramsdale"));
        jugador.add(new Jugador(R.drawable.allison, 40, 88, "Portero", "Allison"));
        jugador.add(new Jugador(R.drawable.curtois, 50, 90, "Portero", "Curtois"));
        jugador.add(new Jugador(R.drawable.donaruma, 18, 86, "Portero", "Gigi"));
        jugador.add(new Jugador(R.drawable.ederson, 21, 85, "Portero", "Ederson"));
        jugador.add(new Jugador(R.drawable.emiliano, 12, 82, "Portero", "Dibu"));
        jugador.add(new Jugador(R.drawable.maignan, 8, 81, "Portero", "Maignan"));
        jugador.add(new Jugador(R.drawable.oblak, 36, 89, "Portero", "Oblak"));
        jugador.add(new Jugador(R.drawable.onana, 14, 80, "Portero", "Onana"));
        jugador.add(new Jugador(R.drawable.terstegen, 40, 88, "Portero", "Ter Stegen"));

        /*DEFENSAS*/
        jugador.add(new Jugador(R.drawable.alaba, 16, 83, "Defensa", "Alaba"));
        jugador.add(new Jugador(R.drawable.trent, 33, 87, "Defensa", "Trent"));
        jugador.add(new Jugador(R.drawable.asencio, 11, 79, "Defensa", "Asencio"));
        jugador.add(new Jugador(R.drawable.cacncelo, 7, 77, "Defensa", "Cancelo"));
        jugador.add(new Jugador(R.drawable.davies, 23, 85, "Defensa", "Davies"));
        jugador.add(new Jugador(R.drawable.marqunhos, 15, 85, "Defensa", "Marquinhos"));
        jugador.add(new Jugador(R.drawable.militao, 18, 86, "Defensa", "Militao"));
        jugador.add(new Jugador(R.drawable.ruben, 21, 86, "Defensa", "Dias"));
        jugador.add(new Jugador(R.drawable.rudiger, 33, 88, "Defensa", "Rudiger"));
        jugador.add(new Jugador(R.drawable.theo, 29, 86, "Defensa", "Theo"));
        jugador.add(new Jugador(R.drawable.virgil, 35, 89, "Defensa", "Van Dijk"));

        /*CENTROCAMPISTAS*/
        jugador.add(new Jugador(R.drawable.bellingham, 45, 90, "Mediapunta", "Bellingham"));
        jugador.add(new Jugador(R.drawable.fernandes, 25, 84, "Mediapunta", "Bruno"));
        jugador.add(new Jugador(R.drawable.debruyne, 20, 88, "Mediapunta", "De Bruyne"));
        jugador.add(new Jugador(R.drawable.pedri, 47, 89, "Mediocampista", "Pedri"));
        jugador.add(new Jugador(R.drawable.kimmich, 36, 87, "Mediocampista", "Kimmich"));
        jugador.add(new Jugador(R.drawable.modric, 12, 85, "Mediocampista", "Modric"));
        jugador.add(new Jugador(R.drawable.odergard, 21, 86, "Mediapunta", "Odegaard"));
        jugador.add(new Jugador(R.drawable.rice, 32, 87, "Mediocampista", "Rice"));
        jugador.add(new Jugador(R.drawable.rodri, 50, 90, "Mediocampista", "Rodri"));
        jugador.add(new Jugador(R.drawable.valverde, 44, 89, "Mediocampista", "Valverde"));

        /*DELANTEROS*/
        jugador.add(new Jugador(R.drawable.benzema, 10, 83, "Delantero", "Benzema"));
        jugador.add(new Jugador(R.drawable.cr7, 18, 86, "Delantero", "Cristiano"));
        jugador.add(new Jugador(R.drawable.messi, 15, 84, "Delantero", "Messi"));
        jugador.add(new Jugador(R.drawable.kane, 26, 89, "Delantero", "Kane"));
        jugador.add(new Jugador(R.drawable.oshimen, 29, 85, "Delantero", "Oshimen"));
        jugador.add(new Jugador(R.drawable.mbappe, 70, 92, "Delantero", "Mbappe"));
        jugador.add(new Jugador(R.drawable.salah, 62, 90, "Delantero", "Salah"));
        jugador.add(new Jugador(R.drawable.vini, 68, 91, "Delantero", "Vini jr"));
        jugador.add(new Jugador(R.drawable.haaland, 70, 92, "Delantero", "Haaland"));
        jugador.add(new Jugador(R.drawable.lewandoski, 16, 86, "Delantero", "Lewandoski"));

    }

    public ArrayList<Equipo> obtenerEquipos() {
        return equipos;
    }

    public ArrayList<Jugador> obtenerJugadores() {
        return jugador;
    }

}
