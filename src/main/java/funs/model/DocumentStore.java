package funs.model;


import java.util.ArrayList;
import java.util.List;

public class DocumentStore  {
    private List<Document> documents = new ArrayList<>();

    public Document newDocument(String html){
        synchronized (DocumentStore.class){
            int id = documents.size();
            Document doc = new Document(id, html);
            documents.add(doc);
            return doc;
        }
    }

    public Document getDocument(int id){
        return documents.get(id);
    }

    public List<Document> getDocuments() {
        return documents;
    }
}
