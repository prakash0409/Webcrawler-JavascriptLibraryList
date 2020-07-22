package com.test.webcrawler.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author prakashp7
 */
public class StandardInputUtil {

    private StandardInputUtil() {
        throw new IllegalStateException("This is an utility class");
    }

    public static String readStringStandardInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    public static int readIntStandardInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int number;
        try {
            number = Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a correct input. Enter a numeric value");
            number = readIntStandardInput();
        }
        return number;
    }

}
