/**
 *
 */
package com.test.webcrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.test.webcrawler.parser.GoogleResultTagParser;
import com.test.webcrawler.utils.HttpUrlConnection;
import com.test.webcrawler.utils.StandardInputUtil;

/**
 * @author prakashp7
 *
 */
public class WebCrawler {

    private static final String SEARCH_URL = "https://www.google.com/search";

    private static Map<String, Integer> jsListMap = new ConcurrentHashMap<>();

    /**
     * @param args
     */
    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        try {
            System.out.print("Enter the query to searched: ");
            String query = StandardInputUtil.readStringStandardInput();

            System.out.print("Enter the number of results to be fetched the server:");
            int resultLimit = StandardInputUtil.readIntStandardInput();

            ExecutorService executor = Executors.newFixedThreadPool(resultLimit);
            String searchURL = SEARCH_URL + "?q=" + query + "&num=" + resultLimit;

            String resultPage = HttpUrlConnection.searchCall(searchURL);
            Set<String> resultLinks = GoogleResultTagParser.getGoogleResultLinks(resultPage);
            System.out.println("----------------------------------------------");

            System.out.println("Below are the results for your query :" + resultLinks.size());
            for (String resultLink : resultLinks) {
                System.out.println(resultLink);
                JSLibraryTask jsLibraryTask = new JSLibraryTask(resultLink, jsListMap);
                executor.submit(jsLibraryTask);
            }
            executor.shutdown();

            // Wait until all threads are finished executing
            while (!executor.isTerminated()) {
            }
            System.out.println("----------------------------------------------");

            System.out.println("Below are the top 5 used js libraries");
            List<Entry<String, Integer>> sortedList = sortMapByValue(jsListMap);

            int count = 0;
            for (Map.Entry<String, Integer> entry : sortedList) {

                if (5 == count) {
                    break;
                }
                System.out.println(entry.getKey() + "--> " + entry.getValue());
                ++count;
            }

            long end = System.currentTimeMillis();

            System.out.println("Total time in milliseconds :" + (end - start));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Sort the Map by ascending values
     *
     * @param JsListMap
     * @return
     */
    public static List<Entry<String, Integer>> sortMapByValue(Map<String, Integer> JsListMap) {

        Set<Entry<String, Integer>> set = JsListMap.entrySet();
        List<Entry<String, Integer>> list = new ArrayList<>(set);
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        return list;
    }
}
