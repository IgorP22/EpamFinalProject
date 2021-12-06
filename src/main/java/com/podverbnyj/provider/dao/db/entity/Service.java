package com.podverbnyj.provider.dao.db.entity;

import java.io.Serializable;

public class Service implements Serializable {
    private int id;
    private String titleRu;
    private String titleEn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitleRu() {
        return titleRu;
    }

    public void setTitleRu(String titleRu) {
        this.titleRu = titleRu;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }


    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", titleRu='" + titleRu + '\'' +
                ", titleEn='" + titleEn + '\'' +
                '}';
    }
}