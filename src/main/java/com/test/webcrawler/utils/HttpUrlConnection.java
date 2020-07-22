/**
 *
 */
package com.test.webcrawler.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * @author prakashp7
 *
 */
public class HttpUrlConnection {

    private static final Logger logger = LoggerFactory.getLogger(HttpUrlConnection.class);

    private static final String HTTP_CONTENT_TYPE_PROPERTY = "Content-Type";
    private static final String HTTP_CONTENT_TYPE_VALUE = "application/json;charset=utf-8";
    private static final String CHARACTER_SET = "UTF-8";
    private static final int HTTP_STATUS_OK = 200;
    private static final String USER_AGENT_VALUE = "Mozilla/5.0";
    private static final String USER_AGENT_KEY = "User-Agent";

    public static String searchCall(final String urlString) {
        logger.debug("Start - Server GET CALL {}", urlString);
        String url = urlString.replaceAll(" ", "%20");
        try {
            final URL u = new URL(url);
            final HttpURLConnection httpCon = (HttpURLConnection) u.openConnection();
            httpCon.setRequestMethod("GET");
            httpCon.setRequestProperty(HTTP_CONTENT_TYPE_PROPERTY, HTTP_CONTENT_TYPE_VALUE);
            httpCon.setUseCaches(false);
            httpCon.setRequestProperty(USER_AGENT_KEY, USER_AGENT_VALUE);
            int responseCode = httpCon.getResponseCode();
            StringBuilder response = new StringBuilder();
            InputStream in;
            if (responseCode == HTTP_STATUS_OK) {
                in = httpCon.getInputStream();
            } else {
                in = httpCon.getErrorStream();
            }

            if (in != null) {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(in, CHARACTER_SET));

                char[] buffer = new char[1024];
                int len;
                while ((len = inputReader.read(buffer)) > 0) {
                    response.append(new String(Arrays.copyOfRange(buffer, 0, len)));
                }
                inputReader.close();
            }

            return response.toString();
        } catch (final IOException e) {
            logger.error("Error while getting results from server", url, e);
        }
        return "";
    }

}
