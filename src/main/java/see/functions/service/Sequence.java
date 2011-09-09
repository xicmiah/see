package see.functions.service;

import see.functions.Function;

import java.util.Iterator;
import java.util.List;

/**
 * Eagerly evaluate all elements in list and return last value, or null if list is empty.
 * @param <T>
 */
public class Sequence<T> implements Function<List<T>, T> {
	@Override
	public T apply(List<T> input) {
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
