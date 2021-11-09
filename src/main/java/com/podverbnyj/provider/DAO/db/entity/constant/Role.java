package com.podverbnyj.provider.DAO.db.entity.constant;

public enum Role {
    ADMIN("admin"), USER("user");

    private final String value;
    Role (String value) {this.value=value;}

    public boolean equalsTo(String name) {
        return value.equals(name);
    }

    public String value() {
        return value;
    }
}