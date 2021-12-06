package com.podverbnyj.provider.dao.db.entity;

import java.io.Serializable;

public class Tariff implements Serializable {
    private int id;
    private String nameRu;
    private String nameEn;
    private double price;
    private int serviceId;
    private String descriptionRu;
    private String descriptionEn;


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

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getDescriptionRu() {
        return descriptionRu;
    }

    public void setDescriptionRu(String descriptionRu) {
        this.descriptionRu = descriptionRu;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    @Override
    public String toString() {
        return "Tariff{" +
                "id=" + id +
                ", nameRu='" + nameRu + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", price=" + price +
                ", serviceId=" + serviceId +
                ", descriptionRu='" + descriptionRu + '\'' +
                ", descriptionEn='" + descriptionEn + '\'' +
                '}';
    }
}