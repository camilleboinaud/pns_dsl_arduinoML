package fr.polytech.unice.si5.kernel.generator;

import fr.polytech.unice.si5.kernel.App;
import fr.polytech.unice.si5.kernel.Morse;
import fr.polytech.unice.si5.kernel.behavioral.*;
import fr.polytech.unice.si5.kernel.structural.AnalogicalActuator;
import fr.polytech.unice.si5.kernel.structural.AnalogicalSensor;
import fr.polytech.unice.si5.kernel.structural.DigitalActuator;
import fr.polytech.unice.si5.kernel.structural.DigitalSensor;

import java.util.HashMap;
import java.util.Map;

public abstract class Visitor<T> {

	public abstract void visit(App app);

	public abstract void visit(State state);

	public abstract void visit(DigitalAction action);
	public abstract void visit(AnalogicalAction action);

	public abstract void visit(DigitalActuator actuator);
	public abstract void visit(AnalogicalActuator actuator);

	public abstract void visit(DigitalSensor sensor);
	public abstract void visit(AnalogicalSensor sensor);

	public abstract void visit(SimpleExpression simpleExpression);
	public abstract void visit(BooleanExpression booleanExpression);

	public abstract void visit(DigitalCondition digitalCondition);
	public abstract void visit(AnalogicalCondition analogicalCondition);

	public abstract void visit(Transition transition);
	public abstract void visit(Morse morse);


	/***********************
	 ** Helper mechanisms **
	 ***********************/

	protected Map<String,Object> context = new HashMap<>();

	protected T result;

	public T getResult() {
		return result;
	}

}

