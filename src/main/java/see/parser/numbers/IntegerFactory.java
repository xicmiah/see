package see.parser.numbers;

public class IntegerFactory implements NumberFactory {
    @Override
    public Number getNumber(String string) {
        return Integer.valueOf(string);
    }

    @Override
    public Number getNumber(Number number) {
        return number.intValue();
    }

    @Override
    public Character getDecimalSeparator() {
        return '.';
    }
}
