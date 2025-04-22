package com.romero.proyectofinalprueba.models;

public class Jugador {

    private int urlImg;
    private int monedas;
    private int media;
    private String posicion;
    private String nombre;
    private boolean favorito;

    public Jugador(int urlImg, int monedas, int media, String posicion, String nombre) {
        this.urlImg = urlImg;
        this.monedas = monedas;
        this.media = media;
        this.posicion = posicion;
        this.nombre = nombre;
        this.favorito=false;
    }

    public int getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(int urlImg) {
        this.urlImg = urlImg;
    }

    public int getMonedas() {
        return monedas;
    }

    public void setMonedas(int monedas) {
        this.monedas = monedas;
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

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
