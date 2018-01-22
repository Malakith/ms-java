package funs.acquisition;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import funs.CallCache;
import funs.model.corpus.Corpus;
import funs.model.corpus.Document;
import funs.util.Singleton;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HtmlCollector {

    private static CallCache<String> cache = new CallCache<>("html_requester");

    public static void getHtml(){
        Corpus corp = Singleton.getInstance();
        Unirest.setTimeouts(20000, 120000);
        for (Document doc : corp.getDocuments()){
            getHtml(doc);
        }
        System.out.println("Done getting html, removing bad documents.");
        System.out.println("Before cleaning there are " + corp.getDocuments().size() + " documents.");
        List<Document> cleanDocks = new LinkedList<>();
        for (Document doc : corp.getDocuments()) {
            if (!doc.isMarkedForDeletion()) {
                cleanDocks.add(doc);
            }
        }
        corp.setDocuments(cleanDocks);
        System.out.println("After clearning we had " + corp.getDocuments().size() + " documents.");
        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Future<Boolean>> futures = new LinkedList<>();
        for (Document doc : corp.getDocuments()){
            futures.add(executor.submit(() -> {
                HTMLParser parser = new HTMLParser();
                HTMLParser.ParseResult p = parser.parseHtml(doc.getHtml());
                doc.setParse(p);
                doc.setText(p.getText());
                return new Boolean(true);
            }));
        }
        executor.shutdown();
        int i = 0;
        for (Future f : futures){
            try {
                f.get();
                i++;
                System.out.println("Done with " + i + " of " + futures.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("shutting down executor.");
        System.out.println("Done getting html");
    }

    private static void getHtml(Document doc){
        List<String> cacheHtml = cache.load(doc.getUrl());
        String html;
        if (cacheHtml != null) {
            html = cacheHtml.get(0);
        } else {
            html = getHtml(doc.getUrl());
        }
        if (html == null || html.length() == 0) {
            doc.markForDeletion();
        } else {
            doc.setHtml(html);
        }
        cache.save(doc.getUrl(), html);
    }

    private static String getHtml(String url){
        String html = null;
        try {
            Unirest.setHttpClient(HttpClients.custom()
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, (x509Certificates, s) -> true).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .disableCookieManagement()
                    .build());
            html = Unirest.get(url).asString().getBody();
        } catch (UnirestException e) {
            if (e.getCause() instanceof javax.net.ssl.SSLHandshakeException
                    || e.getCause() instanceof javax.net.ssl.SSLException){
                System.out.println("SSL failed for url: " + url);
            } else {
                System.out.println("Error for url: " + url);
                if (e.getCause() instanceof SocketTimeoutException) {
                    System.out.println("Socket timed out.");
                } else {
                    e.printStackTrace();
                }

            }
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }
        return html;
    }
}
