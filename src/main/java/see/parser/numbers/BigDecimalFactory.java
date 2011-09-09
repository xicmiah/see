package see.parser.numbers;

import java.math.BigDecimal;
import java.math.MathContext;

public class BigDecimalFactory implements NumberFactory {
    private final MathContext mathContext;

    public BigDecimalFactory() {
        this(MathContext.DECIMAL64);
    }

    public BigDecimalFactory(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    @Override
    public BigDecimal getNumber(String string) {
        return new BigDecimal(string, mathContext);
    }

    @Override
    public BigDecimal getNumber(Number number) {
        if (number instanceof BigDecimal) {
            return (BigDecimal) number;
        } else {
            return BigDecimal.valueOf(number.doubleValue());
        }
    }

    @Override
    public Character getDecimalSeparator() {
        return '.';
    }

    public MathContext getMathContext() {
        return mathContext;
    }
}
