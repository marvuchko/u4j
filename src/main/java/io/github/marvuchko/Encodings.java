package io.github.marvuchko;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;

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
     * Stores the last generated timestamp to check for conflicts during ULID generation.
     */
    private static long lastTimestamp;

    /**
     * Stores the last generated random value to check for conflicts during ULID generation.
     */
    private static byte[] lastRandom;

    static {
        lastTimestamp = Instant.now().toEpochMilli();
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
        var result = new byte[Constants.TIMESTAMP_SIZE];

        for (int step = Constants.MAXIMUM_STEPS - 1; step >= Constants.FIRST_INDEX; --step) {
            result[step] = Constants.ALPHABET[(int) (timestamp & Constants.FIVE_BITS_MASK)];
            timestamp >>>= Constants.TIMESTAMP_OFFSET_IN_BITS;
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
        if (ulidValue == null || ulidValue.length != Constants.ULID_LENGTH) {
            throw new IllegalArgumentException("Invalid ULID. It must be 26 characters long.");
        }

        long timestampValue = 0;

        for (int step = Constants.FIRST_INDEX; step < Constants.MAXIMUM_STEPS; ++step) {
            var ulidChar = ulidValue[step];
            var value = Arrays.binarySearch(Constants.ALPHABET, ulidChar);
            timestampValue = (timestampValue << Constants.MAXIMUM_STEPS / 2) | value;
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
        var result = new byte[Constants.RANDOM_SIZE];
        RANDOM.nextBytes(result);

        final int randomSize = Constants.RANDOM_SIZE;
        final int maximumSteps = Constants.MAXIMUM_STEPS;
        final int fiveBitsMask = Constants.FIVE_BITS_MASK;

        int index = Constants.FIRST_INDEX;

        for (int step = index; step < maximumSteps; ++step) {
            int byteVal = result[step] & Constants.INTEGER_MASK;
            if (step < maximumSteps / 2) {
                result[(index++) % randomSize] = Constants.ALPHABET[(byteVal >>> 3) & fiveBitsMask];
            } else {
                result[(index++) % randomSize] = Constants.ALPHABET[(byteVal >>> 5) & fiveBitsMask];
            }
            result[(index++) % randomSize] = Constants.ALPHABET[byteVal & fiveBitsMask];
        }

        if (hasConflict(timestamp, result)) {
            synchronized (Encodings.class) {
                result = encodeRandom(timestamp);
            }
        }

        lastRandom = result;
        lastTimestamp = timestamp;

        return result;
    }

    /**
     * Checks if the generated random component of the ULID conflicts with the last generated one.
     * Conflicts occur if the timestamp and random component are identical to the previous one.
     *
     * @param timestamp the current timestamp in milliseconds.
     * @param result    the generated random value to check for conflicts.
     * @return {@code true} if a conflict exists, otherwise {@code false}.
     */
    private static boolean hasConflict(long timestamp, byte[] result) {
        return lastTimestamp == timestamp && Arrays.equals(lastRandom, result);
    }

    /**
     * Concatenates the encoded timestamp and random component to form the full ULID byte array.
     *
     * @param timestamp the encoded timestamp portion of the ULID.
     * @param random    the encoded random portion of the ULID.
     * @return a byte array representing the full ULID.
     */
    private static byte[] concat(byte[] timestamp, byte[] random) {
        var result = Arrays.copyOf(timestamp, timestamp.length + random.length);
        System.arraycopy(random, Constants.FIRST_INDEX, result, timestamp.length, random.length);
        return result;
    }

}
