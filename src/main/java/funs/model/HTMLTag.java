package funs.model;

public class HTMLTag {
    int id;
    int start;
    int end;
    String name;

    public HTMLTag(int id, int start, String name){
        this.id = id;
        this.start = start;
        this.name = name;
    }

    public void end(int end){
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
