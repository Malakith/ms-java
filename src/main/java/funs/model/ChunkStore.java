package funs.model;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChunkStore {

    private Map<String, Chunk> chunks = new HashMap<>();

    public synchronized Chunk createChunk(List<String> words){
        String id = String.join("_", words);
        Chunk chunk = chunks.get(id);
        if (chunk == null){
            chunk = new Chunk(id, words);
            chunks.put(id, chunk);
        }
        return chunk;
    }

    public synchronized Chunk createChunk(String word){
        Chunk chunk = chunks.get(word);
        if (chunk == null){
            chunk = new Chunk(word);
            chunks.put(word, chunk);
        }
        return chunk;
    }

    public Chunk getChunk(List<String> words){
        String id = String.join("_", words);
        return chunks.get(id);
    }

    public Iterator<Chunk> getChunks(){
        return chunks.values().iterator();
    }

}
