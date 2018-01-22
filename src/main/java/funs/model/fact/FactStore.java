package funs.model.fact;

import funs.model.corpus.Chunk;
import funs.model.corpus.Document;

import java.util.*;

public class FactStore {

    private static List<FactInterface> facts;
    private static Map<Chunk, List<FactInterface>> chunkFactLookup;
    private static Map<Document, List<FactInterface>> docFactLookup;
    private static Map<Class<?>, List<FactInterface>> typeFactLookup;
    private static FactStore instance = new FactStore();

    public FactStore(){
        facts = new ArrayList<>();
        chunkFactLookup = new HashMap<>();
        docFactLookup = new HashMap<>();
        typeFactLookup = new HashMap<>();
    }

    public static FactStore getFactstore(){
        return instance;
    }

    public synchronized List<FactInterface> getFacts(Chunk chunk){
        return chunkFactLookup.get(chunk);
    }
    public synchronized List<FactInterface> getFacts(Document doc){
        return docFactLookup.get(doc);
    }
    public synchronized List<FactInterface> getFacts(Class<?> type){
        return chunkFactLookup.get(type);
    }
    public synchronized void putFact(Chunk chunk, Document doc, FactInterface fact){
        chunkFactLookup.putIfAbsent(chunk, new LinkedList<FactInterface>()).add(fact);
        docFactLookup.putIfAbsent(doc, new LinkedList<FactInterface>()).add(fact);
        typeFactLookup.putIfAbsent(fact.getClass(), new LinkedList<FactInterface>()).add(fact);
    }
}
