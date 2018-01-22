package funs.model;

import funs.acquisition.HTMLParser;

public class Document {

    private int id;
    private String html;
    private HTMLParser.ParseResult parseResult;
    private edu.stanford.nlp.simple.Document nlpDoc;

    public Document(int id, String html){
        this.id = id;
        this.html = html;
    }

    public String getHtml() {
        return html;
    }

    public HTMLParser.ParseResult getParseResult() {
        return parseResult;
    }

    public int getId() {
        return id;
    }

    public void setNLPDoc(edu.stanford.nlp.simple.Document doc){
        nlpDoc = doc;
    }

    public edu.stanford.nlp.simple.Document getNLPDoc(){
        return nlpDoc;
    }

    public void setParseResult(HTMLParser.ParseResult parseResult) {
        this.parseResult = parseResult;
    }
}
