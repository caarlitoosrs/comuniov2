package com.romero.proyectofinalprueba.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.romero.proyectofinalprueba.R;
import com.romero.proyectofinalprueba.models.Jugador;
import com.romero.proyectofinalprueba.models.MercadoAdapter;
import com.romero.proyectofinalprueba.models.ui.main.EquipoViewModel;
import com.romero.proyectofinalprueba.models.ui.main.SharedViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FragmentMercado extends Fragment {

    private ListView lista;
    private ArrayList<Jugador> jugadoresOriginales;
    private ArrayList<Jugador> jugadores;
    private MercadoAdapter adapter;
    private SharedViewModel viewModel;
    private TextView saldoTextView;
    private CheckBox cbPorteros, cbDefensas, cbMediocentros, cbMediapuntas, cbDelanteros, cbFavoritos;

    public FragmentMercado() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        EquipoViewModel equipoViewModel = new ViewModelProvider(requireActivity()).get(EquipoViewModel.class);
        ArrayList<Jugador> listaJugadores = equipoViewModel.getListaJugadores();

        jugadoresOriginales = new ArrayList<>(listaJugadores);
        jugadores = new ArrayList<>(jugadoresOriginales);

        adapter = new MercadoAdapter(getContext(), jugadores);
        adapter.setTextSize(textoSizeSp);
        lista.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        viewModel.getSaldoActual().observe(getViewLifecycleOwner(), saldo -> {
            if (saldoTextView != null) {
                saldoTextView.setText("Saldo: " + saldo + "M");
            }
        });

        viewModel.getFiltrosSeleccionados().observe(getViewLifecycleOwner(), filtros -> {
            cbPorteros.setChecked(filtros.contains("portero"));
            cbDefensas.setChecked(filtros.contains("defensa"));
            cbMediocentros.setChecked(filtros.contains("mediocampista"));
            cbMediapuntas.setChecked(filtros.contains("mediapunta"));
            cbDelanteros.setChecked(filtros.contains("delantero"));
            cbFavoritos.setChecked(filtros.contains("favorito"));
            filtrarJugadores();
        });

        View.OnClickListener filtroListener = v -> {
            filtrarJugadores();
            guardarFiltrosSeleccionadosEnViewModel();
        };

        cbPorteros.setOnClickListener(filtroListener);
        cbDefensas.setOnClickListener(filtroListener);
        cbMediocentros.setOnClickListener(filtroListener);
        cbMediapuntas.setOnClickListener(filtroListener);
        cbDelanteros.setOnClickListener(filtroListener);
        cbFavoritos.setOnClickListener(filtroListener);

        lista.setOnItemClickListener((parent, view1, position, id) -> {
            Jugador jugadorSeleccionado = jugadores.get(position);

            if (viewModel.getJugadoresComprados().getValue().containsKey(jugadorSeleccionado.getPosicion())) {
                new AlertDialog.Builder(view1.getContext())
                        .setTitle("Jugador ya comprado")
                        .setMessage("Ya tienes un " + jugadorSeleccionado.getPosicion() + " en tu equipo.")
                        .setPositiveButton("Aceptar", null)
                        .show();
                return;
            }

            new AlertDialog.Builder(view1.getContext())
                    .setTitle("¿Quieres comprar a " + jugadorSeleccionado.getNombre() + "?")
                    .setMessage("VALOR: " + jugadorSeleccionado.getMonedas() + "M\n" +
                            "MEDIA: " + jugadorSeleccionado.getMedia() + "GRL\n" +
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

        boolean filtroPosicion = cbPorteros.isChecked() || cbDefensas.isChecked() ||
                cbMediocentros.isChecked() || cbMediapuntas.isChecked() ||
                cbDelanteros.isChecked();

        for (Jugador jugador : jugadoresOriginales) {
            if (cbFavoritos.isChecked() && !jugador.isFavorito()) continue;

            boolean cumplePosicion = !filtroPosicion ||
                    (cbPorteros.isChecked() && jugador.getPosicion().equalsIgnoreCase("portero")) ||
                    (cbDefensas.isChecked() && jugador.getPosicion().equalsIgnoreCase("defensa")) ||
                    (cbMediocentros.isChecked() && jugador.getPosicion().equalsIgnoreCase("mediocampista")) ||
                    (cbMediapuntas.isChecked() && jugador.getPosicion().equalsIgnoreCase("mediapunta")) ||
                    (cbDelanteros.isChecked() && jugador.getPosicion().equalsIgnoreCase("delantero"));

            if (cumplePosicion) jugadoresFiltrados.add(jugador);
        }

        jugadores.clear();
        jugadores.addAll(jugadoresFiltrados);
        adapter.notifyDataSetChanged();
    }

    private void guardarFiltrosSeleccionadosEnViewModel() {
        Set<String> filtros = new HashSet<>();
        if (cbPorteros.isChecked()) filtros.add("portero");
        if (cbDefensas.isChecked()) filtros.add("defensa");
        if (cbMediocentros.isChecked()) filtros.add("mediocampista");
        if (cbMediapuntas.isChecked()) filtros.add("mediapunta");
        if (cbDelanteros.isChecked()) filtros.add("delantero");
        if (cbFavoritos.isChecked()) filtros.add("favorito");
        viewModel.setFiltrosSeleccionados(filtros);
    }

    public void setTextSize(float sizeSp) {
        SharedPreferences prefs = requireActivity().getSharedPreferences("configuraciones", Context.MODE_PRIVATE);
        prefs.edit().putFloat("texto_size_sp", sizeSp).apply();
        adapter.setTextSize(sizeSp);
        adapter.notifyDataSetChanged();
    }
}
