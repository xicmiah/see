package see.parser;

import see.tree.Node;

public interface Parser<T> {
	Node<T> parse(String input);
}
