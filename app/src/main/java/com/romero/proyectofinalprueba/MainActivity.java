package com.romero.proyectofinalprueba;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.romero.proyectofinalprueba.fragments.FragmentEquipo;
import com.romero.proyectofinalprueba.fragments.FragmentMercado;
import com.romero.proyectofinalprueba.fragments.FragmentPlantilla;
import com.romero.proyectofinalprueba.models.DAOEscudos;
import com.romero.proyectofinalprueba.models.Equipo;

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

        escudos=findViewById(R.id.escudoPrincipal);
        flechaAnt=findViewById(R.id.flechaAnterior);
        flechaSig=findViewById(R.id.flechaSiguiente);
        nombreEsc=findViewById(R.id.nombreEscudo);

        daoEscudos = new DAOEscudos();
        equipos = daoEscudos.obtenerEquipos();

        SharedPreferences prefs = getSharedPreferences("configuraciones", MODE_PRIVATE);
        float textoSizeSp = prefs.getFloat("texto_size_sp", 14f);
        nombreEsc.setTextSize(textoSizeSp);

        actualizarEquipo();

        //----------FUNCIONALIDAD AL HACER CLICK EN EL ESCUDO Y LAS FLECHAS-----------
        flechaAnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anteriorEquipo();
            }
        });

        flechaSig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siguienteEquipo();
            }
        });

        escudos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivityFragments.class);
                Equipo equipoActual = equipos.get(posicionActual);
                intent.putExtra("imgEquipo", equipoActual.getImagenResId());
                intent.putExtra("teamName", equipoActual.getNombre());
                startActivity(intent);
            }
        });

    }

    //--------------MENÚ----------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.itemCerrar){
            finishAffinity();
        }else if(item.getItemId()==R.id.itemTexto){
            mostarTamanioDialogo();
        }
        return super.onOptionsItemSelected(item);
    }

    //--------FUNCIONES DE CÓDIGO---------
    private void siguienteEquipo(){
        posicionActual = (posicionActual + 1) % equipos.size();
        actualizarEquipo();
    }

    private void anteriorEquipo(){
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

    //---------METODOS PARA EL TAMAÑO DE LAS LETRAS

    private void mostarTamanioDialogo() {
        String[] opciones = {"+", "-"};
        new AlertDialog.Builder(this).setTitle("Tamaño de la fuente").setItems(opciones, ((dialog, which) -> {
            // Aquí ajustamos el tamaño del texto y lo guardamos
            if (which == 0) {
                tamanioTexto += 2f;
            } else {
                tamanioTexto = Math.max(10f, tamanioTexto - 2f);
            }

            // Guardar en SharedPreferences el tamaño
            SharedPreferences prefs = getSharedPreferences("configuraciones", MODE_PRIVATE);
            prefs.edit().putFloat("texto_size_sp", tamanioTexto).apply();

            // Aplicar el nuevo tamaño al TextView (nombreEsc)
            aplicarTamanioTexto();
        })).show();
    }

    private void aplicarTamanioTexto() {
        nombreEsc.setTextSize(tamanioTexto);  // Aplicar el tamaño de texto al TextView
    }


    }


