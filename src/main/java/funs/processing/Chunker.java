package funs.processing;

import edu.stanford.nlp.ie.machinereading.structure.Span;
import edu.stanford.nlp.simple.*;
import funs.model.Chunk;
import funs.model.ChunkStore;

public class Chunker{

    private static String nounPattern = "";

    public static void chunk(ChunkStore store, funs.model.Document htmlDoc){
        String text = htmlDoc.getParseResult().getText();
        chunk(store, htmlDoc, text);
    }

    public static void chunk(ChunkStore store,funs.model.Document htmlDoc, String text) {
        int docId = htmlDoc.getId();

        Document doc = new Document(text);

        htmlDoc.setNLPDoc(doc);

        for (Sentence sent: doc.sentences()){
            for (int i = 0; i < sent.length(); i++){
                String pos = sent.posTag(i);
                if (pos.matches("NN|NNS|NNP|NNPS")){
                    int j = i;
                    while (sent.posTag(j).matches("NN|NNS|NNP|NNPS")){
                        Chunk c = store.createChunk(sent.word(j));
                        c.addLocation(docId, sent.sentenceIndex(), j);
                        c.setType("NN");
                        j++;
                    }
                    Chunk c = store.createChunk(sent.words().subList(i, j));
                    c.setHeadword(sent.word(sent.algorithms().headOfSpan(new Span(i,j))));
                    c.addLocation(docId, sent.sentenceIndex(), i);
                    c.setType("NN");
                } else if (pos.matches("JJ")){
                    Chunk c = store.createChunk(sent.word(i));
                    c.addLocation(docId, sent.sentenceIndex(), i);
                    c.setType("JJ");
                }
            }
        }
    }
}
