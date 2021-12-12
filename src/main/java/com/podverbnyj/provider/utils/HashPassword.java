package com.podverbnyj.provider.utils;

import com.podverbnyj.provider.dao.db.DBException;
import org.apache.logging.log4j.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Secure password class
 */
public class HashPassword {

    private HashPassword() {
    }

    private static final Logger log = LogManager.getLogger(HashPassword.class);

    /**
     * Secure password method
     *
     * @param input password
     * @return password conwerted with SHA-256 algorithm
     * @throws DBException our own exception
     */
    public static String securePassword(String input) throws DBException {
        MessageDigest digester;
        try {
            digester = MessageDigest.getInstance("SHA-256");
            digester.update(input.getBytes());
        } catch (NoSuchAlgorithmException ex) {
            log.debug("Secure password error", ex);
            // wrong exception name, used to throw our own exception
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
