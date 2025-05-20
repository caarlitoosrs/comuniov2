package com.romero.proyectofinalprueba;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.romero.proyectofinalprueba.models.DAOEscudos;
import com.romero.proyectofinalprueba.models.Equipo;
import com.romero.proyectofinalprueba.models.Jugador;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private float tamanioTexto = 14f;
    private ImageView escudos, flechaAnt, flechaSig;
    private TextView nombreEsc;
    private ArrayList<Equipo> equipos;
    private int posicionActual = 0;
    private DAOEscudos daoEscudos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        escudos = findViewById(R.id.escudoPrincipal);
        flechaAnt = findViewById(R.id.flechaAnterior);
        flechaSig = findViewById(R.id.flechaSiguiente);
        nombreEsc = findViewById(R.id.nombreEscudo);

        daoEscudos = new DAOEscudos();
        equipos = daoEscudos.obtenerEquipos();

        // Obtener tamaño de texto desde SharedPreferences
        SharedPreferences prefs = getSharedPreferences("configuraciones", MODE_PRIVATE);
        float textoSizeSp = prefs.getFloat("texto_size_sp", 14f);
        nombreEsc.setTextSize(textoSizeSp);

        actualizarEquipo();

        // Llamamos a la tarea para obtener el JSON desde la URL utilizando Volley
        obtenerJSONConVolley("https://raw.githubusercontent.com/caarlitoosrs/recursos/refs/heads/main/recursos.json");

        // Funcionalidad al hacer clic en el escudo y las flechas
        flechaAnt.setOnClickListener(v -> anteriorEquipo());
        flechaSig.setOnClickListener(v -> siguienteEquipo());

        escudos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivityFragments.class);
            Equipo equipoActual = equipos.get(posicionActual);
            intent.putExtra("imgEquipo", equipoActual.getImagenResId());
            intent.putExtra("teamName", equipoActual.getNombre());
            startActivity(intent);
        });
    }

    // Menú
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemCerrar) {
            finishAffinity();
        } else if (item.getItemId() == R.id.itemTexto) {
            mostarTamanioDialogo();
        }
        return super.onOptionsItemSelected(item);
    }

    // Funciones para cambiar equipo
    private void siguienteEquipo() {
        posicionActual = (posicionActual + 1) % equipos.size();
        actualizarEquipo();
    }

    private void anteriorEquipo() {
        if (posicionActual == 0) {
            posicionActual = equipos.size();
        }
        posicionActual--;
        actualizarEquipo();
    }

    private void actualizarEquipo() {
        Equipo equipoActual = equipos.get(posicionActual);
        escudos.setImageResource(equipoActual.getImagenResId());
        nombreEsc.setText(equipoActual.getNombre());
    }

    // Métodos para el tamaño de las letras
    private void mostarTamanioDialogo() {
        String[] opciones = {"+", "-"};
        new AlertDialog.Builder(this).setTitle("Tamaño de la fuente").setItems(opciones, ((dialog, which) -> {
            if (which == 0) {
                tamanioTexto += 2f;
            } else {
                tamanioTexto = Math.max(10f, tamanioTexto - 2f);
            }

            SharedPreferences prefs = getSharedPreferences("configuraciones", MODE_PRIVATE);
            prefs.edit().putFloat("texto_size_sp", tamanioTexto).apply();

            aplicarTamanioTexto();
        })).show();
    }

    private void aplicarTamanioTexto() {
        nombreEsc.setTextSize(tamanioTexto);
    }

    // Función para obtener el JSON desde la URL utilizando Volley
    private void obtenerJSONConVolley(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    deserializarJSON(response.toString());
                },
                error -> {
                    Log.e("Volley Error", error.toString());
                });

        requestQueue.add(jsonObjectRequest);
    }

    // Método para deserializar el JSON a objetos de Jugador y Equipo
    private void deserializarJSON(String json) {
        Gson gson = new Gson();

        // Deserializamos el JSON en listas de jugadores y equipos
        EquipoResponse response = gson.fromJson(json, EquipoResponse.class);


    }

    public class EquipoResponse {
        private ArrayList<Equipo> equipos;
        private ArrayList<Jugador> jugadores;

        public ArrayList<Equipo> getEquipos() {
            return equipos;
        }

        public ArrayList<Jugador> getJugadores() {
            return jugadores;
        }
    }
}