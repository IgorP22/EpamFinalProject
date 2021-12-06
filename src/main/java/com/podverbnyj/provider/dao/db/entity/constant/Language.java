package com.podverbnyj.provider.dao.db.entity.constant;

public enum Language {
    RU("ru"), EN("en");

    private final String value;

    Language(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
