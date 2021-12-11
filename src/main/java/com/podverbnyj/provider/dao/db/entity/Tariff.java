package com.podverbnyj.provider.dao.db.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class 'Tariff' for receiving database entity for table 'tariff'
 *
 * id - id field, autogenerated by database, unique
 * nameRu - name of tariff in Russian language to realize localization
 * nameEn - name of tariff in Russian language to realize localization
 * price - price of tariff for 30 day
 * serviceID - service to which this tariff belongs
 * descriptionRu - description of tariff in Russian language to realize localization
 * descriptionEn - description of tariff in English language to realize localization
 */
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tariff tariff = (Tariff) o;
        return id == tariff.id && Double.compare(tariff.price, price) == 0 && serviceId == tariff.serviceId && nameRu.equals(tariff.nameRu) && nameEn.equals(tariff.nameEn) && descriptionRu.equals(tariff.descriptionRu) && descriptionEn.equals(tariff.descriptionEn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameRu, nameEn, price, serviceId, descriptionRu, descriptionEn);
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
