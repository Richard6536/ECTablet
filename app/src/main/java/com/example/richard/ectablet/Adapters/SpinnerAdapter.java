package com.example.richard.ectablet.Adapters;

public class SpinnerAdapter {
    int id;
    String nombre;
    String patente;

    public SpinnerAdapter(int id, String nombre, String patente) {
        this.id = id;
        this.nombre = nombre;
        this.patente = patente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getPatente() {
        return patente;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
