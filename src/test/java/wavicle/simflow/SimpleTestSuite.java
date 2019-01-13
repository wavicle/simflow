package wavicle.simflow;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;

import org.junit.Test;

import wavicle.simflow.StateHandler.StateOutput;

public class SimpleTestSuite {

	@Test
	public void simple() {

		/** Define the process and add some states to it, along with their behavior **/
		Process process = new Process();
		process.addState("start", (input, session) -> {
			return new StateOutput("next");
		});
		process.addState("askFirstName", (input, session) -> {
			session.put("firstName", "Shashank");
			return new StateOutput("next");
		});
		process.addState("calcFullName", (input, session) -> {
			String firstName = (String) session.get("firstName");
			return new StateOutput("next", Collections.singletonMap("out", firstName + " Araokar"));
		});
		process.addState("tellName", (input, session) -> {
			return new StateOutput(Collections.singletonMap("out", "Your name is: " + input.get("out")));
		});

		/** Specify how the process transitions from one state to another **/
		process.addTransition("start", "next", "askFirstName");
		process.addTransition("askFirstName", "next", "calcFullName");
		process.addTransition("calcFullName", "next", "tellName");
		process.addTransition("tellName", "next", "start");

		/** Now create an actual instance of the process above that we will run **/
		ProcessInstance inst = new ProcessInstance(process, new HashMap<>());

		/**
		 * This map will collect the output of the previous state and will also serve as
		 * input to the next state
		 **/
		StateOutput stateOutput = inst.start(new HashMap<>());

		/**
		 * Note that we are simulating the 'next' call on the process. In a real-world
		 * application, this will happen when the user clisk the 'Submit' button, for
		 * example.
		 */
		stateOutput = inst.followTransition("next", stateOutput.output);
		stateOutput = inst.followTransition("next", stateOutput.output);
		stateOutput = inst.followTransition("next", stateOutput.output);

		/**
		 * Verify that the process computed a sentence as expected. Observe the state
		 * definitions in the previous step to understand how the states work together
		 * to make this happen.
		 **/
		String outputFromProcess = (String) stateOutput.output.get("out");
		assertEquals("Your name is: Shashank Araokar", outputFromProcess);

	}

}
