/**
 *
 */
package com.test.webcrawler;

import com.test.webcrawler.parser.JavascriptTagParser;
import com.test.webcrawler.utils.FileOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author prakashp7
 *
 */
public class JSLibraryTask implements Callable<Map<String, Integer>> {

    private static final Logger logger = LoggerFactory.getLogger(JSLibraryTask.class);

    private static final String SCRIPT_TAG_START_PATTERN = "<script";
    private static final String SCRIPT_TAG_END_PATTERN = "</script>";
    private static final Pattern SCRIPT_TAG_PATTERN = Pattern.compile(Pattern.quote(SCRIPT_TAG_START_PATTERN) + "(.*?)" + Pattern.quote(SCRIPT_TAG_END_PATTERN));

    private final String url;

    private Map<String, Integer> jsListMap;

    public JSLibraryTask(String url, Map<String, Integer> jsListMap) {
        this.url = url;
        this.jsListMap = jsListMap;
    }

    @Override
    public Map<String, Integer> call() {
        return getJSLibraryWebPage(this.url);
    }

    /**
     *  Download the page for the given URL
     * @param urlString
     * @return
     */
    public Map<String, Integer> getJSLibraryWebPage(String urlString) {
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
            jsListMap = getJSLibraryMap(FileOperations.readFileAsString(file));
            file.delete();
        } catch (MalformedURLException mue) {
            logger.error("Malformed URL Exception");
        } catch (IOException ie) {
            logger.error("IOException while connecting to the host");
        }
        return jsListMap;
    }

    /**
     *  Get the Map of all the js libraries
     * @param html
     * @return
     */
    private Map<String, Integer> getJSLibraryMap(String html) {
        Matcher m = SCRIPT_TAG_PATTERN.matcher(html);

        while (m.find()) {
            String scriptTag = m.group(0).trim();
            String jsLib = JavascriptTagParser.readValueFromScriptTag(scriptTag);
            if (!jsLib.isEmpty()) {
                if (jsListMap.containsKey(jsLib)) {
                    jsListMap.put(jsLib, jsListMap.get(jsLib) + 1);
                } else {
                    jsListMap.put(jsLib, 1);
                }
            }

        }
        return jsListMap;
    }

}
