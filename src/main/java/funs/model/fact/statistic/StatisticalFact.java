package funs.model.fact.statistic;

import funs.model.corpus.Document;
import funs.model.fact.UnaryFactInterface;

import java.util.Map;

public class StatisticalFact implements UnaryFactInterface {
    double idf;
    Map<Document, Double> tfidf;

    public StatisticalFact(double idf, Map<Document, Double> tfidf){
        this.idf = idf;
        this.tfidf = tfidf;
    }
}
