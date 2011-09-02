package see.parser.config;

/**
 * Abstract factory for creating different Number instances
 */
public interface NumberFactory {
    Number getNumber(String string);
    Number getNumber(Number number);
    Character getDecimalSeparator();
}
