package com.romero.proyectofinalprueba.models;

public class Equipo {
    private String nombre;
    private int imagenResId;
    private int cancionResId;
    private int videoResId;

    public Equipo(String nombre, int imagenResId, int cancionResId, int videoResId) {
        this.nombre = nombre;
        this.imagenResId = imagenResId;
        this.cancionResId = cancionResId;
        this.videoResId = videoResId;
    }

    public int getVideoResId() {
        return videoResId;
    }

    public void setVideoResId(int videoResId) {
        this.videoResId = videoResId;
    }

    public int getCancionResId() {
        return cancionResId;
    }

    public void setCancionResId(int cancionResId) {
        this.cancionResId = cancionResId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getImagenResId() {
        return imagenResId;
    }

    public void setImagenResId(int imagenResId) {
        this.imagenResId = imagenResId;
    }

    @Override
    public String toString() {
        return "Equipo{" +
                "nombre='" + nombre + '\'' +
                ", imagenResId=" + imagenResId +
                '}';
    }
}
