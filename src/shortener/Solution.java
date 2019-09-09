package shortener;

import shortener.strategy.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Solution {

    public static void main(String[] args) { //10000 test elements for each strategy to process, except for FileStorageStrategy class, the test of which generally takes much more time due to creating temp files as buckets
        HashMapStorageStrategy hashMapStorageStrategy = new HashMapStorageStrategy();
        testStrategy(hashMapStorageStrategy, 10000);
        OurHashMapStorageStrategy ourHashMapStorageStrategy = new OurHashMapStorageStrategy();
        testStrategy(ourHashMapStorageStrategy, 10000);
        FileStorageStrategy fileStorageStrategy = new FileStorageStrategy();
        testStrategy(fileStorageStrategy, 100);
        OurHashBiMapStorageStrategy ourHashBiMapStorageStrategy = new OurHashBiMapStorageStrategy();
        testStrategy(ourHashBiMapStorageStrategy, 10000);
        DualHashBidiMapStorageStrategy dualHashBidiMapStorageStrategy = new DualHashBidiMapStorageStrategy();
        testStrategy(dualHashBidiMapStorageStrategy, 10000);
        HashBiMapStorageStrategy hashBiMapStorageStrategy = new HashBiMapStorageStrategy();
        testStrategy(hashBiMapStorageStrategy, 10000);
    }

    public static Set<Long> getIds(Shortener shortener, Set<String> strings) {
        Set<Long> ids = new HashSet<>();
        for (String string : strings) {
            ids.add(shortener.getId(string));
        }
        return ids;
    }

    public static Set<String> getStrings(Shortener shortener, Set<Long> keys) {
        Set<String> strings = new HashSet<>();
        for (Long value : keys) {
            strings.add(shortener.getString(value));
        }
        return strings;
    }

    public static void testStrategy(StorageStrategy strategy, long elementsNumber) {
        Helper.printMessage(strategy.getClass().getSimpleName());

        HashSet<String> randomStrings = new HashSet<>();
        for (int i = 0; i < elementsNumber; i++)
            randomStrings.add(Helper.generateRandomString());

        Shortener shortener = new Shortener(strategy);

        Date beginTime = new Date();
        Set<Long> createdIds = getIds(shortener, randomStrings);
        Date endTime = new Date();
        String processingTime = String.valueOf(endTime.getTime() - beginTime.getTime());
        Helper.printMessage("Time of map keys obtaining(getIds method processing time for the current strategy): " + processingTime + "ms");

        beginTime = new Date();
        Set<String> existingRandomStrings = getStrings(shortener, createdIds);
        endTime = new Date();
        processingTime = String.valueOf(endTime.getTime() - beginTime.getTime());
        Helper.printMessage("Time of map values obtaining(getStrings method processing time for the current strategy): " + processingTime + "ms");

        System.out.print("Comparison of strategy content identity(Unaffected - passed/Affected - failed): ");
        if (randomStrings.equals(existingRandomStrings)) {
            Helper.printMessage("Test passed.\n");
        } else {
            Helper.printMessage("Test failed.\n");
        }
    }
}
