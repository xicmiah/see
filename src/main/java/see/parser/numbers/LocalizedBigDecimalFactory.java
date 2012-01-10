package see.parser.numbers;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

public class LocalizedBigDecimalFactory extends BigDecimalFactory {
    private final Locale locale;

    public LocalizedBigDecimalFactory(Locale locale) {
        this.locale = locale;
    }

    public LocalizedBigDecimalFactory(MathContext mathContext, Locale locale) {
        super(mathContext);
        this.locale = locale;
    }

    @Override
    public BigDecimal getNumber(String string) {
        DecimalFormat format = (DecimalFormat) DecimalFormat.getNumberInstance(locale);
        format.setParseBigDecimal(true);
        try {
            return (BigDecimal) format.parse(string);
        } catch (ParseException e) {
            throw new RuntimeException("Couldn't parse " + string, e);
        }
    }

    @Override
    public Character getDecimalSeparator() {
        return ((DecimalFormat) DecimalFormat.getNumberInstance(locale)).getDecimalFormatSymbols().getDecimalSeparator();
    }
}
