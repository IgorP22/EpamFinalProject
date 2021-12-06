package com.podverbnyj.provider.dao.db.entity;

import java.io.Serializable;

public class PasswordRecovery implements Serializable {
    private int userId;
    private String code;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "PasswordRestore{" +
                "userId=" + userId +
                ", code='" + code + '\'' +
                '}';
    }
}
