package com.podverbnyj.provider.DAO.db;

public class SQLConstant {

    public static class UserConstants {
        public static  final String FIND_ALL_USERS = "SELECT * FROM user";
        public static final String GET_USER_BY_LOGIN = "SELECT * FROM user WHERE login=?";
    }

}
