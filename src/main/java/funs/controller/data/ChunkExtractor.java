package funs.controller.data;

import edu.stanford.nlp.ie.machinereading.structure.Span;
import edu.stanford.nlp.simple.Sentence;
import funs.model.corpus.Document;
import funs.model.corpus.Chunk;
import funs.model.corpus.Corpus;
import funs.util.Singleton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class ChunkExtractor {

    public static void extractFromCorpus() {
        Corpus corp = Singleton.getInstance();

        List<Document> docs = corp.getDocuments();
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Queue<Future<List<Chunk>>> futures = new ConcurrentLinkedQueue<Future<List<Chunk>>>();

        for (Document doc : docs) {
            futures.add(executor.submit(() -> extract(doc)));
        }
        int i = 0;
        List<Chunk> chunkList = new LinkedList<>();
        for (Future<List<Chunk>> f : futures){
            try {
                List<Chunk> chunks = f.get();
                chunkList.addAll(chunks);
                i++;
                System.out.println("Finished with " + i + " of " + docs.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        for(Chunk chunk : chunkList){
            corp.addChunk(chunk);
        }
        System.out.println("Done extracting chunks.");
    }

    public static List<Chunk> extract(Document doc){
        edu.stanford.nlp.simple.Document nlp = new edu.stanford.nlp.simple.Document(doc.getText());
        List<Chunk> chunks = new LinkedList<>();
        for (Sentence sent : nlp.sentences()){
            List<Chunk> newChunks = extract(sent);
            chunks = Chunk.mergeLists(chunks, newChunks);
        }
        for (Chunk c : chunks){
            c.addDocument(doc);
            doc.addChunk(c);
        }
        return chunks;
    }

    private static List<Chunk> extract(Sentence sentence){
        List<Chunk> chunks = new LinkedList<>();
        for (int i = 0; i < sentence.length();){
            if (sentence.posTag(i).startsWith("NN")) {
                int j = i+1;
                while (j < sentence.length() && sentence.posTag(j).startsWith("NN")){
                    j++;
                }
                chunks.addAll(createNounChunks(sentence, i, j));
                i = j;
            } else if (sentence.posTag(i).startsWith("JJ")){
                chunks.add(createAdjChunk(sentence, i));
                i++;
            } else if (sentence.posTag(i).startsWith("CD")) {
                int j = i+1;
                while (j < sentence.length() && sentence.posTag(j).startsWith("CD")){
                    j++;
                }
                chunks.add(createCDChunk(sentence, i, j));
                i = j;
            }else {
                i++;
            }
        }
        return chunks;
    }

    private static List<Chunk> createNounChunks(Sentence sentence, int i, int j){

        List<Chunk> chunks = new LinkedList<>();
        while (i < j) {
            Chunk chunk = new Chunk(sentence.words().subList(i,j));
            chunk.addSentence(sentence);
            chunk.setPosTag("NN");
            Span loc = new Span(i,j);
            int headLoc = sentence.algorithms().headOfSpan(loc);
            chunk.setHeadWord(sentence.word(headLoc));
            chunk.setNerTag(sentence.nerTag(i));
            chunks.add(chunk);
            i++;
        }
        return chunks;
    }

    private static Chunk createAdjChunk(Sentence sentence, int i){
        Chunk chunk = new Chunk(sentence.word(i));
        chunk.addSentence(sentence);
        chunk.setPosTag("JJ");
        Span loc = new Span(i,i+1);
        int headLoc = sentence.algorithms().headOfSpan(loc);
        chunk.setHeadWord(sentence.word(headLoc));
        chunk.setNerTag(sentence.nerTag(i));
        return chunk;
    }

    private static Chunk createCDChunk(Sentence sentence, int i, int j){
        Chunk chunk = new Chunk(sentence.words().subList(i, j));
        chunk.addSentence(sentence);
        chunk.setPosTag("CD");
        Span loc = new Span(i,j);
        int headLoc = sentence.algorithms().headOfSpan(loc);
        chunk.setHeadWord(sentence.word(headLoc));
        chunk.setNerTag(sentence.nerTag(i));
        return chunk;
    }
}
