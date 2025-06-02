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
import com.squareup.picasso.Picasso;

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

        // Obtener tamaño de texto desde SharedPreferences
        SharedPreferences prefs = getSharedPreferences("configuraciones", MODE_PRIVATE);
        float textoSizeSp = prefs.getFloat("texto_size_sp", 14f);
        nombreEsc.setTextSize(textoSizeSp);

        daoEscudos = new DAOEscudos(this);

        // CARGA REMOTA DE DATOS DESDE GITHUB
        daoEscudos.cargarDatosDesdeGitHub(this, () -> {
            equipos = daoEscudos.obtenerEquipos();

            if (equipos != null && !equipos.isEmpty()) {
                actualizarEquipo();


                flechaAnt.setOnClickListener(v -> anteriorEquipo());
                flechaSig.setOnClickListener(v -> siguienteEquipo());

                escudos.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, MainActivityFragments.class);
                    Equipo equipoActual = equipos.get(posicionActual);
                    intent.putExtra("imgEquipo", equipoActual.getImagenResId());
                    intent.putExtra("teamName", equipoActual.getNombre());
                    startActivity(intent);
                });

            } else {
                Log.e("MainActivity", "La lista de equipos está vacía.");
            }
        });
    }


    // ------MENU-------
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

    // Cargar la imagen del escudo con Picasso
    private void actualizarEquipo() {
        Equipo equipoActual = equipos.get(posicionActual);

        // Convertir el nombre del recurso String a ID int
        int resId = getResources().getIdentifier(
                equipoActual.getImagenResId(),
                "drawable",
                getPackageName()
        );

        Picasso.get().load(resId).into(escudos);

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


}