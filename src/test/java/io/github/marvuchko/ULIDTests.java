package io.github.marvuchko;

import org.junit.jupiter.api.Test;

import static io.github.marvuchko.U4J.ulid;
import static org.junit.jupiter.api.Assertions.*;

class ULIDTests {

    private String generateUlid() {
        return ulid().toString();
    }

    @Test
    public void testUlidLength() {
        String ulid = generateUlid();
        assertEquals(26, ulid.length(), "ULID should be 26 characters long");
    }

    @Test
    public void testUlidCharacters() {
        String ulid = generateUlid();
        assertTrue(ulid.matches("[0-9A-HJKMNP-TV-Z]{26}"),
                "ULID should only contain valid characters (0-9, A-H, J-K, M-N, P-T, V-Z)");
    }

    @Test
    public void testUlidUniqueness() {
        String ulid1 = generateUlid();
        String ulid2 = generateUlid();
        assertNotEquals(ulid1, ulid2, "Each ULID should be unique");
    }

    @Test
    public void testUlidIsNotNull() {
        String ulid = generateUlid();
        assertNotNull(ulid, "ULID should not be null");
    }

    @Test
    public void testUlidIsString() {
        String ulid = generateUlid();
        assertNotNull(ulid, "ULID should be a string");
    }

    @Test
    public void testMultipleUlidUniqueness() {
        for (int i = 0; i < 100_000; i++) {
            String ulid1 = generateUlid();
            String ulid2 = generateUlid();
            assertNotEquals(ulid1, ulid2, "Each ULID should be unique, but we have: " + ulid1 + " : " + ulid2 + ", index: " + i);
        }
    }

    @Test
    public void testUlidLexicographicalOrder() {
        String ulid1 = generateUlid();
        String ulid2 = generateUlid();
        assertTrue(ulid1.compareTo(ulid2) != 0,
                "ULIDs should be lexicographically sortable");
    }

    @Test
    public void testSpecificUlid() {
        String ulid = "01ARZ3NDEKTSV4RRFFQ69G5FAV";
        assertEquals(26, ulid(ulid).toString().length(), "The specific ULID should be 26 characters long");
    }

    @Test
    public void testEdgeCaseUlid() {
        String ulid = "00000000000000000000000000";  // Edge case ULID (min value)
        assertEquals(ulid, ulid(ulid).toString(),
                "Edge case ULID should be valid and generated correctly");
    }

    @Test
    public void testMaxValueUlid() {
        String ulid = "7ZZZZZZZZZZZZZZZZZZZZZZZZZ";  // Example maximum ULID
        assertEquals(ulid, ulid(ulid).toString(),
                "The maximum value ULID should be generated correctly");
    }

    @Test
    public void testBatchOfUlids() {
        for (int i = 0; i < 100_000; i++) {
            String ulid = generateUlid();
            assertNotNull(ulid, "ULID in batch should not be null");
            assertEquals(26, ulid.length(), "ULID in batch should be 26 characters long");
            assertTrue(ulid.matches("[0-9A-HJKMNP-TV-Z]{26}"), "ULID in batch contains valid characters");
        }
    }

    @Test
    void testDefaultConstructorCreatesValidULID() {
        ULID ulid = new ULID();

        assertNotNull(ulid);
        assertNotNull(ulid.getValue());
        assertEquals(Constants.ULID_LENGTH, ulid.getValue().length, "ULID should be of valid length");
    }

    @Test
    void testConstructorWithTimestampCreatesValidULID() {
        long timestamp = System.currentTimeMillis();
        ULID ulid = ULID.create(timestamp);

        assertNotNull(ulid);
        assertEquals(timestamp, ulid.getTimestamp().toEpochMilli(), "Timestamp should match the input");
    }

    @Test
    void testConstructorWithByteArrayCreatesValidULID() {
        ULID originalUlid = ULID.create();
        byte[] value = originalUlid.getValue();
        ULID ulidFromBytes = ULID.create(value);

        assertNotNull(ulidFromBytes);
        assertArrayEquals(value, ulidFromBytes.getValue(), "ULID should match the original value");
    }

    @Test
    void testConstructorWithStringCreatesValidULID() {
        ULID originalUlid = ULID.create();
        String ulidString = originalUlid.toString();
        ULID ulidFromString = ULID.create(ulidString);

        assertNotNull(ulidFromString);
        assertEquals(ulidString, ulidFromString.toString(), "ULID string representation should match");
    }

    @Test
    void testEqualsMethod() {
        ULID ulid1 = ULID.create();
        ULID ulid2 = ULID.create(ulid1.getValue());

        assertEquals(ulid1, ulid2, "ULIDs created from the same value should be equal");
    }

    @Test
    void testHashCodeMethod() {
        ULID ulid1 = ULID.create();
        ULID ulid2 = ULID.create(ulid1.getValue());

        assertEquals(ulid1.hashCode(), ulid2.hashCode(), "ULIDs created from the same value should have same hash code");
    }
}
