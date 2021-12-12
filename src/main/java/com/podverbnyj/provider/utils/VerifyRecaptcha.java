package com.podverbnyj.provider.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

/**
 * VerifyRecaptcha class, taken from example
 */
public class VerifyRecaptcha {

    public static final String URL = "https://www.google.com/recaptcha/api/siteverify";
    public static final String SECRET = "6Leyol4dAAAAAAoxBY1qQmEU7hBGm1Jb7SMRS3Wn";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final Logger log = LogManager.getLogger(VerifyRecaptcha.class);

    private VerifyRecaptcha() {
    }

    public static boolean verify(String gRecaptchaResponse) throws IOException {
        if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
            return false;
        }

        try {
            URL obj = new URL(URL);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            // add request header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String postParams = "secret=" + SECRET + "&response="
                    + gRecaptchaResponse;

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            log.info("Sending 'POST' request to URL : {}", URL);
            log.info("Response Code : {}", responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //parse JSON response and return 'success' value
            JsonObject jsonObject;
            try (JsonReader jsonReader = Json.createReader(new StringReader(response.toString()))) {
                jsonObject = jsonReader.readObject();
            }

            return jsonObject.getBoolean("success");
        } catch (Exception ex) {
            log.error("Error to get captha", ex);
            return false;
        }
    }
}