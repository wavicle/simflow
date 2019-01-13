package wavicle.simflow;

import java.util.Map;

public interface StateHandler {
	public static class StateOutput {
		public final String proposedTransition;
		public final Map<String, Object> output;

		public StateOutput(String proposedTransition, Map<String, Object> output) {
			super();
			this.proposedTransition = proposedTransition;
			this.output = output;
		}

		public StateOutput(String proposedTransition) {
			super();
			this.proposedTransition = proposedTransition;
			this.output = null;
		}

		public StateOutput(Map<String, Object> output) {
			super();
			this.proposedTransition = null;
			this.output = output;
		}

	}

	StateOutput execute(Map<String, Object> stateInput, Map<String, Object> session);
}
