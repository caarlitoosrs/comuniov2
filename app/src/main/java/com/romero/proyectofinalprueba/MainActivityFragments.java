package com.romero.proyectofinalprueba;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.romero.proyectofinalprueba.fragments.FragmentEquipo;
import com.romero.proyectofinalprueba.fragments.FragmentMercado;
import com.romero.proyectofinalprueba.fragments.FragmentPlantilla;
import com.romero.proyectofinalprueba.models.DAOEscudos;
import com.romero.proyectofinalprueba.models.Equipo;
import com.romero.proyectofinalprueba.models.ui.main.EquipoViewModel;
import com.romero.proyectofinalprueba.models.ui.main.SharedViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivityFragments extends AppCompatActivity {

    private float tamanioTexto = 14f;
    private ImageView imagen, btnMercado, btnPlantilla, btnEquipo;
    private TextView nombreEquipo, tvSaldo;
    private int saldo = 200;
    DAOEscudos daoEscudos;
    EquipoViewModel equipoViewModel;
    SharedViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("configuraciones", MODE_PRIVATE);
        tamanioTexto = prefs.getFloat("texto_tamanio_sp", 18f);

        setContentView(R.layout.activity_main_fragments);

        btnEquipo = findViewById(R.id.btnEquipo);
        btnMercado = findViewById(R.id.btnMercado);
        btnPlantilla = findViewById(R.id.btnPlantilla);
        imagen = findViewById(R.id.imgEquipoEscogido);
        nombreEquipo = findViewById(R.id.nombreEquipoEscogido);
        tvSaldo = findViewById(R.id.saldoInicial);

        equipoViewModel = new ViewModelProvider(this).get(EquipoViewModel.class);

        int img = getIntent().getIntExtra("imgEquipo", 0);
        String nombre = getIntent().getStringExtra("teamName");

        daoEscudos = new DAOEscudos(this);
        daoEscudos.cargarDatosDesdeGitHub(this, () -> {
            ArrayList<Equipo> equipos = daoEscudos.obtenerEquipos();

            // Buscar equipo por nombre
            Equipo seleccionado = null;
            for (Equipo e : equipos) {
                if (e.getNombre().equalsIgnoreCase(nombre)) {
                    seleccionado = e;
                    break;
                }
            }

            if (seleccionado != null) {
                equipoViewModel.setEquipoSeleccionado(seleccionado);
                equipoViewModel.setListaJugadores(daoEscudos.obtenerJugadores());
                nombreEquipo.setText(seleccionado.getNombre());

                int resId = getResources().getIdentifier(seleccionado.getImagenResId(), "drawable", getPackageName());
                if (resId != 0) {
                    Picasso.get().load(resId).into(imagen);
                } else {
                    Log.e("MainActivityFragments", "Imagen no encontrada: " + seleccionado.getImagenResId());
                    imagen.setImageResource(R.drawable.baseline_favorite_border_24);
                }
            }


            equipoViewModel = new ViewModelProvider(this).get(EquipoViewModel.class);
            viewModel = new ViewModelProvider(this).get(SharedViewModel.class); //



            tvSaldo.setText("Saldo: " + saldo + "M");

            viewModel.getSaldoActual().observe(this, saldo -> {
                tvSaldo.setText("Saldo: " + saldo + "M");
            });



            // Botones fragmentos
            btnEquipo.setOnClickListener(v -> {
                imagen.setVisibility(View.GONE);
                nombreEquipo.setVisibility(View.GONE);
                tvSaldo.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_bottom, new FragmentEquipo()).commit();
            });

            btnMercado.setOnClickListener(v -> {
                imagen.setVisibility(View.GONE);
                nombreEquipo.setVisibility(View.GONE);
                tvSaldo.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_bottom, new FragmentMercado()).commit();
            });

            btnPlantilla.setOnClickListener(v -> {
                imagen.setVisibility(View.GONE);
                nombreEquipo.setVisibility(View.GONE);
                tvSaldo.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_bottom, new FragmentPlantilla()).commit();
            });

            aplicarTamanioTextos();
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
        if (item.getItemId() == R.id.itemCerrar) {
            finishAffinity();
        } else if (item.getItemId() == R.id.itemTexto) {
            mostarTamanioDialogo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //---------METODOS PARA EL TAMAÑO DE LAS LETRAS-------------

    private void mostarTamanioDialogo() {
        String[] opciones = {"+", "-"};
        new AlertDialog.Builder(this).setTitle("Tamaño de la fuente").setItems(opciones, ((dialog, which) -> {
            if (which == 0) {
                tamanioTexto += 2f;
            } else {
                tamanioTexto = Math.max(10f, tamanioTexto - 2f);
            }
            aplicarTamanioTextos();

            //Guardamos en prefs el nuevo valor
            SharedPreferences prefs = getSharedPreferences("configuraciones", MODE_PRIVATE);
            prefs.edit().putFloat("texto_tamanio_sp", tamanioTexto).apply();
        })).show();
    }


    private void aplicarTamanioTextos() {
        nombreEquipo.setTextSize(tamanioTexto);
        tvSaldo.setTextSize(tamanioTexto);

        // Obtener todos los fragmentos en el FragmentManager
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null) {
                // Aplicar tamaño de texto si el fragmento tiene el método setTextSize
                if (fragment instanceof FragmentMercado) {
                    ((FragmentMercado) fragment).setTextSize(tamanioTexto);
                } else if (fragment instanceof FragmentPlantilla) {
                    ((FragmentPlantilla) fragment).setTextSize(tamanioTexto);
                }
            }
        }
    }


}