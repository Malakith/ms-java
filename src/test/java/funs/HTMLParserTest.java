package funs;


import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import funs.acquisition.HTMLParser;
import funs.model.HTMLTag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;


public class HTMLParserTest {
    @Test
    public void testInstantiation(){
        String html = "";
        HTMLParser hp = new HTMLParser();
        HTMLParser.ParseResult p = hp.parseHtml(html);
        assertEquals("", p.getText());
        assertEquals(0, p.getTags().size());
    }

    @Test
    public void testOnlyText(){
        String html = "test";
        HTMLParser hp = new HTMLParser();
        HTMLParser.ParseResult p = hp.parseHtml(html);
        assertEquals(html, p.getText());
        assertEquals(0, p.getTags().size());
    }

    @Test
    public void testOneParagraph(){
        String html = "<p>test</p>";
        HTMLParser hp = new HTMLParser();
        HTMLParser.ParseResult p = hp.parseHtml(html);
        assertEquals("test.", p.getText());
        assertEquals(1, p.getTags().size());
        HTMLTag tag = p.getTags().get(0);
        assertEquals("p", tag.getName());
        assertEquals(0, tag.getStart());
        assertEquals(5, tag.getEnd());
    }

    @Test
    public void testTwoParagraphs(){
        String html = "<p>test1</p><p>test2</p>";
        HTMLParser hp = new HTMLParser();
        HTMLParser.ParseResult p = hp.parseHtml(html);
        //assertEquals("test1. test2.", p.getText());
        assertEquals(2, p.getTags().size());
        System.out.println(p.getText());
        HTMLTag tag1 = p.getTags().get(0);
        assertEquals("p", tag1.getName());
        assertEquals(0, tag1.getStart());
        assertEquals(7, tag1.getEnd());
        HTMLTag tag2 = p.getTags().get(1);
        assertEquals("p", tag2.getName());
        assertEquals(7, tag2.getStart());
        assertEquals(13, tag2.getEnd());
    }
/*
/
    @Test
    public void testThreeParagraphs(){
        String html = "<p>test1</p><p>test2</p><p>test3</p>";
        HTMLParser hp = new HTMLParser();
        HTMLParser.ParseResult p = hp.parseHtml(html);
        assertEquals("test1. test2. test3.", p.getText());
        assertEquals(3, p.getTags().size());
        HTMLParser.Tag tag1 = p.getTags().get(0);
        assertEquals("p", tag1.getTag());
        assertEquals(0, tag1.index());
        assertEquals(6, tag1.length());
        HTMLParser.Tag tag2 = p.getTags().get(1);
        assertEquals("p", tag2.getTag());
        assertEquals(6, tag2.index());
        assertEquals(6, tag2.length());
        HTMLParser.Tag tag3 = p.getTags().get(2);
        assertEquals("p", tag3.getTag());
        assertEquals(12, tag3.index());
        assertEquals(6, tag3.length());
    }*/

    @Test
    public void testParagraphInDiv(){
        String html = "<div><p>Testing something</p></div>";
        HTMLParser p = new HTMLParser();
        p.parseHtml(html);
    }

    @Test
    public void testHtmlStuff() throws IOException {
        String html = String.join("\n", Files.readAllLines(Paths.get("C:\\Users\\Morten\\IdeaProjects\\learner\\src\\test\\java\\funs\\htmltest.html")));
        HTMLParser hp = new HTMLParser();
        HTMLParser.ParseResult p = hp.parseHtml(html);
        System.out.println(p.getText());
    }

    @Test
    public void testHtmlStuff2() throws IOException {
        String html = String.join("\n", Files.readAllLines(Paths.get("C:\\Users\\Morten\\IdeaProjects\\learner\\src\\test\\java\\funs\\htmltest.html")));
        Document doc = new Document(html);
        List<Sentence> sents = doc.sentences();
        for (String word : sents.get(0).words()){
            System.out.println(word);
        }
    }


}
