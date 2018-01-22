package funs.fact.extractor;

import edu.stanford.nlp.simple.Document;
import funs.model.ChunkStore;
import funs.model.DocumentStore;

import java.util.List;

public class SyntacticFactExtractor implements FactExtractorInterface {

    private List<String> getHearstPartners(funs.model.Document doc){
        Document nlpDoc = doc.getNLPDoc();
        return null;
    }

    @Override
    public void extract(funs.model.corpus.Document docs) {

    }
}
