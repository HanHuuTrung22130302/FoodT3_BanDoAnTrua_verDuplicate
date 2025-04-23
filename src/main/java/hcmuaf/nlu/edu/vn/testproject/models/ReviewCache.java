package hcmuaf.nlu.edu.vn.testproject.models;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ReviewCache {
    // Map<foodId-option, List<ReviewFood>>
    private static final Map<String, List<ReviewFood>> cache = new ConcurrentHashMap<>();

    public static String makeKey(int foodId, int option) {
        return foodId + "-" + option;
    }

    public static List<ReviewFood> get(int foodId, int option) {
        return cache.get(makeKey(foodId, option));
    }

    public static void put(int foodId, int option, List<ReviewFood> reviews) {
        cache.put(makeKey(foodId, option), reviews);
    }

    public static boolean contains(int foodId, int option) {
        return cache.containsKey(makeKey(foodId, option));
    }

    public static void clear() {
        cache.clear();
    }
}
