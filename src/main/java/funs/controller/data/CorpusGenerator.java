package funs.controller.data;

import funs.acquisition.Bing;
import funs.model.corpus.Document;
import funs.model.corpus.Corpus;
import funs.util.Singleton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CorpusGenerator {

    public static Corpus generateCorpus(String concept, String[] attributes, int queryResults){
        Corpus corp = Singleton.getInstance();
        Set<String> urls = new HashSet<>();
        for (String query : createSearchQueries(concept, attributes)){
            try {
                Thread.sleep(500); //To prevent Bing blocking us.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            urls.addAll(getUrls(query, queryResults));
        }
        for (String url : urls){
            Document doc = new Document(url);
            corp.addDocument(doc);
        }
        return corp;
    }

    private static List<String> createSearchQueries(String concept, String[] attributes){
        List<String> queries = new ArrayList<>(attributes.length*3);
        for (String a : attributes){
            queries.add(concept + "'s " + a);
            queries.add(a + " of " + concept);
            queries.add(concept + " " + a);
        }
        return queries;
    }

    private static Set<String> getUrls(String query, int queryResults){
        return Bing.searchWeb(query, queryResults);
    }
}
