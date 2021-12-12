package com.podverbnyj.provider.utils;

import org.apache.logging.log4j.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Phone format corrector, not used yet. Plane use it later for restore password by phone part.
 */
public class PhoneFormatCorrector {

    private PhoneFormatCorrector() {
    }

    private static final Logger log = LogManager.getLogger(PhoneFormatCorrector.class);

    public static String correctPhone(String input) {

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);
        StringBuilder digits = new StringBuilder();
        while (matcher.find()) {
            digits.append(matcher.group());
        }

        log.debug("Phone number changed ({}) ==> ({})",input,input);
        return digits.toString();
    }
}
