package funs.fact.extractor;

import funs.model.corpus.Corpus;
import funs.model.corpus.Document;
import funs.util.Singleton;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.dictionary.Dictionary;

public class SemanticExtractor implements FactExtractorInterface{
    @Override
    public void extract(Document docs) {

        Corpus corpus = Singleton.getInstance();
        Dictionary dictionary = null;
        try {
            dictionary = Dictionary.getDefaultResourceInstance();
        } catch (JWNLException e) {
            e.printStackTrace();
        }


    }
}
