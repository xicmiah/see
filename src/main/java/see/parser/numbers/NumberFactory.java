package see.parser.numbers;

/**
 * Abstract factory for creating different Number instances
 */
public interface NumberFactory {
    /**
     * Parse Number from string
     *
     * @param string input to parse
     * @return parsed number
     */
    Number getNumber(String string);

    /**
     * Convert supplied number to supported type
     *
     * @param number number to convert
     * @return Number instance of supported type
     */
    Number getNumber(Number number);

    /**
     * Get character for decimal point
     *
     * @return decimal separator
     */
    Character getDecimalSeparator();
}
