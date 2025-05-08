package com.romero.proyectofinalprueba.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.romero.proyectofinalprueba.R;
import com.romero.proyectofinalprueba.models.DAOEscudos;
import com.romero.proyectofinalprueba.models.Jugador;
import com.romero.proyectofinalprueba.models.MercadoAdapter;
import com.romero.proyectofinalprueba.models.ui.main.SharedViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FragmentMercado extends Fragment {

    private ListView lista;
    private ArrayList<Jugador> jugadoresOriginales;
    private ArrayList<Jugador> jugadores; // Lista que se muestra en el adaptador
    private MercadoAdapter adapter;
    private DAOEscudos daoEscudos;
    private SharedViewModel viewModel;
    private TextView saldoTextView;
    private int saldoActual;
    private CheckBox cbPorteros, cbDefensas, cbMediocentros, cbMediapuntas, cbDelanteros, cbFavoritos;

    public FragmentMercado(TextView saldoTextView) {
        this.saldoTextView = saldoTextView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mercado, container, false);

        SharedPreferences prefs = requireActivity().getSharedPreferences("configuraciones", Context.MODE_PRIVATE);
        float textoSizeSp = prefs.getFloat("texto_size_sp", 14f);

        lista = view.findViewById(R.id.listaMercado);
        cbPorteros = view.findViewById(R.id.checkBoxPortero);
        cbDefensas = view.findViewById(R.id.checkBoxDefensa);
        cbMediocentros = view.findViewById(R.id.checkBoxMediocentro);
        cbMediapuntas = view.findViewById(R.id.checkBoxMediapunta);
        cbDelanteros = view.findViewById(R.id.checkBoxDelantero);
        cbFavoritos = view.findViewById(R.id.checkBoxFavoritos);

        daoEscudos = new DAOEscudos();
        // Crea una copia de la lista original para filtrar sin modificar la fuente
        jugadoresOriginales = new ArrayList<>(daoEscudos.obtenerJugadores());
        jugadores = new ArrayList<>(jugadoresOriginales);
        adapter = new MercadoAdapter(getContext(), jugadores);
        adapter.setTextSize(textoSizeSp);
        lista.setAdapter(adapter);


        //CONVIERTE LA CADENA Y LO ALMACENA EN SALDO ACTUAL
        saldoActual = Integer.parseInt(saldoTextView.getText().toString().replace("Saldo: ", "").replace("M", "").trim());

        //OBTIENE EL VIEWMODEL ASOCIADO A LA ACTIVIDAD PARA QUE OTROS FRAGMENTOS ACCEDAN A LOS DATOS
        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        //REPRESENTA EL SALDO ACTUAL (DEVUELVE UN LIVEDATA<INTEGER>)
        viewModel.getSaldoActual().observe(getViewLifecycleOwner(), saldo -> {
            saldoTextView.setText("Saldo: " + saldo + "M");
        });

        cbPorteros.setOnClickListener(v -> filtrarJugadores());
        cbDefensas.setOnClickListener(v -> filtrarJugadores());
        cbMediocentros.setOnClickListener(v -> filtrarJugadores());
        cbMediapuntas.setOnClickListener(v -> filtrarJugadores());
        cbDelanteros.setOnClickListener(v -> filtrarJugadores());

        //HACER CLICK EN LA LISTA
        lista.setOnItemClickListener((parent, view1, position, id) -> {
            Jugador jugadorSeleccionado = jugadores.get(position);

            //PARA QUE NO SE REPITA UNA POSICIÓN QUE YA TIENES
            if (viewModel.getJugadoresComprados().getValue().containsKey(jugadorSeleccionado.getPosicion())) {
                new AlertDialog.Builder(view1.getContext())
                        .setTitle("Jugador ya comprado")
                        .setMessage("Ya tienes un " + jugadorSeleccionado.getPosicion() + " en tu equipo.")
                        .setPositiveButton("Aceptar", null)
                        .show();
                return;
            }

            //COMPRAR AL JUGADOR Y ACTUALIZAR EL SALDO
            new AlertDialog.Builder(view1.getContext())
                    .setTitle("¿Quieres comprar a " + jugadorSeleccionado.getNombre() + "?")
                    .setMessage("VALOR: " + jugadorSeleccionado.getMonedas() + "M" + "\n" +
                            "MEDIA: " + jugadorSeleccionado.getMedia() + "GRL" + "\n" +
                            "Posición: " + jugadorSeleccionado.getPosicion())
                    .setPositiveButton("Cerrar", null)
                    .setNegativeButton("Comprar", (dialog, which) -> {

                        Integer saldoActualizado = viewModel.getSaldoActual().getValue();
                        if (saldoActualizado >= jugadorSeleccionado.getMonedas()) {
                            int nuevoSaldo = saldoActualizado - jugadorSeleccionado.getMonedas();
                            viewModel.actualizarSaldo(nuevoSaldo);
                            viewModel.agregarJugador(jugadorSeleccionado.getPosicion(), jugadorSeleccionado);

                        } else {
                            new AlertDialog.Builder(view1.getContext())
                                    .setTitle("Saldo insuficiente")
                                    .setMessage("No tienes suficiente dinero para comprar a este jugador.")
                                    .setPositiveButton("Aceptar", null)
                                    .show();
                        }
                    }).show();
        });


        return view;
    }

    private void filtrarJugadores() {
        ArrayList<Jugador> jugadoresFiltrados = new ArrayList<>();

        // Detectar si se ha seleccionado algún filtro de posición, si se pulsa al menos 1 es "true"
        boolean filtroPosicion = cbPorteros.isChecked() || cbDefensas.isChecked() ||
                cbMediocentros.isChecked() || cbMediapuntas.isChecked() ||
                cbDelanteros.isChecked();

        // Recorrer la lista original para aplicar los filtros
        for (Jugador jugador : jugadoresOriginales) {
            // Si se ha marcado el filtro de favoritos y el jugador no es favorito, lo descartamos
            if (cbFavoritos.isChecked() && !jugador.isFavorito()) {
                continue;
            }

            // Si se seleccionó algún filtro de posición, verificar si el jugador cumple alguna condición
            if (filtroPosicion) {
                boolean cumplePosicion = false;
                if (cbPorteros.isChecked() && jugador.getPosicion().equalsIgnoreCase("portero")) {
                    cumplePosicion = true;
                }
                if (cbDefensas.isChecked() && jugador.getPosicion().equalsIgnoreCase("defensa")) {
                    cumplePosicion = true;
                }
                if (cbMediocentros.isChecked() && jugador.getPosicion().equalsIgnoreCase("mediocampista")) {
                    cumplePosicion = true;
                }
                if (cbMediapuntas.isChecked() && jugador.getPosicion().equalsIgnoreCase("mediapunta")) {
                    cumplePosicion = true;
                }
                if (cbDelanteros.isChecked() && jugador.getPosicion().equalsIgnoreCase("delantero")) {
                    cumplePosicion = true;
                }

                if (cumplePosicion) {
                    jugadoresFiltrados.add(jugador);
                }
            } else {
                // Si no hay filtro de posición, agregamos el jugador (ya sea que solo se filtre por favoritos o ninguno)
                jugadoresFiltrados.add(jugador);
            }
        }

        // Actualizar la lista que usa el adaptador
        jugadores.clear();
        jugadores.addAll(jugadoresFiltrados);
        adapter.notifyDataSetChanged();
    }

    public void setTextSize(float sizeSp) {
        adapter.setTextSize(sizeSp);
        adapter.notifyDataSetChanged();
    }




}
