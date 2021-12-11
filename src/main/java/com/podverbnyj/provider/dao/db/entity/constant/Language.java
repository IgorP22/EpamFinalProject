package com.podverbnyj.provider.dao.db.entity.constant;

/**
 * Enum constant for field Language database user table
 */
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
