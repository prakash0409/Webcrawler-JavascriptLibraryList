package com.test.webcrawler.parser;

import com.test.webcrawler.utils.FileOperations;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author prakashp7
 */
public class JavascriptTagParser {

    private static final String SCRIPT_TAG_START_PATTERN = "<script";
    private static final String SCRIPT_TAG_END_PATTERN = "</script>";
    private static final Pattern SCRIPT_TAG_PATTERN = Pattern.compile(Pattern.quote(SCRIPT_TAG_START_PATTERN) + "(.*?)" + Pattern.quote(SCRIPT_TAG_END_PATTERN));

    private static final String SCRIPT_VALUE_ATTRIBUTE_START= "src";
    private static final String SCRIPT_VALUE_ATTRIBUTE_END = ".js";
    private static final Pattern SCRIPT_VALUE_ATTRIBUTE_PATTERN = Pattern.compile(Pattern.quote(SCRIPT_VALUE_ATTRIBUTE_START) + "(.*?)" + Pattern.quote(SCRIPT_VALUE_ATTRIBUTE_END));

    public static List<String> getJSLibraryWebPage(String urlString) {
        List<String> jsLibraryList = new ArrayList<>();
        try {
            URL url = new URL(urlString);
            InputStream inStr = url.openStream();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(inStr));

            BufferedWriter writer =
                    new BufferedWriter(new FileWriter(url.getHost() + ".html"));

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }

            reader.close();
            writer.close();

            File file = new File(url.getHost() + ".html");
            jsLibraryList = getJSLibrary(FileOperations.readFileAsString(file));
            file.delete();
        } catch (MalformedURLException mue) {
            System.out.println("Malformed URL Exception raised");
        } catch (IOException ie) {
            System.out.println("IOException raised");
        }
        return jsLibraryList;
    }

    /**
     *  Get the Map of all the js libraries
     * @param html
     * @return
     */
    public static List<String> getJSLibrary(String html) {
        Matcher m = SCRIPT_TAG_PATTERN.matcher(html);

        List<String> jsLibraryList = new ArrayList<>();

        while (m.find()) {
            String scriptTag = m.group(0).trim();
            String jsLib = readValueFromScriptTag(scriptTag);
            if (!jsLib.isEmpty()) {
                jsLibraryList.add(jsLib);
            }

        }
        return jsLibraryList;
    }

    /**
     *  Read the value from the script tag
     * @param scriptTag
     * @return
     */
    public  static String readValueFromScriptTag(String scriptTag) {
        Matcher m = SCRIPT_VALUE_ATTRIBUTE_PATTERN.matcher(scriptTag);
        if (m.find()) {
            String jsLibrary = m.group(1).trim();
            jsLibrary = jsLibrary.substring(jsLibrary.lastIndexOf("/") + 1);
            if (jsLibrary.indexOf(".") != -1) {
                jsLibrary = jsLibrary.substring(0, jsLibrary.indexOf("."));
            }
            return jsLibrary;
        }
        return "";
    }

}
