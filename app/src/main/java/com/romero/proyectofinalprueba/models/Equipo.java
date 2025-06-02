package com.romero.proyectofinalprueba.models;

public class Equipo {
    private String nombre;
    private String imagenResId;
    private int cancionResId;
    private int videoResId;

    public Equipo(String nombre, String imagenResId, int cancionResId, int videoResId) {
        this.nombre = nombre;
        this.imagenResId = imagenResId;
        this.cancionResId = cancionResId;
        this.videoResId = videoResId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagenResId() {
        return imagenResId;
    }

    public void setImagenResId(String imagenResId) {
        this.imagenResId = imagenResId;
    }

    public int getCancionResId() {
        return cancionResId;
    }

    public void setCancionResId(int cancionResId) {
        this.cancionResId = cancionResId;
    }

    public int getVideoResId() {
        return videoResId;
    }

    public void setVideoResId(int videoResId) {
        this.videoResId = videoResId;
    }
}
