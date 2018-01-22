package funs.model.corpus;

import edu.stanford.nlp.ie.machinereading.structure.Span;
import edu.stanford.nlp.simple.Sentence;

import java.util.*;

public class Chunk implements Comparable  {
    List<Sentence> sentences;
    List<Document> documents;
    List<String> words;
    Map<Sentence, Span> locations;
    String posTag;
    String headWord;
    String nerTag;
    int frequency;


    public Chunk(String word){
        frequency = 1;
        sentences = new LinkedList<>();
        documents = new LinkedList<>();
        locations = new HashMap<>();
        this.words = Arrays.asList(word);
    }

    public Chunk(List<String> words){
        frequency = 1;
        sentences = new LinkedList<>();
        documents = new LinkedList<>();
        locations = new HashMap<>();
        this.words = words;
    }

    public String getNerTag() {
        return nerTag;
    }

    public String getHeadWord() {
        return headWord;
    }

    public List<Document> getDocuments(){
        return documents;
    }

    public List<Sentence> getSentences(){
        return sentences;
    }

    public void addSentence(Sentence sentence){
        sentences.add(sentence);
    }

    public int getFrequency(){
        return frequency;
    }

    public void addLocation(Sentence sentence, Span location){
        locations.put(sentence, location);
    }

    public void setNerTag(String tag){
        nerTag = tag;
    }

    public void setHeadWord(String headword){
        this.headWord = headword;
    }

    public void addDocument(Document document){
        documents.add(document);
    }

    public void setPosTag(String tag){
        posTag = tag;
    }

    public List<String> getWords(){
        return words;
    }

    public boolean equals(Object obj){
        Chunk c = (Chunk) obj;
        boolean equal = true;
        equal &= this.words.size() == c.getWords().size();
        if (equal){
            for (int i = 0; i < this.words.size(); i++){
                equal &= this.words.get(i).equals(c.getWords().get(i));
            }
        }
        equal &= this.headWord.equals(c.headWord)
                && this.posTag.equals(c.posTag)
                && this.nerTag.equals(c.nerTag);
        return equal;
    }

    @Override
    public int compareTo(Object o) {
        Chunk c = (Chunk) o;
        Iterator<String> it1 = this.words.iterator();
        Iterator<String> it2 = c.getWords().iterator();
        while (it1.hasNext() && it2.hasNext()){
            int comp = it1.next().compareTo(it2.next());
            if (comp != 0){
                return (int)Math.signum(comp);
            }
        }
        if (this.words.size() < c.getWords().size()) {
            return -1;
        } else if (this.words.size() > c.getWords().size()){
            return 1;
        } else {
            return 0;
        }
    }

    public void merge(Chunk o){
        this.documents.addAll(o.documents);
        this.sentences.addAll(o.sentences);
        this.locations.putAll(o.locations);
        this.frequency += o.frequency;
    }

    public static List<Chunk> mergeLists(List<Chunk> l1, List<Chunk> l2){
        List<Chunk> result = new LinkedList<>();
        if (l1.size() == 0 || l2.size() == 0) {
            result.addAll(l1);
            result.addAll(l2);
            return result;
        }
        Collections.sort(l1);
        Collections.sort(l2);

        Iterator<Chunk> it1 = l1.iterator();
        Iterator<Chunk> it2 = l2.iterator();

        Chunk c1 = it1.next();
        Chunk c2 = it2.next();

        while (it1.hasNext() && it2.hasNext()){
            int comp = c1.compareTo(c2);

            switch(comp) {
                case -1:
                    result.add(c1);
                    c1 = it1.next();
                    break;
                case 1:
                    result.add(c2);
                    c2 = it2.next();
                    break;
                default:
                    if (c1.equals(c2)){
                        c1.merge(c2);
                        result.add(c1);

                    } else {
                        result.add(c1);
                        result.add(c2);
                    }
                    c1 = it1.next();
                    c2 = it2.next();
            }
        }
        it1.forEachRemaining(result::add);
        it2.forEachRemaining(result::add);
        return result;
    }
}
