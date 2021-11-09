package com.podverbnyj.provider.DAO.db;

public class UserDBManager {


    static UserDBManager instance;

    public static synchronized UserDBManager getInstance() {
        if (instance == null) {
            instance = new UserDBManager();
        }
        return instance;
    }

    private UserDBManager() {
        // no op
    }



}
