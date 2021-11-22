package com.podverbnyj.provider.DAO.db.entity;

import java.io.Serializable;
import java.util.Date;

public class UserPayment implements Serializable {
    private int userId;
    private Date date;
    private double sum;

    public UserPayment(int userId, Date date, double sum) {
        this.userId = userId;
        this.date = date;
        this.sum = sum;
    }

    public UserPayment(int userId, double sum) {
        this.userId = userId;
        this.sum = sum;
    }

    public UserPayment(){}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "UserPayments{" +
                "userId=" + userId +
                ", date='" + date + '\'' +
                ", sum=" + sum +
                '}';
    }
}
