package see.evaluator;

import java.math.BigDecimal;

public class BigDecimalFactory implements NumberFactory {
    @Override
    public BigDecimal getNumber(String string) {
        return new BigDecimal(string);
    }

    @Override
    public BigDecimal getNumber(Number number) {
        if (number instanceof BigDecimal) {
            return (BigDecimal) number;
        } else {
            return BigDecimal.valueOf(number.doubleValue());
        }
    }
}