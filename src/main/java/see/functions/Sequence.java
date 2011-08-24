package see.functions;

import com.google.common.base.Function;

import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Eagerly evaluate all elements in list and return last value
 * @param <T>
 */
public class Sequence<T> implements Function<List<T>, T> {
	@Override
	public T apply(List<T> input) {
		checkArgument(!input.isEmpty(), "Cannot evaluate empty list");

		Iterator<T> iterator = input.iterator();
		T lastValue = iterator.next();

		while (iterator.hasNext()) {
			lastValue = iterator.next();
		}
		
		return lastValue;
	}
}
