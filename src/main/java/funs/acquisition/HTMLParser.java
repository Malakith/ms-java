package funs.acquisition;

import com.google.common.collect.Lists;
import edu.stanford.nlp.util.HasInterval;
import edu.stanford.nlp.util.Interval;
import edu.stanford.nlp.util.IntervalTree;
import funs.model.HTMLTag;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.NodeVisitor;

import java.util.*;

public class HTMLParser {

    private String tagsIncluded = "title|h[1-6]|p|b|i|em|cite|blockquote|q|u|a|div|span|abbr|address|bdi|dfn|mark|meter|small|" +
            "strong|select|optgroup|option|label|fieldset|legend|datalist|output|" +
            "ul|ol|li|dl|dt|dd|menu|table|caption|th|tr|td|thead|tbody|tfoot|main|section|article|aside|details|dialog|summary";

    private String sentenceEnding = "title|h[1-6]|p|cite|blockquote|q|div|span|select|optgroup|option|label|fieldset|legend|" +
            "datalist|output|ul|ol|li|dl|dt|dd|menu|table|caption|th|tr|td|thead|tbody|tfoot|main|section|article|aside|details|dialog|summary";

    private String tagsIgnored = "code|del|kbd|s|samp|wbr|col|colgroup|style|base|basefont|script|noscript|applet|embed|object|param|data"+
            "|audio|video|track|source|frame|iframe|frameset|noframes";

    private int unique = 0;

    public ParseResult parseHtml(String html){
        Document doc = Jsoup.parse(html);
        MyVisitor visitor = new MyVisitor();
        doc.traverse(visitor);
        return new ParseResult(visitor.getTags(), visitor.getText());
    }

    public class ParseResult{
        private List<HTMLTag> tags;
        private String text;

        public ParseResult(List<HTMLTag> tags, String text){
            this.tags = tags;
            this.text = text;
        }

        public String getText(){
            return text;
        }

        public List<HTMLTag> getTags(){
            return tags;
        }
    }


    private class MyVisitor implements NodeVisitor{

        String sentenceEnding = "title|h[1-6]|p|cite|blockquote|q|div|span|select|optgroup|option|label|fieldset|legend|" +
                "datalist|output|ul|ol|li|dl|dt|dd|menu|table|caption|th|tr|td|thead|tbody|tfoot|main|section|article|aside|details|dialog|summary";

        String tagsIncluded = "title|h[1-6]|p|b|i|em|cite|blockquote|q|u|a|div|span|abbr|address|bdi|dfn|mark|meter|small|" +
                "strong|select|optgroup|option|label|fieldset|legend|datalist|output|" +
                "ul|ol|li|dl|dt|dd|menu|table|caption|th|tr|td|thead|tbody|tfoot|main|section|article|aside|details|dialog|summary";

        String text = "";

        Map<Node, Integer> nodeMap;
        List<HTMLTag> tags;

        boolean ignore = false;
        Node ignorer = null;

        public MyVisitor(){
            nodeMap = new HashMap<>();
            tags = new LinkedList<>();
        }

        public String getText() {
            return text;
        }

        public List<HTMLTag> getTags() {
            return tags;
        }

        @Override
        public void head(Node node, int i) {
            if (ignore) {
                return;
            }
            if (node instanceof TextNode) {
                TextNode tn = (TextNode) node;
                text = text.trim() + " " + (tn.getWholeText()).trim();
            } else if (node instanceof Element){
                String name = ((Element) node).tagName();
                if (name.matches(tagsIncluded) ) {
                    int id = tags.size();
                    tags.add(new HTMLTag(id, text.length(), name));
                    nodeMap.put(node, id);
                }
            } else if (node instanceof Comment ||
                    node instanceof DataNode ||
                    node instanceof XmlDeclaration){
                ignorer = node;
                ignore = true;
            }

        }

        @Override
        public void tail(Node node, int i) {
            if (ignore){
                if (node == ignorer){
                    ignorer = null;
                    ignore = false;
                }
            } else if (node instanceof Element && nodeMap.containsKey(node)){
                HTMLTag tag = tags.get(nodeMap.get(node));
                if (!text.endsWith(".")) {
                    if (tag.getName().matches(sentenceEnding) || (text.length()-tag.getStart()) > 20) {
                        text += ".";
                    }
                }
                tag.end(text.length());
            }
        }
    }
}
