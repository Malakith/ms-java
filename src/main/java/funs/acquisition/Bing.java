package funs.acquisition;

import java.net.*;
import java.util.*;
import java.io.*;
import javax.net.ssl.HttpsURLConnection;

/*
 * Gson: https://github.com/google/gson
 * Maven info:
 *     groupId: com.google.code.gson
 *     artifactId: gson
 *     version: 2.8.1
 *
 * Once you have compiled or downloaded gson-2.8.1.jar, assuming you have placed it in the
 * same folder as this file (BingWebSearch.java), you can compile and run this program at
 * the command line as follows.
 *
 * javac BingWebSearch.java -classpath .;gson-2.8.1.jar -encoding UTF-8
 * java -cp .;gson-2.8.1.jar BingWebSearch
 */
import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import funs.CallCache;
import org.json.JSONArray;

public class Bing {

// ***********************************************
// *** Update or verify the following values. ***
// **********************************************

    // Replace the subscriptionKey string value with your valid subscription key.
    static String subscriptionKey = "";

    // Verify the endpoint URI.  At this writing, only one endpoint is used for Bing
    // search APIs.  In the future, regional endpoints may be available.  If you
    // encounter unexpected authorization errors, double-check this value against
    // the endpoint for your Bing Web search instance in your Azure dashboard.
    static String url = "https://api.cognitive.microsoft.com/bing/v7.0/search";
    static CallCache<String> cache = new CallCache<>("bing_cache");


    public static Set<String> searchWeb(String searchQuery){
        return searchWeb(searchQuery, 50);
    }

    public static Set<String> searchWeb(String searchQuery, int results){
        return searchWeb(searchQuery, results, 0);
    }

    public static Set<String> searchWeb(String searchQuery, int results, int offset) {
        Set<String> urls = new HashSet<>(results);
        List<String> cached = cache.load(searchQuery);
        if (cached != null) {
            urls.addAll(Arrays.asList(cached.get(0).split(" ")));
            System.out.println("Found cache with " + urls.size() + "urls.");
            if (urls.size() >= results){
                return urls;
            }
            results -= urls.size();
            offset += urls.size();
        }
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            jsonResponse = Unirest.get(url)
                    .header("Ocp-Apim-Subscription-Key", subscriptionKey)
                    .header("accept", "application/json")
                    .queryString("count", Math.min(50, results))
                    .queryString("responseFilter", "webpages")
                    .queryString("offset", offset)
                    .queryString("q", searchQuery).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        System.out.println(jsonResponse);
        JSONArray queries = jsonResponse.getBody()
                .getObject().getJSONObject("webPages")
                .getJSONArray("value");

        for (int i = 0; i < queries.length(); i++){
            urls.add(queries.getJSONObject(i).getString("url"));
        }

        if (urls.size() < results && urls.size() > 0){
            try {
                Thread.sleep(500);
                urls.addAll(searchWeb(searchQuery, results-urls.size(), offset+results));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        cache.save(searchQuery, String.join(" ", urls));
        return urls;
    }

}
