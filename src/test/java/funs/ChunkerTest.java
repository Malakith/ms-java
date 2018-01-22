package funs;

import funs.model.Chunk;
import funs.model.ChunkStore;
import funs.processing.Chunker;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

public class ChunkerTest {
    /*
    @Test
    public void chunkerTest(){
        String text = "It was the engine block that failed me. He was a sweet young man.";
        ChunkStore store = new ChunkStore();
        Chunker.chunk(store, 1, text);
        Iterator<Chunk> iter = store.getChunks();
        while (iter.hasNext()){
            Chunk c = iter.next();
            System.out.println("  ============  ");
            System.out.println();
            System.out.println(c.getWords());
            System.out.println(c.getType());
            System.out.println(c.getLocations());
            System.out.println();
        }
    }

    @Test
    public void secondChunkerTest(){
        String text = "It was the engine block that failed me. It was a large engine.";
        String text2 = "The engine was destroyed.";
        ChunkStore store = new ChunkStore();
        Chunker.chunk(store, 1, text);
        Chunker.chunk(store, 0, text2);
        Iterator<Chunk> iter = store.getChunks();
        while (iter.hasNext()){
            Chunk c = iter.next();
            System.out.println("  ============  ");
            System.out.println();
            System.out.println(c.getWords());
            System.out.println(c.appearsInDocs());
            System.out.println(c.appearsInSents(0));
            System.out.println(c.appearsInSents(1));
        }
    }*/
}
