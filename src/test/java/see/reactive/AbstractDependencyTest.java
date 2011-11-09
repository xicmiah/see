package see.reactive;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class AbstractDependencyTest {
    @Test
    public void testInterraction() throws Exception {
        final Var<String> a = new Var<String>("asd");
        Signal<Integer> b = new StatefulSignal<Integer>(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return a.now().length();
            }
        }, ImmutableSet.of(a));

        assertEquals(Integer.valueOf(3), b.now());
        a.update("omg");
        assertEquals(Integer.valueOf(3), b.now());
        a.update("zxcv");
        assertEquals(Integer.valueOf(4), b.now());
    }
}
