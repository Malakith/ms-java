package funs.fact;

import java.util.Map;

public class StatisticalFact {
    Map<Integer, Integer> docFreq;
    double idf;
    Map<Integer, Double> tfidf;
    Map<String, Double> dependency;

    public StatisticalFact(Map<Integer, Integer> docFreq,
                           double idf,
                           Map<Integer, Double> tfidf,
                           Map<String, Double> dependency){
        this.docFreq = docFreq;
        this.idf = idf;
        this.tfidf = tfidf;
        this.dependency = dependency;
    }
}
