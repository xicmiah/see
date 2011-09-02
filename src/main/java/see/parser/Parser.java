package see.parser;

import see.exceptions.ParseException;
import see.tree.Node;

public interface Parser<T> {
	Node<T> parse(String input) throws ParseException;
}
