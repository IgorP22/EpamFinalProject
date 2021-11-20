package com.podverbnyj.provider.utils;

import com.podverbnyj.provider.logic.command.LoginCommand;
import org.apache.logging.log4j.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PhoneFormatCorrector {

    private static final Logger log = LogManager.getLogger(LoginCommand.class);

    public static String correctPhone(String input) {

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);
        StringBuilder digits = new StringBuilder();
        while (matcher.find()) {
            digits.append(matcher.group());
        }
        String result = null;
        if (digits.length() >=9) {
            result = "+380" + digits.substring(digits.length()-9);
        }
        log.debug("Phone number changed (" + input + ") ==> (" + result + ")");
        return result;
    }
}
