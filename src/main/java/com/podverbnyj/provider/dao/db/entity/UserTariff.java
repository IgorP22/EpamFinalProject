package com.podverbnyj.provider.dao.db.entity;

import java.io.Serializable;
import java.util.Objects;

public class UserTariff implements Serializable {
    private int userId;
    private int tariffId;

    public UserTariff(int userId, int tariffId) {
        this.userId = userId;
        this.tariffId = tariffId;
    }

    public UserTariff() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTariffId() {
        return tariffId;
    }

    public void setTariffId(int tariffId) {
        this.tariffId = tariffId;
    }

    @Override
    public String toString() {
        return "UserTariffs{" +
                "userId=" + userId +
                ", tariffId=" + tariffId +
                '}';
    }
}
