package io.github.marvuchko;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;

import static io.github.marvuchko.ULID.isValid;
import static io.github.marvuchko.Constants.*;
import static java.util.Arrays.copyOf;

/**
 * Utility class for encoding and decoding components of a ULID (Universally Unique Lexicographically Sortable Identifier).
 * It provides methods for encoding timestamps, generating random values, and decoding ULID timestamps.
 * <p>
 * The class uses a {@link SecureRandom} instance to generate random components of ULIDs.
 * It is designed to be thread-safe and ensures no conflicts between ULIDs generated at the same millisecond.
 * </p>
 * <p>
 * This class cannot be instantiated or extended, and all methods are static.
 * </p>
 */
final class Encodings {

    /**
     * The {@link SecureRandom} instance used to generate random byte arrays for ULIDs.
     */
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Stores the last generated timestamp to handle conflicts during ULID generation.
     */
    private static long lastTimestamp;

    /**
     * Stores the last generated random value to handle conflicts during ULID generation.
     */
    private static final byte[] lastRandom;

    static {
        lastTimestamp = Instant.now().toEpochMilli();
        lastRandom = new byte[RANDOM_SIZE];
        RANDOM.nextBytes(lastRandom);
    }

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static methods.
     */
    private Encodings() {
        super();
    }

    /**
     * Encodes the provided timestamp and generates the corresponding ULID byte array.
     * The ULID is a concatenation of the encoded timestamp and a randomly generated component.
     *
     * @param timestamp the timestamp in milliseconds since Unix epoch.
     * @return a byte array representing the encoded ULID.
     */
    static byte[] encodeAndGet(long timestamp) {
        return concat(encodeTimestamp(timestamp), encodeRandom(timestamp));
    }

    /**
     * Encodes the provided timestamp into a byte array.
     * The timestamp is encoded into the first part of the ULID.
     *
     * @param timestamp the timestamp in milliseconds since Unix epoch.
     * @return a byte array representing the encoded timestamp portion of the ULID.
     */
    private static byte[] encodeTimestamp(long timestamp) {
        var result = new byte[TIMESTAMP_SIZE];

        for (int step = MAXIMUM_STEPS - 1; step >= FIRST_INDEX; --step) {
            result[step] = ALPHABET[(int) (timestamp & FIVE_BITS_MASK)];
            timestamp >>>= TIMESTAMP_OFFSET_IN_BITS;
        }

        return result;
    }

    /**
     * Decodes the timestamp from a ULID byte array.
     * The timestamp portion is extracted from the first part of the ULID byte array.
     *
     * @param ulidValue a ULID byte array.
     * @return the decoded timestamp as an {@link Instant}.
     * @throws IllegalArgumentException if the ULID is invalid (must be 26 characters long).
     */
    static Instant decodeTimestamp(byte[] ulidValue) {
        if (!isValid(ulidValue)) {
            throw new IllegalArgumentException("Invalid ULID. It must be 26 characters long.");
        }

        long timestampValue = 0;

        for (int step = FIRST_INDEX; step < MAXIMUM_STEPS; ++step) {
            var ulidChar = ulidValue[step];
            var value = Arrays.binarySearch(ALPHABET, ulidChar);
            timestampValue = (timestampValue << TIMESTAMP_OFFSET_IN_BITS) | value;
        }

        return Instant.ofEpochMilli(timestampValue);
    }

    /**
     * Generates the random portion of the ULID. The random value is appended to the timestamp component.
     * <p>
     * If the same timestamp occurs (e.g., two ULIDs generated in the same millisecond), this method ensures
     * that a unique random value is generated to avoid conflicts.
     * </p>
     *
     * @param timestamp the timestamp used to check for conflicts in the random value.
     * @return a byte array representing the random portion of the ULID.
     */
    private static byte[] encodeRandom(long timestamp) {
        var randomBytes = getRandomBytes(timestamp);
        var result = new byte[RANDOM_SIZE];

        for (int index = FIRST_INDEX; index < RANDOM_SIZE; ++index) {
            result[index] = ALPHABET[randomBytes[index] & FIVE_BITS_MASK];
        }

        return result;
    }

    /**
     * Generates a random byte array for use in ULID generation. This method ensures
     * that if two ULIDs are generated with the same timestamp, the lower bits of the
     * random component are incremented to avoid collisions. The method is synchronized
     * to ensure thread safety when checking and modifying the last generated random value.
     *
     * <p>If a conflict with the previous timestamp is detected (i.e., the method is called
     * with the same timestamp as the last generated ULID), the random bytes are incremented
     * to avoid duplication. Specifically, the lower bits are incremented. If they overflow,
     * the higher bits are incremented as well.</p>
     *
     * @param timestamp the current timestamp in milliseconds to check for conflicts.
     * @return a byte array containing the random component of the ULID.
     */
    private static byte[] getRandomBytes(long timestamp) {
        if (!hasConflict(timestamp)) {
            RANDOM.nextBytes(lastRandom);
        } else {
            incrementLastRandom();
        }
        lastTimestamp = timestamp;
        return lastRandom;
    }

    /**
     * Increments the last generated random byte array to handle conflicts during ULID generation.
     * <p>
     * When the lower bits of the random value overflow, the higher bits are incremented.
     * </p>
     */
    private synchronized static void incrementLastRandom() {
        for (int index = RANDOM_SIZE - 1; index >= FIRST_INDEX; --index) {
            int byteValue = lastRandom[index] & FIVE_BITS_MASK;
            ++byteValue;
            if (byteValue < FIVE_BITS_MASK) {
                lastRandom[index] = (byte) byteValue;
                break;
            }
            lastRandom[index] = 0;
        }
    }

    /**
     * Checks if the generated random component of the ULID conflicts with the last generated one.
     * Conflicts occur if the timestamp component is identical to the previous one.
     *
     * @param timestamp the current timestamp in milliseconds.
     * @return {@code true} if a conflict exists, otherwise {@code false}.
     */
    private static boolean hasConflict(long timestamp) {
        return lastTimestamp == timestamp;
    }

    /**
     * Concatenates the encoded timestamp and random component to form the full ULID byte array.
     *
     * @param timestamp the encoded timestamp portion of the ULID.
     * @param random    the encoded random portion of the ULID.
     * @return a byte array representing the full ULID.
     */
    private static byte[] concat(byte[] timestamp, byte[] random) {
        var result = copyOf(timestamp, timestamp.length + random.length);
        System.arraycopy(random, FIRST_INDEX, result, timestamp.length, random.length);
        return result;
    }

}
