package com.test.webcrawler.streams;

import com.test.webcrawler.parser.GoogleResultTagParser;
import com.test.webcrawler.parser.JavascriptTagParser;
import com.test.webcrawler.utils.HttpUrlConnection;
import com.test.webcrawler.utils.StandardInputUtil;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author prakashp7
 */
public class StreamWebCrawler {

    private static final String SEARCH_URL = "https://www.google.com/search";

    public static void main(String[] args) throws IOException {
        programUsingStreamApi();
    }

    private static void programUsingStreamApi() throws IOException {
        long start = System.currentTimeMillis();

        System.out.print("Enter the query to searched: ");
        String query = StandardInputUtil.readStringStandardInput();

        System.out.print("Enter the number of results to be fetched the server:");
        int resultLimit = StandardInputUtil.readIntStandardInput();

        String searchURL = SEARCH_URL + "?q=" + query + "&num=" + resultLimit;

        //Get list of all search results
        String resultPage = HttpUrlConnection.searchCall(searchURL);
        Set<String> resultLinks = GoogleResultTagParser.getGoogleResultLinks(resultPage);

        //Get the website url and its list of javascript library which are used
        Map<String, List<String>> websiteScriptMap = resultLinks.parallelStream()
                .collect(
                        Collectors.toMap(Function.identity(),
                                p -> JavascriptTagParser.getJSLibraryWebPage(p)
                        ));

        //Deduce the logic to list out the javascript library count from all websites
        Map<String, Integer> jsLibMap = new HashMap<>();
        for (Map.Entry<String, List<String>> stringListEntry : websiteScriptMap.entrySet()) {
            List<String> s = stringListEntry.getValue();
            s.stream()
                    .forEach(p -> {
                                if (jsLibMap.containsKey(p)) {
                                    jsLibMap.put(p, jsLibMap.get(p) + 1);
                                } else {
                                    jsLibMap.put(p, 1);
                                }
                            }
                    );
        }


        System.out.println("----------------------------------------------");

        System.out.println("Print out the top 5 used js libraries");
        jsLibMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(5)
                .forEach(
                        entry -> System.out.println(entry.getKey() + " --> " + entry.getValue())
                );

        System.out.println("----------------------------------------------");

        long end = System.currentTimeMillis();

        System.out.println("Total time in milliseconds :" + (end - start));
    }
}
