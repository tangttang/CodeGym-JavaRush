package shortener.tests;

import org.junit.Assert;
import org.junit.Test;
import shortener.Helper;
import shortener.Shortener;
import shortener.strategy.HashBiMapStorageStrategy;
import shortener.strategy.HashMapStorageStrategy;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * JUnit speed test between JDK 8 HashMap and Google Guava BiMap
 */

public class SpeedTest {

    @Test
    public void testHashMapStorage() {
        Shortener hashMapStorageStrategy = new Shortener(new HashMapStorageStrategy());
        Shortener hashBiMapStorageStrategy = new Shortener(new HashBiMapStorageStrategy());

        Set<String> origStrings = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            origStrings.add(Helper.generateRandomString());
        }
        HashSet<Long> ids = new HashSet<>();

        long hashMapIdTime = getTimeToGetIds(hashMapStorageStrategy, origStrings, ids);
        long biHashMapIdTime = getTimeToGetIds(hashBiMapStorageStrategy, origStrings, new HashSet<>());
        Assert.assertTrue(hashMapIdTime > biHashMapIdTime);
        long hashMapValueTime = getTimeToGetStrings(hashMapStorageStrategy, ids, origStrings);
        long biHashMapValueTime = getTimeToGetStrings(hashBiMapStorageStrategy, ids, origStrings);
        Assert.assertEquals(hashMapValueTime, biHashMapValueTime, 30);

    }

    public long getTimeToGetIds(Shortener shortener, Set<String> strings, Set<Long> ids) {
        Date beginTime = new Date();
        for (String string : strings) {
            ids.add(shortener.getId(string));
        }
        Date endTime = new Date();
        return endTime.getTime() - beginTime.getTime();
    }

    public long getTimeToGetStrings(Shortener shortener, Set<Long> ids, Set<String> strings) {
        Date beginTime = new Date();
        for (Long id : ids) {
            strings.add(shortener.getString(id));
        }
        Date endTime = new Date();
        return endTime.getTime() - beginTime.getTime();
    }
}
