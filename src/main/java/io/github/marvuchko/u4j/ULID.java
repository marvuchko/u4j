package io.github.marvuchko.u4j;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

import static io.github.marvuchko.u4j.Encodings.decodeTimestamp;
import static io.github.marvuchko.u4j.Encodings.encodeAndGet;
import static io.github.marvuchko.u4j.Constants.*;

/**
 * Represents a ULID (Universally Unique Lexicographically Sortable Identifier),
 * which is a 26-character string composed of a timestamp and a random component.
 * <p>
 * A ULID is 128 bits long: the first 48 bits represent the timestamp and the remaining 80 bits are random.
 * </p>
 * <p>
 * This class implements {@code Comparable<ULID>}.
 * </p>
 *
 * @author Marko Vučković
 * @see <a href="https://github.com/ulid/spec">ULID Specification</a>
 */
public final class ULID implements Comparable<ULID> {

    /**
     * Encoded ULID value represented as a string.
     */
    private final String value;

    /**
     * Private constructor that initializes a ULID from the given byte array.
     * <p>
     * The byte array must represent a valid 26-character ULID.
     * </p>
     *
     * @param value a byte array representing the ULID.
     */
    private ULID(byte[] value) {
        this.value = new String(value, StandardCharsets.UTF_8);
    }

    /**
     * Private constructor that initializes a ULID from the given string.
     * <p>
     * The string must represent a valid 26-character ULID.
     * </p>
     *
     * @param value a string representing the ULID.
     */
    private ULID(String value) {
        this.value = value;
    }

    /**
     * Creates a new ULID based on the current timestamp.
     *
     * @return a new {@code ULID} instance.
     */
    static ULID create() {
        return create(Instant.now().toEpochMilli());
    }

    /**
     * Creates a new ULID based on the specified timestamp.
     *
     * @param timestamp a long value representing the timestamp in milliseconds since Unix epoch.
     * @return a new {@code ULID} instance.
     */
    static ULID create(long timestamp) {
        var bytes = encodeAndGet(timestamp);
        return new ULID(bytes);
    }

    /**
     * Creates a new ULID from an existing ULID string.
     * <p>
     * The string must represent a valid 26-character ULID.
     * </p>
     *
     * @param existingULID a string representing the ULID.
     * @return a new {@code ULID} instance.
     * @throws IllegalArgumentException if the provided string is null or not of valid ULID length.
     */
    static ULID create(String existingULID) {
        if (isValid(existingULID)) {
            return new ULID(existingULID.toUpperCase());
        }
        throw new IllegalArgumentException("Invalid ULID. It must be 26 characters long.");
    }

    /**
     * Validates if the provided ULID string conforms to the ULID format.
     * <p>
     * A valid ULID is a 26-character string consisting of characters from the ULID alphabet (0-9, A-H, J-K, M-N, P-Z).
     * This method uses a regular expression to check the validity.
     * </p>
     *
     * @param existingULID the ULID string to validate.
     * @return {@code true} if the string is a valid ULID, {@code false} otherwise.
     */
    public static boolean isValid(String existingULID) {
        if (existingULID == null || existingULID.length() != ULID_LENGTH) {
            return false;
        }
        return existingULID.toUpperCase().matches(VALID_ULID_REGEX);
    }

    /**
     * Returns the ULID as a string.
     *
     * @return the ULID as a {@code String}.
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns the timestamp component of the ULID.
     * The timestamp is extracted from the first 48 bits of the ULID and represents
     * the time in milliseconds since the Unix epoch.
     *
     * @return an {@code Instant} representing the timestamp of the ULID.
     */
    public Instant getTimestamp() {
        return decodeTimestamp(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Compares this ULID with another ULID.
     * <p>
     * The comparison is done lexicographically based on the character sequences.
     * </p>
     *
     * @param other the ULID to compare this ULID to.
     * @return a negative integer, zero, or a positive integer as this ULID is less than, equal to, or greater than the specified ULID.
     */
    @Override
    public int compareTo(ULID other) {
        return CharSequence.compare(value, other.value);
    }

    /**
     * Checks if this ULID is equal to another object.
     * <p>
     * Two ULIDs are considered equal if their string values are identical.
     * </p>
     *
     * @param other the object to compare to.
     * @return {@code true} if this ULID is equal to the other object, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (Objects.isNull(other)) {
            return false;
        }
        if (!Objects.equals(getClass(), other.getClass())) {
            return false;
        }
        ULID ulid = (ULID) other;
        return Objects.deepEquals(value, ulid.value);
    }

    /**
     * Returns the hash code value for this ULID.
     * <p>
     * The hash code is computed based on the byte array representation of the ULID.
     * </p>
     *
     * @return the hash code for this ULID.
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(value.getBytes());
    }

    /**
     * Returns the ULID as a string.
     *
     * @return the ULID as a {@code String}.
     */
    @Override
    public String toString() {
        return getValue();
    }
}
