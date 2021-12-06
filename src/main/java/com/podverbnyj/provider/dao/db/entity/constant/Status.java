package com.podverbnyj.provider.dao.db.entity.constant;

public enum Status {
    ACTIVE("active"), BLOCKED("blocked");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public boolean equalsTo(String name) {
        return value.equals(name);
    }

    public String value() {
        return value;
    }
}