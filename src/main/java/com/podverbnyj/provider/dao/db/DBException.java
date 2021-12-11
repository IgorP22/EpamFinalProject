package com.podverbnyj.provider.dao.db;

public class DBException extends Exception {

    /**
     * Our own exception for sending high level message about errors
     * @param message
     */
    public DBException(String message) {
        super(message);
    }

}
