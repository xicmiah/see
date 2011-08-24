package see.evaluator;

import java.util.Map;

/**
 * Interface for data objects which provide read/write access to current context
 */
public interface ContextAware {
	void setContext(Map<String, Object> context);
}
