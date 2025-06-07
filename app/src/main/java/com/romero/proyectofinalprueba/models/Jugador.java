package com.romero.proyectofinalprueba.models;

public class Jugador {

    private String urlImg;
    private int monedas;
    private int media;
    private String posicion;
    private String nombre;
    private boolean favorito;

    public Jugador(String urlImg, int monedas, int media, String posicion, String nombre) {
        this.urlImg = urlImg;
        this.monedas = monedas;
        this.media = media;
        this.posicion = posicion;
        this.nombre = nombre;
        this.favorito=false;
    }

    public String getUrlImg() {
        return urlImg;
    }


    public int getMonedas() {
        return monedas;
    }


    public int getMedia() {
        return media;
    }

    public void setMedia(int media) {
        this.media = media;
    }

    public String getPosicion() {
        return posicion;
    }


    public String getNombre() {
        return nombre;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "urlImg=" + urlImg +
                ", monedas=" + monedas +
                ", media=" + media +
                ", posicion='" + posicion + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
