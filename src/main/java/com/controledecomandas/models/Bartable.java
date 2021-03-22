package com.controledecomandas.models;

public class Bartable {
    private int id;
    private int capacity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Nº " + id +
                ", Capacidade " + capacity;
    }
}
