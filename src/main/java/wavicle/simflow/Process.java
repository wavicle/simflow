package wavicle.simflow;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

public class Process {
	public static final String START = "start";

	private Map<String, StateHandler> handlersByState = new HashMap<>();
	private Map<String, Map<String, String>> transitionMapsByState = new HashMap<>();

	public void addState(String state, StateHandler handler) {
		handlersByState.put(state, handler);
	}

	public StateHandler getHandler(String state) {
		return handlersByState.get(state);
	}

	public void addTransition(String state, String transition, String target) {
		transitionMapsByState.computeIfAbsent(state, (key) -> new HashMap<>());
		transitionMapsByState.get(state).put(transition, target);
	}

	public String getTarget(String currentState, String transition) {
		Map<String, String> transitionMap = transitionMapsByState.get(currentState);
		Validate.notNull(transitionMap, "No transitions are defined for state: " + currentState);
		String targetState = transitionMap.get(transition);
		return targetState;
	}

	public boolean handlerExists(String state) {
		StateHandler handler = handlersByState.get(state);
		return handler != null;
	}

}
