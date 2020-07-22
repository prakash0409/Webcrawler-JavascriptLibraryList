/**
 *
 */
package com.test.webcrawler.parser;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author prakashp7
 *
 */
public class GoogleResultTagParser {

    private static final String GOOGLE_RESULT_START_PATTERN = "<div class=\"kCrYT\"><a href=\"/url?q=";
    private static final String GOOGLE_RESULT_END_PATTERN = "\">";
    private static final Pattern GOOGLE_RESULT_PATTERN = Pattern.compile(Pattern.quote(GOOGLE_RESULT_START_PATTERN) + "(.*?)" + Pattern.quote(GOOGLE_RESULT_END_PATTERN));

    private GoogleResultTagParser() {
        throw new IllegalStateException("This is an utility class");
    }

    public static Set<String> getGoogleResultLinks(String html) {
        Set<String> result = new HashSet<>();
        Matcher m = GOOGLE_RESULT_PATTERN.matcher(html);

        while (m.find()) {
            String completeHTMLDivResult = m.group(0).trim();
            String domainNameWithQueryParams = completeHTMLDivResult.substring(completeHTMLDivResult.indexOf("/url?q=") + 7);
            String domainName = domainNameWithQueryParams.substring(0, domainNameWithQueryParams.indexOf("&amp;"));
            result.add(domainName);
        }
        return result;
    }
}
