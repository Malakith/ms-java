package funs.model.corpus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Corpus {

    private Corpus instance;
    List<Document> docs;
    List<Chunk> chunks;

    public Corpus(){
        docs = new LinkedList<>();
        chunks = new LinkedList<>();
    }

    public List<Document> getDocuments(){
        return docs;
    }

    public void setDocuments(List<Document> docs){
        this.docs = docs;
    }

    public List<Chunk> getChunks(){
        return chunks;
    }

    public void addChunk(Chunk chunk){
        chunks.add(chunk);
    }

    public void addDocument(Document doc){
        docs.add(doc);
    }
}
