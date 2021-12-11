package com.podverbnyj.provider.dao.db.entity.constant;

/**
 * Enum constant for field Status database user table
 */
public enum Status {
    ACTIVE("active"), BLOCKED("blocked");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}