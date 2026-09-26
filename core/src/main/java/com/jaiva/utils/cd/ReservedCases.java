package com.jaiva.utils.cd;

/**
 * ReservedCases is an enumeration that defines special cases that do not follow
 * the standard context rules defined by {@link ContextDispatcher}.
 * These cases are not exceptions, but hard coded scenarios. This enum exists to remove the use of magic numbers for these cases.
 */
public enum ReservedCases {
//    EMPTY_STRING(0b10000), // 16. Commented out because this follows the standard rules.
    DOUBLE_QUOTES(0b10001), // 17. A string that starts and ends with double quotes.
    TERNARY(0b10010), // 18. A ternary operation.
    LAMBDA(0b10011); // 19. A lambda.

    /**
     * The code representing the reserved case.
     */
    final int code;

    ReservedCases(int code) {
        this.code = code;
    }

    /**
     * Retrieves the code associated with the reserved case.
     *
     * @return the code of the reserved case.
     */
    public int code() {
        return code;
    }
}
