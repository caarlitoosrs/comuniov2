package com.romero.proyectofinalprueba;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
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

public class MainActivityFragments extends AppCompatActivity {

    private float tamanioTexto = 14f;
    private ImageView imagen, btnMercado, btnPlantilla, btnEquipo;
    private TextView nombreEquipo, tvSaldo;
    private int saldo = 200;
    DAOEscudos daoEscudos;
    EquipoViewModel equipoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Recuperar tamaño guardado
        SharedPreferences prefs = getSharedPreferences("configuraciones", MODE_PRIVATE);
        tamanioTexto = prefs.getFloat("texto_tamanio_sp", 18f);

        setContentView(R.layout.activity_main_fragments);

        btnEquipo = findViewById(R.id.btnEquipo);
        btnMercado = findViewById(R.id.btnMercado);
        btnPlantilla = findViewById(R.id.btnPlantilla);
        imagen = findViewById(R.id.imgEquipoEscogido);
        nombreEquipo = findViewById(R.id.nombreEquipoEscogido);
        tvSaldo = findViewById(R.id.saldoInicial);

        daoEscudos = new DAOEscudos();
        equipoViewModel = new ViewModelProvider(this).get(EquipoViewModel.class);

        tvSaldo.setText("Saldo: " + saldo + "M");
        int img = getIntent().getIntExtra("imgEquipo", 0);
        String nombre = getIntent().getStringExtra("teamName");

        imagen.setImageResource(img);
        nombreEquipo.setText(nombre);

        Equipo seleccionado = null;
        for (Equipo e : daoEscudos.obtenerEquipos()) {
            if (e.getNombre().equalsIgnoreCase(nombre)) {
                seleccionado = e;
                break;
            }
        }

        if (seleccionado != null) {
            // 4) ¡Este es el paso clave!
            //    Alimentas el LiveData del ViewModel con el Equipo elegido
            equipoViewModel.setEquipoSeleccionado(seleccionado);
        }

        btnEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imagen.setVisibility(View.GONE);
                nombreEquipo.setVisibility(View.GONE);
                tvSaldo.setVisibility(View.GONE);

                getSupportFragmentManager().beginTransaction().replace(R.id.container_bottom, new FragmentEquipo()).commit();
            }
        });


        btnMercado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagen.setVisibility(View.GONE);
                nombreEquipo.setVisibility(View.GONE);
                tvSaldo.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_bottom, new FragmentMercado(tvSaldo)).commit();
            }
        });

        btnPlantilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagen.setVisibility(View.GONE);
                nombreEquipo.setVisibility(View.GONE);
                tvSaldo.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_bottom, new FragmentPlantilla()).commit();

            }
        });

        aplicarTamanioTextos();


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
            //Aqui tenemos que elegir el tamaño de las fuentes, color, etc.
            mostarTamanioDialogo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //---------METODOS PARA EL TAMAÑO DE LAS LETRAS

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
        // Actividad
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
                } else if (fragment instanceof FragmentEquipo) {
                    ((FragmentEquipo) fragment).setTextSize(tamanioTexto);
                }
            }
        }
    }


}