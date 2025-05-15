package io.github.marvuchko.u4j;

import org.junit.jupiter.api.Test;

import static io.github.marvuchko.u4j.U4J.ulid;
import static org.junit.jupiter.api.Assertions.*;

class U4JTests {

    @Test
    void testUlidCreatesNewInstance() {
        ULID ulid1 = ulid();
        ULID ulid2 = ulid();

        assertNotNull(ulid1);
        assertNotNull(ulid2);
        assertNotEquals(ulid1.toString(), ulid2.toString(), "ULIDs should be unique");
    }

    @Test
    void testUlidWithTimestampCreatesInstance() {
        long timestamp = System.currentTimeMillis();
        ULID ulid = ulid(timestamp);

        assertNotNull(ulid);
        assertEquals(timestamp, ulid.getTimestamp().toEpochMilli(), "Timestamp should match the input");
    }

    @Test
    void testUlidWithStringCreatesInstance() {
        ULID originalUlid = ulid();
        String ulidString = originalUlid.toString();
        ULID ulidFromString = ulid(ulidString);

        assertNotNull(ulidFromString);
        assertEquals(ulidString, ulidFromString.toString(), "ULID string representation should match");
    }
}
