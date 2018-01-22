package funs.acquisition;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import funs.CallCache;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.ssl.SSLContextBuilder;


import javax.net.ssl.SSLException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class Collector {
    private CallCache<String> cache;
    private Bing bing = new Bing();

    public Collector(){
        try {
            Unirest.setAsyncHttpClient(HttpAsyncClients.custom()
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, (x509Certificates, s) -> true).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .disableCookieManagement()
                    .build());
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }
        cache = new CallCache<>("collector_cache");
    }

    public List<String> getPages(String concept, String... attributes){
        String id = concept + String.join(",", attributes);
        List<String> pages = cache.load(id);
        if (pages != null) {
            return pages;
        }
        pages = getContentOfPages(getUrls(concept, attributes));
        cache.save(id, pages);
        return pages;
    }

    public Set<String> getUrls(String concept, String... attributes) {
        Set<String> urls = new HashSet<>();
        for (String a : attributes) {
            urls.addAll(bing.searchWeb(concept + "'s " + a));
            urls.addAll(bing.searchWeb(a + " of " + concept));
            urls.addAll(bing.searchWeb(concept + " " + a));
        }
        return urls;
    }

    public List<String> getContentOfPages(Set<String> urls) {
        List<Future<HttpResponse<String>>> responses = new LinkedList<Future<HttpResponse<String>>>();
        for (String url : urls) {
            responses.add(Unirest.get(url).asStringAsync());
        }
        List<String> content = new LinkedList<String>();
        for (Future<HttpResponse<String>> future : responses) {
            try {
                content.add(future.get(5, TimeUnit.MINUTES).getBody());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                if ((e.getCause() instanceof SSLException)){
                    continue;

                } else if(e.getCause() instanceof CircularRedirectException) {
                    continue;
                } else {
                    e.printStackTrace();
                }
            } catch (TimeoutException e) {
                continue;
            }
        }
        try {
            Unirest.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}

