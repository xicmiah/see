package see.functions.arithmetic;

import com.google.common.base.Supplier;
import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Divide function for BigDecimals.
 */
public class Divide implements VarArgFunction<BigDecimal, BigDecimal> {

    private final Supplier<MathContext> mathContext;

    /**
     * Construct instance from by-name math context.
     * @param mathContext math context instance, passed by-name
     */
    public Divide(Supplier<MathContext> mathContext) {
        this.mathContext = mathContext;
    }

    @Override
    public BigDecimal apply(@Nonnull List<BigDecimal> input) {
        checkArgument(input.size() == 2, "Divide takes only two arguments");

        BigDecimal decimal = input.get(0);
        BigDecimal divisor = input.get(1);
        
        return decimal.divide(divisor, mathContext.get());
    }
    @Override
    public String toString() {
        return "divide";
    }
}
