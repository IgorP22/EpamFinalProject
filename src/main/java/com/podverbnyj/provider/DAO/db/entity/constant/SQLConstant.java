package com.podverbnyj.provider.DAO.db.entity.constant;

public class SQLConstant {

    public static class UserConstants {
        public static final String FIND_ALL_USERS = "SELECT * FROM user";
        public static final String GET_USER_BY_ID = "SELECT * FROM user WHERE user_id=?";
        public static final String GET_USER_BY_LOGIN = "SELECT * FROM user WHERE login=?";
        public static final String CREATE_USER = "INSERT INTO user (login, password, email,"
                + " name, surname, phone, balance, language, role, notification, status) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        public static final String UPDATE_USER = "UPDATE user SET login = ?, password = ?, "
                + "email = ?, name = ?, surname = ?, phone = ?, balance = ?, "
                + "language = ?, role= ?, notification = ?, status = ? WHERE (user_id = ?)";
        public static final String DELETE_USER_BY_LOGIN = "DELETE FROM user WHERE login = ?";
    }

    public static class ServiceConstants {
        public static final String FIND_ALL_SERVICES = "SELECT * FROM service";
        public static final String GET_SERVICE_BY_ID = "SELECT * FROM service WHERE service_id=?";
        public static final String CREATE_SERVICE = "INSERT INTO service (title_ru, title_en) "
                + "VALUES (?,?)";
        public static final String UPDATE_SERVICE = "UPDATE service SET title_ru = ?, title_en = ? "
                + " WHERE (service_id = ?)";
        public static final String DELETE_SERVICE_BY_ID = "DELETE FROM service WHERE service_id = ?";
    }

    public static class TariffConstants {
        public static final String FIND_ALL_TARIFFS = "SELECT * FROM tariff";
        public static final String GET_TARIFF_BY_ID = "SELECT * FROM tariff WHERE tariff_id=?";
        public static final String CREATE_TARIFF = "INSERT INTO tariff (name_ru, name_en, "
                + "price, service_id,description_ru, description_en) VALUES (?,?,?,?,?,?)";
        public static final String UPDATE_TARIFF = "UPDATE tariff SET name_ru = ?, name_en = ?, "
                + "price = ?, service_id = ?, description_ru = ?, description_en = ? WHERE (tariff_id = ?)";
        public static final String DELETE_TARIFF_BY_ID = "DELETE FROM tariff WHERE tariff_id = ?";
    }

}
