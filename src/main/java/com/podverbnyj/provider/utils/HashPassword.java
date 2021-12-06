package com.podverbnyj.provider.utils;

import com.podverbnyj.provider.dao.db.DBException;
import org.apache.logging.log4j.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPassword {

    private HashPassword() {
    }

    private static final Logger log = LogManager.getLogger(HashPassword.class);

    public static String securePassword(String input) throws DBException {
        MessageDigest digester;
        try {
            digester = MessageDigest.getInstance("SHA-256");
            digester.update(input.getBytes());
        } catch (NoSuchAlgorithmException ex) {
            log.debug("Secure password error", ex);
            throw new DBException("Secure password error");
        }


        byte[] hash = digester.digest();

        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02X", b));
        }
        log.debug("Password secured");
        return sb.toString();
    }
}
