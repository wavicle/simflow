package wavicle.simflow;

import java.util.Collections;
import java.util.HashMap;

import wavicle.simflow.StateHandler.StateOutput;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		Process process = new Process();

		process.addState("start", (input, session) -> {
			return new StateOutput(null, "next");
		});

		process.addState("askFirstName", (input, session) -> {
			session.put("firstName", "Shashank");
			return new StateOutput(null, "next");
		});

		process.addState("calcFullName", (input, session) -> {
			String firstName = (String) session.get("firstName");
			return new StateOutput(Collections.singletonMap("out", firstName + " Araokar"), "next");
		});

		process.addState("tellName", (input, session) -> {
			return new StateOutput(Collections.singletonMap("out", "Your name is: " + input.get("out")), null);
		});

		process.addTransition("start", "next", "askFirstName");
		process.addTransition("askFirstName", "next", "calcFullName");
		process.addTransition("calcFullName", "next", "tellName");
		process.addTransition("tellName", "next", "start");

		ProcessInstance inst = new ProcessInstance(process, new HashMap<>());
		StateOutput stateOutput = inst.start(new HashMap<>());

		stateOutput = inst.followTransition("next", stateOutput.output);
		stateOutput = inst.followTransition("next", stateOutput.output);
		stateOutput = inst.followTransition("next", stateOutput.output);
		System.out.println(stateOutput.output.get("out"));
	}
}
