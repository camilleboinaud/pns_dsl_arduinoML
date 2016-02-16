package fr.polytech.unice.si5.kernel.generator;


import fr.polytech.unice.si5.kernel.App;
import fr.polytech.unice.si5.kernel.behavioral.*;
import fr.polytech.unice.si5.kernel.structural.*;

/**
 * Quick and dirty visitor to support the generation of Wiring code
 */
public class ToWiring extends Visitor<StringBuffer> {

	private final static String CURRENT_STATE = "current_state";
	public final static int UNITY_LENGTH = 300;

	public ToWiring() {
		this.result = new StringBuffer();
	}

	private void wn(String s) {
		result.append(String.format("%s\n",s));
	}

	private void w(String s) {
		result.append(String.format("%s",s));
	}

	@Override
	public void visit(App app) {
		wn("// Wiring code generated from an ArduinoML model");
		wn(String.format("// Application name: %s\n", app.getName()));

		wn("void setup(){");
		for(Brick brick: app.getBricks()){
			brick.accept(this);
		}
		wn("}\n");

		wn("long time = 0; long debounce = 200;\n");

		for(State state: app.getStates()){
			state.accept(this);
		}

		wn("void loop() {");
		wn(String.format("  state_%s();", app.getInitial().getName()));
		wn("}");
	}

	@Override
	public void visit(DigitalActuator actuator) {
		wn(String.format("  pinMode(%d, OUTPUT); // %s [DigitalActuator]", actuator.getPin(), actuator.getName()));
	}

	@Override
	public void visit(AnalogicalActuator actuator) {
		wn(String.format("  pinMode(%d, OUTPUT); // %s [AnalogicalActuator]", actuator.getPin(), actuator.getName()));
	}

	@Override
	public void visit(DigitalSensor sensor) {
		wn(String.format("  pinMode(%d, INPUT);  // %s [DigitalSensor]", sensor.getPin(), sensor.getName()));
	}

	@Override
	public void visit(AnalogicalSensor sensor) {
		wn(String.format("  pinMode(%d, INPUT);  // %s [AnalogicalSensor]", sensor.getPin(), sensor.getName()));
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
				analogicalCondition.getOperator().getSymbol(), (int)analogicalCondition.getValueToCompare()));
	}

	@Override
	public void visit(Transition transition) {
		if(transition.getExpression() == null) {
			wn(String.format("  state_%s();",  transition.getNext().getName()));
		} else {
			w(String.format("  if(("));
			transition.getExpression().accept(this);
			wn(String.format(") && guard ) {"));

			wn("    time = millis();");

			wn(String.format("    state_%s();", transition.getNext().getName()));
			wn("  } else {");
			wn(String.format("    state_%s();", ((State) context.get(CURRENT_STATE)).getName()));
			wn("  }");
		}
	}

	@Override
	public void visit(State state) {
		wn(String.format("void state_%s() {",state.getName()));
		for(Action action: state.getActions()) {
			action.accept(this);
		}
		wn("  boolean guard = millis() - time > debounce;");
		context.put(CURRENT_STATE, state);
		state.getTransition().accept(this);
		wn("}\n");
	}

	@Override
	public void visit(DigitalAction action) {
		MORSESIGNAL morse = action.getMorse();
		if( morse != null) {
			generateMorseArduino(morse, action.getActuator().getPin());
		} else {
			wn(String.format("  digitalWrite(%d,%s);",action.getActuator().getPin(), action.getValue()));
		}
	}

	@Override
	public void visit(AnalogicalAction action) {
		wn(String.format("  analogWrite(%d,%d);",action.getActuator().getPin(), (int) action.getValue()));
	}

	private void generateMorseArduino(MORSESIGNAL morse, int pin) {
		switch(morse) {
			case SHORT:
				wn(String.format("  digitalWrite(%d,%s);", pin, SIGNAL.HIGH));
				wn(String.format("  delay(%d);", UNITY_LENGTH));
				wn(String.format("  digitalWrite(%d,%s);", pin, SIGNAL.LOW));
				wn(String.format("  delay(%d);", UNITY_LENGTH));
				break;
			case LONG:
				wn(String.format("  digitalWrite(%d,%s);", pin, SIGNAL.HIGH));
				wn(String.format("  delay(%d);", UNITY_LENGTH * 3));
				wn(String.format("  digitalWrite(%d,%s);", pin, SIGNAL.LOW));
				wn(String.format("  delay(%d);", UNITY_LENGTH));
				break;
			case STOP:
				wn(String.format("  delay(%d);", UNITY_LENGTH));
				break;
			case ESPACE:
				wn(String.format("  delay(%d);", UNITY_LENGTH));
				break;
			case END:
				wn(String.format("  delay(%d);", UNITY_LENGTH * 10));
				break;

		}
	}
}