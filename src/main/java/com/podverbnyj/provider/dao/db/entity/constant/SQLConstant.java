package com.podverbnyj.provider.dao.db.entity.constant;

/**
 * SQL constants for all tables database 'provider'
 */
public class SQLConstant {
    private SQLConstant() {
    }

    public static class UserConstants {

        /**
         * Constants for table 'user'
         */
        private UserConstants() {
        }

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
        public static final String COUNT_ADMINS = "SELECT COUNT(*) FROM user WHERE role = 'ADMIN'";

    }

    /**
     * Constants for table 'service'
     */
    public static class ServiceConstants {
        private ServiceConstants() {
        }

        public static final String FIND_ALL_SERVICES = "SELECT * FROM service";
        public static final String GET_SERVICE_BY_ID = "SELECT * FROM service WHERE service_id=?";
        public static final String CREATE_SERVICE = "INSERT INTO service (title_ru, title_en) VALUES (?,?)";
        public static final String UPDATE_SERVICE = "UPDATE service SET title_ru = ?, title_en = ? "
                + " WHERE (service_id = ?)";
        public static final String DELETE_SERVICE_BY_ID = "DELETE FROM service WHERE service_id = ?";
    }

    /**
     * Constants for table 'tariff'
     */
    public static class TariffConstants {
        private TariffConstants() {
        }
        public static final String FIND_ALL_TARIFFS = "SELECT * FROM tariff";
        public static final String GET_TARIFF_BY_ID = "SELECT * FROM tariff WHERE tariff_id=?";
        public static final String CREATE_TARIFF = "INSERT INTO tariff (name_ru, name_en, "
                + "price, service_id,description_ru, description_en) VALUES (?,?,?,?,?,?)";
        public static final String UPDATE_TARIFF = "UPDATE tariff SET name_ru = ?, name_en = ?, "
                + "price = ?, service_id = ?, description_ru = ?, description_en = ? WHERE (tariff_id = ?)";
        public static final String DELETE_TARIFF_BY_ID = "DELETE FROM tariff WHERE tariff_id = ?";
    }

    /**
     * Constants for table 'user_tariffs'
     */
    public static class UserTariffConstants {
        private UserTariffConstants() {
        }

        public static final String GET_ALL_TARIFFS_BY_USER_ID = "SELECT * FROM user_tariffs WHERE user_id = ?";
        public static final String CREATE_USER_ID_TARIFF = "INSERT INTO user_tariffs (user_id, tariff_id) "
                + "VALUES (?,?)";
        public static final String DELETE_TARIFFS_BY_USER_ID = "DELETE FROM user_tariffs WHERE user_id = ?";
        public static final String GET_TOTAL_COST_BY_USER_ID = "SELECT SUM(price) FROM user_tariffs JOIN tariff "
                + "ON user_tariffs.tariff_id = tariff.tariff_id WHERE user_id = ?";
    }

    /**
     * Constants for table 'user_payments'
     */
    public static class UserPaymentsConstants {
        private UserPaymentsConstants() {
        }

        public static final String FIND_ALL_PAYMENTS_BY_USER_ID = "SELECT * FROM user_payments WHERE user_id = ? " +
                "ORDER BY date DESC";
        public static final String CREATE_PAYMENT = "INSERT INTO user_payments (user_id, date, "
                + "sum) VALUES (?,?,?)";
        public static final String GET_COUNT_USERS_PAYMENT_SIZE = "SELECT COUNT(*) FROM user_payments WHERE user_id = ?";
        public static final String FIND_GROUP_PAYMENTS_BY_USER_ID = "SELECT * FROM user_payments WHERE user_id = ? " +
                "ORDER BY date DESC LIMIT ? OFFSET ?";
        public static final String GET_COUNT_USERS_PAYMENT_SIZE_ONLY_POSITIVE = "SELECT COUNT(*) FROM user_payments " +
                "WHERE user_id = ? AND sum>0";
        public static final String FIND_GROUP_PAYMENTS_BY_USER_ID_ONLY_POSITIVE = "SELECT * FROM user_payments " +
                "WHERE user_id = ? AND sum>0 ORDER BY date DESC LIMIT ? OFFSET ?";
    }

    /**
     * Constants for table 'password_recovery'
     */
    public static class PasswordRecoveryConstants {
        private PasswordRecoveryConstants() {
        }
        public static final String CREATE_RECOVERY_CODE = "INSERT INTO password_recovery (user_id, code) "
                + "VALUES (?,?)";
        public static final String GET_ENTITY_BY_USER_ID = "SELECT * FROM password_recovery WHERE user_id = ?";
        public static final String GET_ENTITY_BY_CODE = "SELECT * FROM password_recovery WHERE code = ?";
        public static final String DELETE_ENTRY_BY_CODE = "DELETE FROM password_recovery WHERE code = ?";
    }
}



