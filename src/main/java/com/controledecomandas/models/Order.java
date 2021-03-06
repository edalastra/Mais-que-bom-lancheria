package com.controledecomandas.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Order {
    private int id;
    private Bartable bartable;
    private Timestamp openAt;
    private Timestamp closeAt;
    private User worker;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bartable getBartable() {
        return bartable;
    }

    public void setBartable(Bartable bartable) {
        this.bartable = bartable;
    }

    public Timestamp getOpenAt() {
        return openAt;
    }

    public void setOpenAt(Timestamp openAt) {
        this.openAt = openAt;
    }

    public Timestamp getCloseAt() {
        return closeAt;
    }

    public void setCloseAt(Timestamp closeAt) {
        this.closeAt = closeAt;
    }

    public void setWorker(User worker) {
        this.worker = worker;
    }

    public String getWorker() {
        return worker.getFirstName() + " " + worker.getLastName();
    }

    @Override
    public String toString() {
        return "Comanda " + id + ", mesa " + bartable.getId() + ", aberta " + openAt;
    }
}
