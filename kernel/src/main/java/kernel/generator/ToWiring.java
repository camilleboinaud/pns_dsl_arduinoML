package kernel.generator;


import kernel.App;
import kernel.behavioral.*;
import kernel.structural.*;

/**
 * Quick and dirty visitor to support the generation of Wiring code
 */
public class ToWiring extends Visitor<StringBuffer> {

	private final static String CURRENT_STATE = "current_state";

	public ToWiring() {
		this.result = new StringBuffer();
	}

	private void w(String s) {
		result.append(String.format("%s\n",s));
	}

	@Override
	public void visit(App app) {
		//TODO
	}

	@Override
	public void visit(DigitalActuator actuator) {
		//TODO
	}

	@Override
	public void visit(AnalogicalActuator actuator) {
		//TODO
	}

	@Override
	public void visit(DigitalSensor sensor) {
		//TODO
	}

	@Override
	public void visit(AnalogicalSensor sensor) {
		//TODO
	}

	@Override
	public void visit(SimpleExpression simpleExpression) {
		//TODO
	}

	@Override
	public void visit(BooleanExpression booleanExpression) {
		//TODO
	}

	@Override
	public void visit(DigitalCondition digitalCondition) {
		//TODO
	}

	@Override
	public void visit(AnalogicalCondition analogicalCondition) {
		//TODO
	}

	@Override
	public void visit(State state) {
		//TODO
	}

	@Override
	public void visit(DigitalAction action) {
		//TODO
	}

	@Override
	public void visit(AnalogicalAction action) {
		//TODO
	}

}
