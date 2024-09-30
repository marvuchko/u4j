package io.github.marvuchko;

/**
 * Utility class for generating and managing ULIDs.
 * <p>
 * The {@code U4J} class provides static methods to create new ULIDs or manage existing ones.
 * It acts as a convenience wrapper for the {@link ULID} class, allowing for ULID generation
 * using various input parameters such as current timestamp, a specific timestamp, byte arrays, or strings.
 * </p>
 * <p>
 * This class is final and cannot be instantiated.
 * </p>
 *
 * @author Marko Vučković
 */
public final class U4J {

    /**
     * Private constructor to prevent instantiation.
     * This utility class contains only static methods and is not meant to be instantiated.
     */
    private U4J() {
        super();
    }

    /**
     * Generates a new ULID based on the current timestamp.
     * <p>
     * This method creates a ULID using the current system time in milliseconds since the Unix epoch.
     * </p>
     *
     * @return a new {@link ULID} instance representing a unique ULID.
     */
    public static ULID ulid() {
        return ULID.create();
    }

    /**
     * Generates a new ULID based on the provided timestamp.
     * <p>
     * This method creates a ULID using the specified timestamp in milliseconds since the Unix epoch.
     * </p>
     *
     * @param timestamp a long value representing the timestamp in milliseconds since Unix epoch.
     * @return a new {@link ULID} instance corresponding to the provided timestamp.
     */
    public static ULID ulid(long timestamp) {
        return ULID.create(timestamp);
    }

    /**
     * Creates a new ULID from an existing string.
     * <p>
     * This method takes a string representation of an existing ULID and converts it into a {@link ULID} instance.
     * </p>
     *
     * @param existingULID a {@code String} representing an existing ULID.
     * @return a new {@link ULID} instance corresponding to the provided string.
     */
    public static ULID ulid(String existingULID) {
        return ULID.create(existingULID);
    }
}
