package wavicle.simflow;

import java.util.Map;

public interface StateHandler {
	public static class StateOutput {
		public final Map<String, Object> output;
		public final String proposedTransition;

		public StateOutput(Map<String, Object> output, String proposedTransition) {
			super();
			this.output = output;
			this.proposedTransition = proposedTransition;
		}

	}

	StateOutput execute(Map<String, Object> stateInput, Map<String, Object> session);
}
