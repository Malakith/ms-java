package funs;

import funs.acquisition.Collector;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectorTest {
    @Test
    public void testCollectorGetAsync(){
        Set<String> urls = new HashSet<>();
        urls.add("http://www.google.com");
        urls.add("http://www.reddit.com");
        urls.add("http://www.funs.dk");
        Collector col = new Collector();
        List<String> bodies = col.getContentOfPages(urls);
        System.out.println(bodies.size());
        for (String b : bodies) {
            System.out.println(b);
            System.out.println("/////////");
        }
    }

    @Test
    public void testCollector() throws MalformedURLException {
        Collector col = new Collector();
        List<String> pages = col.getPages("Car", "Engine", "Colour");
        System.out.println(pages.size());
    }
}
