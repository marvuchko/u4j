package io.github.marvuchko;

import java.util.HashMap;
import java.util.Map;

import static io.github.marvuchko.U4J.ulid;

public class Main {
    private static final Map<String, Integer> ulidMap = new HashMap<>();

    public static void main(String[] args) {
        long timeMillis = System.currentTimeMillis();

        for (int i = 0; i < 1_000_000; ++i) {
            ULID ulid = ulid(timeMillis);
            String value = ulid.value();
            System.out.println(value);
            if (ulidMap.containsKey(value)) {
                ulidMap.replace(value, ulidMap.get(value) + 1);
            } else {
                ulidMap.put(value, 1);
            }
        }

        long now = System.currentTimeMillis();
        long count = ulidMap.entrySet().stream().filter(entry -> entry.getValue() > 1).count();

        System.out.println(count);
        System.out.printf("%nMillion ULIDs generated in %.4f%n", (now - timeMillis) / 1000f);
    }
}
