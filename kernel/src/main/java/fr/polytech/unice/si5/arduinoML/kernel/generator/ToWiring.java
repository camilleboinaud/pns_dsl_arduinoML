package fr.polytech.unice.si5.arduinoML.kernel.generator;


import fr.polytech.unice.si5.arduinoML.kernel.behavioral.*;
import fr.polytech.unice.si5.arduinoML.kernel.App;
import fr.polytech.unice.si5.arduinoML.kernel.structural.*;

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
		w("// Wiring code generated from an ArduinoML model");
		w(String.format("// Application name: %s\n", app.getName()));

		w("void setup(){");
		for(Brick brick: app.getBricks()){
			brick.accept(this);
		}
		w("}\n");

		w("long time = 0; long debounce = 200;\n");

		for(State state: app.getStates()){
			state.accept(this);
		}

		w("void loop() {");
		w(String.format("  state_%s();", app.getInitial().getName()));
		w("}");
	}

	@Override
	public void visit(DigitalActuator actuator) {
		w(String.format("  pinMode(%d, OUTPUT); // %s [DigitalActuator]", actuator.getPin(), actuator.getName()));
	}

	@Override
	public void visit(AnalogicalActuator actuator) {
		w(String.format("  pinMode(%d, OUTPUT); // %s [AnalogicalActuator]", actuator.getPin(), actuator.getName()));
	}

	@Override
	public void visit(DigitalSensor sensor) {
		w(String.format("  pinMode(%d, INPUT);  // %s [DigitalSensor]", sensor.getPin(), sensor.getName()));
	}

	@Override
	public void visit(AnalogicalSensor sensor) {
		w(String.format("  pinMode(%d, INPUT);  // %s [AnalogicalSensor]", sensor.getPin(), sensor.getName()));
	}


	@Override
	public void visit(SimpleExpression simpleExpression) {
		simpleExpression.getCondition().accept(this);
	}

	@Override
	public void visit(BooleanExpression booleanExpression) {
		booleanExpression.getLeft().accept(this);
		w(String.format(" %s ", booleanExpression.getBooleanOperator().getSymbol()));
		booleanExpression.getRight().accept(this);
	}

	@Override
	public void visit(DigitalCondition digitalCondition) {
		w(String.format("digitalRead(%d) == %s", digitalCondition.getSensor().getPin(), digitalCondition.getSignal().toString()));
	}

	@Override
	public void visit(AnalogicalCondition analogicalCondition) {
		w(String.format("analogRead(%d) %s %d", analogicalCondition.getSensor().getPin(),
				analogicalCondition.getOperator().getSymbol(), analogicalCondition.getValueToCompare()));
	}

	@Override
	public void visit(Transition transition) {
		w(String.format("  if( "));
		transition.getExpression().accept(this);
		w(String.format(" && guard ) {"));

		w("    time = millis();");

		w(String.format("    state_%s();",transition.getNext().getName()));
		w("  } else {");
		w(String.format("    state_%s();",((State) context.get(CURRENT_STATE)).getName()));
		w("  }");
	}

	@Override
	public void visit(State state) {
		w(String.format("void state_%s() {",state.getName()));
		for(Action action: state.getActions()) {
			action.accept(this);
		}
		w("  boolean guard = millis() - time > debounce;");
		context.put(CURRENT_STATE, state);
		state.getTransition().accept(this);
		w("}\n");
	}

	@Override
	public void visit(DigitalAction action) {
		w(String.format("  digitalWrite(%d,%s);",action.getActuator().getPin(), action.getValue()));
	}

	@Override
	public void visit(AnalogicalAction action) {
		w(String.format("  analogWrite(%d,%d);",action.getActuator().getPin(), action.getValue()));
	}

}
