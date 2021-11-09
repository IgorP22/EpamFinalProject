package com.podverbnyj.provider.DAO.db.entity.constant;

public enum Language {
    RU("ru"), EN("en");

    private final String value;
    Language (String value) {this.value=value;}

    public boolean equalsTo(String name) {
        return value.equals(name);
    }

    public String value() {
        return value;
    }
}
