package see.functions;

import see.evaluator.ContextAware;

import java.util.List;
import java.util.Map;

public class Assign<T> implements SingleArgFunction<Assign.VariableAndValue<T>, T> {
	@Override
	public T apply(List<VariableAndValue<T>> input) {
		VariableAndValue<T> variableAndValue = input.get(0);

		Map<String, Object> context = variableAndValue.context;
		context.put(variableAndValue.variable, variableAndValue.value);

		return variableAndValue.value;
	}

	public static class VariableAndValue<T> implements ContextAware {
		private Map<String, Object> context;
		private final String variable;
		private final T value;

		public VariableAndValue(String variable, T value) {
			this.variable = variable;
			this.value = value;
		}

		@Override
		public void setContext(Map<String, Object> context) {
			this.context = context;
		}
	}
}
