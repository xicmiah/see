package see.integration;

import com.google.common.io.ByteStreams;
import org.junit.Test;
import see.See;
import see.tree.Node;

import java.io.ObjectOutputStream;

public class SerializationTest {

    See see = new See();

    @Test
    public void testSerializableTrees() throws Exception {
        Node<Object> tree = see.parseExpression("a.b() == 'c'");

        ObjectOutputStream stream = new ObjectOutputStream(ByteStreams.nullOutputStream());
        stream.writeObject(tree);
    }
}
