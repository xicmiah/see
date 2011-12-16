package see.functions.service;

import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

/**
 * Eagerly evaluate all elements in list and return last value, or null if list is empty.
 * @param <T>
 */
public class Sequence<T> implements VarArgFunction<T, T> {
	@Override
	public T apply(@Nonnull List<T> input) {
		Iterator<T> iterator = input.iterator();
		T lastValue = null;

		while (iterator.hasNext()) {
			lastValue = iterator.next();
		}
		
		return lastValue;
	}

    @Override
    public String toString() {
        return "seq";
    }
}
