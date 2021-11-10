package com.podverbnyj.provider.DAO.db.entity;

import java.io.Serializable;

public class Tariff implements Serializable {
    private int id;
    private String nameRu;
    private String nameEn;
    private double price;
    private int service_id;

    public Tariff(String nameRu, String nameEn, double price) {
        this.nameRu = nameRu;
        this.nameEn = nameEn;
        this.price = price;
    }

    public Tariff (){}



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }



    @Override
    public String toString() {
        return "Tariff{" +
                "id=" + id +
                ", nameRu='" + nameRu + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", price=" + price +
                ", service_id=" + service_id +
                '}';
    }
}
