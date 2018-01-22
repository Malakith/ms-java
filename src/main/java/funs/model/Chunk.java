package funs.model;

import java.util.*;

public class Chunk {

    private String id;
    private String type;
    private String headword;
    private List<String> words;
    private Map<Integer,Map<Integer, Set<Integer>>> inCorpus;
    private Map<Class<?>, Object> factStore = new HashMap<>();

    public Chunk(String id, List<String> words){
        this.id = id;
        this.words = words;
        this.inCorpus = new HashMap<>();
    }

    public Chunk(String word){
        this.words = new LinkedList<>();
        this.words.add(word);
        this.inCorpus = new HashMap<>();
    }

    public void addLocation(int documentId, int sentenceId, int wordIndex){
        Map<Integer, Set<Integer>> inDocuments = inCorpus.computeIfAbsent(documentId, k -> new HashMap<>(1));
        Set<Integer> inSentences = inDocuments.computeIfAbsent(sentenceId, k -> new HashSet<>(1));
        inSentences.add(wordIndex);
    }

    public <T> void setFact(Class<T> type, T fact){
        factStore.put(type, fact);
    }

    public <T> T getFact(Class<T> type){
        return type.cast(factStore.get(type));
    }

    public Map<Integer, Map<Integer, Set<Integer>>> getLocations(){
        return inCorpus;
    }

    public String getType(){
        return type;
    }

    public String getId(){
        return id;
    }

    public List<String> getWords(){
        return words;
    }

    public void setType(String type){
        this.type = type;
    }

    public Set<Integer> appearsInDocs(){
        return inCorpus.keySet();
    }

    public Set<Integer> appearsInSents(int docId){
        if (inCorpus.containsKey(docId)) {
            return inCorpus.get(docId).keySet();
        } else {
            return new HashSet<>();
        }
    }


    public String getHeadword() {
        return headword;
    }

    public void setHeadword(String headword) {
        this.headword = headword;
    }
}
