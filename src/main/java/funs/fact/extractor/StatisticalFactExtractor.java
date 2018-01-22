package funs.fact.extractor;

import funs.model.fact.FactStore;
import funs.model.fact.statistic.StatisticalFact;
import funs.model.corpus.Chunk;
import funs.model.corpus.Document;
import funs.util.Singleton;

import java.util.*;

/***
 * This class calculates the tf, idf, tf-idf and P(c_1, c_2) of all chunks.
 */
public class StatisticalFactExtractor implements FactExtractorInterface {

    @Override
    public void extract(Document doc) {
        List<Chunk> iter = doc.getChunks();
        FactStore fs = FactStore.getFactstore();
        for(Chunk c : doc.getChunks()){
            Map<Document, Integer> docFreq = calculateDocumentFrequency(c);
            double idf = calculateIdf(doc, c);
            Map<Document, Double> tfidf = calculateTfIdf(docFreq, idf);
            StatisticalFact fact = new StatisticalFact(idf, tfidf);
            fs.putFact(c, doc, fact);

        }
    }

    private Map<Document, Double> calculateTfIdf(Map<Document, Integer> tf, double idf){
        Map<Document, Double> result = new HashMap<>(tf.size());
        for (Document doc : tf.keySet()) {
            result.put(doc, tf.get(doc)*idf);
        }
        return result;
    }

    private double calculateIdf(Document doc, Chunk chunk){
        int N = Singleton.getInstance().getDocuments().size();
        int d = chunk.getDocuments().size();
        return Math.log(N/d);
    }

    private Map<Document, Integer> calculateDocumentFrequency(Chunk chunk){
        Map<Document, Integer> result = new HashMap<>();
        for (Document doc : chunk.getDocuments()){
            result.put(doc, doc.getFrequency(chunk));
        }
        return result;
    }

    private Map<String, Double> calculateDependency(Chunk chunk){
        /*
        Map<String, Double> result = new HashMap<>();
        Iterator<Chunk> iter = store.getChunks();
        Set<Integer> d = chunk.appearsInDocs();
        double totalDocs = docs.getDocuments().size();
        while (iter.hasNext()){
            Chunk c = iter.next();
            if (!c.getId().equals(chunk.getId())){
                Set<Integer> shared =  new HashSet<>(d);
                shared.retainAll(c.appearsInDocs());
                double prob = (((double)shared.size())/totalDocs)/
                        (((double)c.appearsInDocs().size())/totalDocs);
                result.put(c.getId(), prob);
            }
        }
        return result;*/
        return null;
    }
}
