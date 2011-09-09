package see.functions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.Test;
import see.functions.service.Sequence;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.ImmutableList.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SequenceTest {
	Sequence<Integer> sequence = new Sequence<Integer>();

	/**
	 * Sequence should return null on empty list
	 * @throws Exception
	 */
	@Test
	public void testEmptyArgs() throws Exception {
        assertNull(sequence.apply(ImmutableList.<Integer>of()));
    }

	/**
	 * Sequence should return last value from passed list
	 * @throws Exception
	 */
	@Test
	public void testLastValue() throws Exception {
		int result = sequence.apply(of(100500, 42, 9));
		assertEquals(9, result);
	}

	/**
	 * Sequence should eagerly evaluate all elements from list
	 * @throws Exception
	 */
	@Test
	public void testEagerness() throws Exception {
		final AtomicInteger counter = new AtomicInteger(0);
		List<Integer> listWithSideEffects = Lists.transform(of(100500, 42, 9), new Function<Integer, Integer>() {
			@Override
			public Integer apply(Integer input) {
				counter.incrementAndGet();
				return input;
			}
		});

		assertEquals(0, counter.get());
		sequence.apply(listWithSideEffects);
		assertEquals(3, counter.get());
	}
}
