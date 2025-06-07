package com.romero.proyectofinalprueba.models;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class DAOEscudos {

    private ArrayList<Equipo> equipos;
    private ArrayList<Jugador> jugador;

    public DAOEscudos(Context context) {
        this.equipos = new ArrayList<>();
        this.jugador = new ArrayList<>();
    }

    // Método para iniciar la carga remota del JSON
    public void cargarDatosDesdeGitHub(Context context, Runnable onFinish) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://raw.githubusercontent.com/caarlitoosrs/recursos/refs/heads/main/recursos.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        Gson gson = new Gson();

                        JSONArray equiposArray = response.getJSONArray("equipos");
                        Type tipoListaEquipos = new TypeToken<ArrayList<EquipoJSON>>() {
                        }.getType();
                        List<EquipoJSON> listaEquiposJson = gson.fromJson(equiposArray.toString(), tipoListaEquipos);
                        equipos.clear();
                        for (EquipoJSON e : listaEquiposJson) {
                            int cancion = getResId(context, e.cancionResId, "raw");
                            int video = getResId(context, e.videoResId, "raw");
                            equipos.add(new Equipo(e.nombre, e.imagenResId, cancion, video));
                        }


                        JSONArray jugadoresArray = response.getJSONArray("jugadores");
                        Type tipoListaJugadores = new TypeToken<ArrayList<Jugador>>() {
                        }.getType();
                        jugador = gson.fromJson(jugadoresArray.toString(), tipoListaJugadores);
                        if (jugador == null) {
                            jugador = new ArrayList<>();
                        }

                        if (onFinish != null) onFinish.run();

                    } catch (Exception e) {
                    }
                },
                error -> Log.e("DAOEscudos", "Error al descargar JSON desde GitHub", error)
        );

        queue.add(jsonObjectRequest);
    }

    // Método para obtener el ID de recurso a partir de su nombre
    private int getResId(Context context, String name, String tipo) {
        int resId = context.getResources().getIdentifier(name, tipo, context.getPackageName());
        return resId;
    }

    public ArrayList<Equipo> obtenerEquipos() {
        return equipos;
    }

    public ArrayList<Jugador> obtenerJugadores() {
        return jugador;
    }
}
