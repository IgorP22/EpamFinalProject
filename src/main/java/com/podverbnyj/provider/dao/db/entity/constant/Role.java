package com.podverbnyj.provider.dao.db.entity.constant;

/**
 * Enum constant for field Role database user table
 */
public enum Role {
    ADMIN("admin"), USER("user");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}