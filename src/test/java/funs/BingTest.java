package funs;

import funs.acquisition.Bing;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BingTest {
    @Test
    public void BingSeachTest(){
        Set<String> results = Bing.searchWeb("Horses", 150);
        //assertEquals(50, results.size() );
        System.out.println(results.size());
        for (String url : results) {
            System.out.println(url);
        }
    }
}
