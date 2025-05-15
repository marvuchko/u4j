package io.github.marvuchko.u4j;

/**
 * A utility class that defines constants used for encoding and decoding ULIDs (Universally Unique Lexicographically Sortable Identifiers).
 * <p>
 * These constants include the alphabet used for encoding, the size of the timestamp and random components, bit masks, and ULID length validation.
 * </p>
 * <p>
 * This class is designed to be uninstantiable and provides only static fields and constants.
 * </p>
 *
 * @author Marko Vučković
 */
final class Constants {

    /**
     * The alphabet used to encode ULID values.
     * It includes numbers and uppercase letters, omitting potentially confusing characters like 'I', 'L', 'O', and 'U'.
     * <p>
     * Characters: 0-9, A-H, J-K, M-N, P-Q, R-S, T-V, W-Z
     * </p>
     */
    static final byte[] ALPHABET = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q',
            'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     * The size of the timestamp portion of the ULID in bytes.
     * The timestamp is represented by 10 characters.
     */
    static final int TIMESTAMP_SIZE = 10;

    /**
     * The bit offset for the timestamp, which is 5 bits.
     * This value helps in shifting and encoding the timestamp during the ULID generation process.
     */
    static final long TIMESTAMP_OFFSET_IN_BITS = 5;

    /**
     * The size of the random portion of the ULID in bytes.
     * The random component is represented by 16 characters.
     */
    static final int RANDOM_SIZE = 16;

    /**
     * The starting index for various loops and array operations, typically 0.
     */
    static final int FIRST_INDEX = 0;

    /**
     * The number of maximum steps (or iterations) required for encoding the timestamp portion of the ULID.
     */
    static final int MAXIMUM_STEPS = 10;

    /**
     * A mask used to extract the last 5 bits of a byte.
     * This value is used during the encoding process, as each ULID character encodes 5 bits of information.
     */
    static final int FIVE_BITS_MASK = 0x1F;

    /**
     * The total length of a ULID, which is 26 characters (10 for the timestamp and 16 for the random component).
     */
    static final int ULID_LENGTH = 26;

    /**
     * A regular expression that validates a ULID string.
     * ULID strings must be exactly 26 characters long and consist of characters from the ULID alphabet (0-9, A-H, J-K, M-N, P-Z).
     */
    static final String VALID_ULID_REGEX = "[0-9A-HJKMNP-TV-Z]{26}";

    /**
     * Private constructor to prevent instantiation.
     * This class is a utility class and should not be instantiated.
     */
    private Constants() {
        super();
    }

}
