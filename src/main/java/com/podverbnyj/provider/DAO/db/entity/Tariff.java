package com.podverbnyj.provider.DAO.db.entity;

import java.io.Serializable;

public class Tariff implements Serializable {
    private int id;
    private String nameRu;
    private String nameEn;
    private double price;
    private int group_id;

    public Tariff(String nameRu, String nameEn, double price) {
        this.nameRu = nameRu;
        this.nameEn = nameEn;
        this.price = price;
    }

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

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }



    @Override
    public String toString() {
        return "Tariff{" +
                "id=" + id +
                ", nameRu='" + nameRu + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", price=" + price +
                ", group_id=" + group_id +
                '}';
    }
}
