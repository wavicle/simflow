package wavicle.simflow;

import java.util.Map;

import org.apache.commons.lang3.Validate;

import wavicle.simflow.StateHandler.StateOutput;

public class ProcessInstance {
	private Process process;
	private Map<String, Object> session;
	private String currentState;

	public ProcessInstance(Process process, Map<String, Object> session) {
		super();
		Validate.notNull(process, "A null process object was passed.");
		Validate.notNull(session, "A null session object was passed.");
		this.process = process;
		this.session = session;
	}

	public void setCurrentState(String currentState, Map<String, Object> session) {
		if (!process.handlerExists(currentState)) {
			throw new IllegalArgumentException("Unhandled state: " + currentState);
		}
		Validate.notNull(session, "A null session object was passed.");
		this.currentState = currentState;
		this.session = session;
	}

	public StateOutput start(Map<String, Object> stateInput) {
		Validate.notNull(stateInput, "A null state input map was passed.");
		StateHandler handler = process.getHandler(Process.START);
		Validate.notNull(handler, "No start state is defined");
		StateOutput stateOutput = handler.execute(stateInput, session);
		currentState = Process.START;
		return stateOutput;
	}

	public StateOutput followTransition(String transition, Map<String, Object> stateInput) {
		Validate.notNull(currentState, "Current state is null. The process might not be started yet.");
		String targetState = process.getTarget(currentState, transition);
		Validate.notNull(targetState,
				"The transition: " + transition + " from state: " + currentState + " leads nowhere.");
		StateHandler targetHandler = process.getHandler(targetState);
		Validate.notNull(targetHandler, "No handler has been defined for target state: " + targetState,
				" for transition: " + transition + " from state: " + currentState);
		StateOutput stateOutput = targetHandler.execute(stateInput, session);
		this.currentState = targetState;
		return stateOutput;
	}
}
