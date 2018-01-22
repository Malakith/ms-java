package funs;

import funs.controller.data.ChunkExtractor;
import funs.model.corpus.Chunk;
import funs.model.corpus.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChunkExtractorTest {
    @Test void compareToTest(){
        Chunk c1 = new Chunk("car");
        Chunk c2 = new Chunk("car");
        c1.setHeadWord("car");
        c2.setHeadWord("car");
        c1.setPosTag("NN");
        c2.setPosTag("NN");
        c1.setNerTag("O");
        c2.setNerTag("O");

        assertEquals(c1, c2);

        assertEquals(0, c1.compareTo(c2));

    }

    @Test
    public void chunkExtractionTest(){
        Document doc = new Document("www.funs.dk");
        doc.setText("The red car was driving very fast, compared to the blue car. " +
                "I got a red car for my birthday. " +
                "I wonder when the cleaning lady will be coming. " +
                "Do you know how old Bill Clinton is? " +
                "My birthday is on the 26th of October. " +
                "Jeremy left for work at 6 am.");
        ChunkExtractor.extract(doc);
        for (Chunk c : doc.getChunks()) {
            System.out.println("=====================");
            System.out.println(String.join(" ", c.getWords()));
            System.out.println(c.getHeadWord());
            System.out.println(c.getNerTag());
            System.out.println(c.getFrequency());
            System.out.println();
        }
    }
}
