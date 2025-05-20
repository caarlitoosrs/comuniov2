package com.romero.proyectofinalprueba.models;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.romero.proyectofinalprueba.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DAOEscudos {

    private ArrayList<Equipo> equipos;
    private ArrayList<Jugador> jugador;

    public DAOEscudos(Context context) {
        this.equipos = new ArrayList<>();
        this.jugador = new ArrayList<>();
        cargarEquipos(context);
        cargarJugadores(context);
    }

    private void cargarJugadores(Context context) {
        try {
            String json = leerJSONDesdeRaw(context, "jugadores");  // Cambié este método para usar "raw"
            if (json != null) {
                Log.d("DAOEscudos", "JSON de jugadores cargado correctamente");
                Gson gson = new Gson();
                Type tipoLista = new TypeToken<ArrayList<Jugador>>() {
                }.getType();
                jugador = gson.fromJson(json, tipoLista);
                if (jugador == null) {
                    Log.e("DAOEscudos", "Error al deserializar jugadores, la lista es null.");
                    jugador = new ArrayList<>(); // Inicializa la lista vacía
                }
            } else {
                Log.e("DAOEscudos", "No se pudo cargar el JSON de jugadores.");
            }
        } catch (Exception e) {
            Log.e("DAOEscudos", "Error al cargar jugadores", e);
        }
    }

    private void cargarEquipos(Context context) {
        try {
            String json = leerJSONDesdeRaw(context, "equipos");  // Cambié este método para usar "raw"
            if (json != null) {
                Log.d("DAOEscudos", "JSON de equipos cargado correctamente");
                Gson gson = new Gson();
                Type tipoLista = new TypeToken<ArrayList<EquipoJSON>>() {
                }.getType();
                List<EquipoJSON> lista = gson.fromJson(json, tipoLista);

                if (lista != null) {
                    for (EquipoJSON e : lista) {
                        int cancion = getResId(context, e.cancionResId, "raw");
                        int video = getResId(context, e.videoResId, "raw");
                        equipos.add(new Equipo(e.nombre, e.imagenResId, cancion, video));
                    }
                    Log.d("DAOEscudos", "Equipos cargados correctamente: " + equipos.size() + " equipos.");
                } else {
                    Log.e("DAOEscudos", "Error al deserializar los equipos, la lista es null.");
                    equipos = new ArrayList<>(); // Inicializa la lista vacía
                }
            } else {
                Log.e("DAOEscudos", "No se pudo cargar el JSON de equipos.");
            }
        } catch (Exception e) {
            Log.e("DAOEscudos", "Error al cargar equipos", e);
        }
    }

    private String leerJSONDesdeRaw(Context context, String archivo) {
        try {
            // Usamos openRawResource para leer los archivos de res/raw
            InputStream is = context.getResources().openRawResource(
                    context.getResources().getIdentifier(archivo, "raw", context.getPackageName()));
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Log.e("DAOEscudos", "Error al leer el archivo JSON desde raw: " + archivo, ex);
            return null;
        }
    }

    private int getResId(Context context, String name, String tipo) {
        int resId = context.getResources().getIdentifier(name, tipo, context.getPackageName());
        if (resId == 0) {
            Log.e("DAOEscudos", "No se encontró el recurso con el nombre: " + name);
        }
        return resId;
    }

    public ArrayList<Equipo> obtenerEquipos() {
        return equipos;
    }

    public ArrayList<Jugador> obtenerJugadores() {
        return jugador;
    }
}
