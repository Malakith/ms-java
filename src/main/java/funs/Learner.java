package funs;

import funs.acquisition.HtmlCollector;
import funs.controller.data.ChunkExtractor;
import funs.controller.data.CorpusGenerator;
import funs.model.corpus.Chunk;
import funs.model.corpus.Corpus;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;


public class Learner
{
    private static final String STRING_ARRAY_SAMPLE = "data1.csv";
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        Class.forName("org.sqlite.JDBC");
        CallCache<Corpus> cache = new CallCache<>("corpus_cache");
        String concept = "Car";
        String[] attributes = {"Engine", "Colour", "Brand", "Mileage"};
        String id = concept+","+ String.join(",", attributes);
        Corpus corp = CorpusGenerator.generateCorpus(concept, attributes, 5);
        HtmlCollector.getHtml();
        System.out.println("Gathered " + corp.getDocuments().size() + " documents.");

        System.out.println("Extracting chunks");
        ChunkExtractor.extractFromCorpus();

        Writer writer = null;


        String[] headerRecord = {"headword", "words", "ner", "docSize", "sentSize"};
        List<Chunk> car = new ArrayList();
        List<Chunk> engine = new ArrayList();
        List<Chunk> colour = new ArrayList();
        List<Chunk> brand = new ArrayList();
        List<Chunk> mileage = new ArrayList();
        for(Chunk c : corp.getChunks()){
            if (c.getHeadWord().equals("car") || c.getHeadWord().equals("Car") ) {car.add(c);}
            if (c.getHeadWord().equals("engine") || c.getHeadWord().equals("Engine") ) {engine.add(c);}
            if (c.getHeadWord().equals("colour") || c.getHeadWord().equals("Colour") ) {colour.add(c);}
            if (c.getHeadWord().equals("brand") || c.getHeadWord().equals("Brand") ) {brand.add(c);}
            if (c.getHeadWord().equals("mileage") || c.getHeadWord().equals("Mileage") ) {mileage.add(c);}
        }
        Collections.sort(car);
        Collections.sort(engine);
        Collections.sort(colour);
        Collections.sort(brand);
        Collections.sort(mileage);
        writer = new PrintWriter("car.txt", "UTF-8");
        writer.write("Car, " + car.size() + "\n");
        for (Chunk c : car) {
            writer.write(c.getHeadWord() + "\t\t" + c.getNerTag() + "\t\t" + c.getSentences().size() + "\t\t" + c.getWords() + "\n");
        }
        writer.close();
        writer = new PrintWriter("engine.txt", "UTF-8");
        writer.write("engine, " + car.size() + "\n");
        for (Chunk c : engine) {
            writer.write(c.getHeadWord() + "\t\t" + c.getNerTag() + "\t\t" + c.getSentences().size() + "\t\t" + c.getWords() + "\n");
        }
        writer.close();
        writer = new PrintWriter("colour.txt", "UTF-8");
        writer.write("colour, " + car.size() + "\n");
        for (Chunk c : colour) {
            writer.write(c.getHeadWord() + "\t\t" + c.getNerTag() + "\t\t" + c.getSentences().size() + "\t\t" + c.getWords() + "\n");
        }
        writer.close();
        writer = new PrintWriter("brand.txt", "UTF-8");
        writer.write("brand, " + car.size() + "\n");
        for (Chunk c : brand) {
            writer.write(c.getHeadWord() + "\t\t" + c.getNerTag() + "\t\t" + c.getSentences().size() + "\t\t" + c.getWords() + "\n");
        }
        writer.close();
        writer = new PrintWriter("mileage.txt", "UTF-8");
        writer.write("mileage, " + car.size() + "\n");
        for (Chunk c : mileage) {
            writer.write(c.getHeadWord() + "\t\t" + c.getNerTag() + "\t\t" + c.getSentences().size() + "\t\t" + c.getWords() + "\n");
        }
        writer.close();





        writer.close();
            System.out.println("file closed.");
        System.out.println(corp.getDocuments().size());
        System.out.println(corp.getChunks().size());
        System.out.println("Done.");

    }
}
