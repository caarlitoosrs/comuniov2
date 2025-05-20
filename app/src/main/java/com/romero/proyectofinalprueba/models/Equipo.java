package com.romero.proyectofinalprueba.models;

public class Equipo {
    private String nombre;
    private String imagenResId;  // Cambiar de String a int
    private int cancionResId; // Cambiar de String a int
    private int videoResId;   // Cambiar de String a int

    public Equipo(String nombre, String imagenResId, int cancionResId, int videoResId) {
        this.nombre = nombre;
        this.imagenResId = imagenResId;
        this.cancionResId = cancionResId;
        this.videoResId = videoResId;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagenResId() {
        return imagenResId;
    }

    public void setImagenResId(String imagenResId) {  // Cambiar el tipo a int
        this.imagenResId = imagenResId;
    }

    public int getCancionResId() {
        return cancionResId;
    }

    public void setCancionResId(int cancionResId) {  // Cambiar el tipo a int
        this.cancionResId = cancionResId;
    }

    public int getVideoResId() {
        return videoResId;
    }

    public void setVideoResId(int videoResId) {  // Cambiar el tipo a int
        this.videoResId = videoResId;
    }
}
