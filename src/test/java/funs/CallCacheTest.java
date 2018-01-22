package funs;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CallCacheTest {

    @Test
    public void testSavingAndLoading() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        CallCache<String> cache = new CallCache<>();
        String id = "testing stuff";
        String data = "This is the data";
        cache.save(id, data);
        String saved = cache.load(id).get(0);
        assertEquals(data, saved);
        cache.save(id, "other data");
        System.out.println(cache.load(id).size());
        saved = cache.load(id).get(0);
        assertEquals("other data", saved);

    }

    @Test
    public void testWhenNotSaved() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        CallCache<String> cache = new CallCache<>();
        String id = "testingStuff";
        List<String> saved = cache.load(id);
        assertEquals(0, saved.size());
    }
}
