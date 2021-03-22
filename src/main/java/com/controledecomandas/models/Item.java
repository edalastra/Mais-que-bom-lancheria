package com.controledecomandas.models;

import java.math.BigDecimal;

public class Item {
    private int id;
    private BigDecimal price;
    private BigDecimal cost;
    private String category;
    private String description;
    private String maker;
    private int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return description + ", " + maker + " - Preço RS" + price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
