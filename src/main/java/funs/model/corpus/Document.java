package funs.model.corpus;

import funs.acquisition.HTMLParser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Document {

    List<Chunk> chunks;
    Map<Chunk, Integer> frequency;
    String url;
    String html;
    String text;
    HTMLParser.ParseResult parse;
    boolean delete = false;

    public Document(String url){
        chunks = new LinkedList<>();
        frequency = new HashMap<>();
        this.url = url;
    }

    public void markForDeletion(){
        delete = true;
    }

    public boolean isMarkedForDeletion(){
        return delete;
    }

    public List<Chunk> getChunks() {
        return chunks;
    }

    public void addChunk(Chunk chunk) {
        this.chunks.add(chunk);
        this.frequency.put(chunk, this.frequency.getOrDefault(chunk, 0) +1);
    }

    public Integer getFrequency(Chunk chunk){
        return frequency.get(chunk);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public String getUrl() {
        return url;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public void setParse(HTMLParser.ParseResult parse) {
        this.text = parse.getText();
        this.parse = parse;
    }
}
