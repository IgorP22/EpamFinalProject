package com.podverbnyj.provider.DAO.db.entity;


import com.podverbnyj.provider.DAO.db.entity.constant.Language;
import com.podverbnyj.provider.DAO.db.entity.constant.Role;
import com.podverbnyj.provider.DAO.db.entity.constant.Status;

import java.io.Serializable;
import java.util.Objects;

import static com.podverbnyj.provider.DAO.db.entity.constant.Language.*;
import static com.podverbnyj.provider.DAO.db.entity.constant.Role.*;
import static com.podverbnyj.provider.DAO.db.entity.constant.Status.*;
import static com.podverbnyj.provider.utils.HashPassword.securePassword;


public class User implements Serializable {

    private int id;
    private String login;
    private String password;
    private String email;
    private String name;
    private String surname;
    private String phone;
    private double balance;
    private Language language;
    private Role role;
    private boolean notification;
    private Status status;

    private User(UserBuilder userBuilder) {
        this.id = userBuilder.id;
        this.login = userBuilder.login;
        this.password = userBuilder.password;
        this.email = userBuilder.email;
        this.name = userBuilder.name;
        this.surname = userBuilder.surname;
        this.phone = userBuilder.phone;
        this.balance = userBuilder.balance;
        this.language = userBuilder.language;
        this.role = userBuilder.role;
        this.notification = userBuilder.notification;
        this.status = userBuilder.status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return login.equals(user.login) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + "********" + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phone='" + phone + '\'' +
                ", balance=" + balance +
                ", language='" + language.value() + '\'' +
                ", role='" + role.value() + '\'' +
                ", notification=" + notification +
                ", status=" + status +
                '}';
    }


    public static class UserBuilder {
        private final String login;
        private final String password;
        private int id;
        private String email = null;
        private String name = null;
        private String surname = null;
        private String phone = null;
        private double balance = 0.0;
        private Language language = RU;
        private Role role = USER;
        private boolean notification = false;
        private Status status = BLOCKED;

        public UserBuilder(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public UserBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public UserBuilder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder setBalance(double balance) {
            this.balance = balance;
            return this;
        }

        public UserBuilder setLanguage(Language language) {
            this.language = language;
            return this;
        }

        public UserBuilder setRole(Role role) {
            this.role = role;
            return this;
        }

        public UserBuilder setNotification(boolean notification) {
            this.notification = notification;
            return this;
        }

        public UserBuilder setNotification(int notification) {
            if (notification == 0) {
                this.notification = false;
            } else {
                this.notification = true;
            }
            return this;
        }

        public UserBuilder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
