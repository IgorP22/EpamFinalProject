package com.podverbnyj.provider.urils;

import com.podverbnyj.provider.logic.command.LoginCommand;
import org.apache.logging.log4j.*;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPassword {

    private static final Logger log = LogManager.getLogger(LoginCommand.class);

    public static String securePassword(String input)  {
        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            log.debug("Secure password error", ex);
        }
        digester.update(input.getBytes());
        byte[] hash = digester.digest();

        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
