package fr.polytech.unice.si5.arduinoML.kernel.generator;

import fr.polytech.unice.si5.arduinoML.kernel.App;
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.Action;
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.State;
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.Transition;
import fr.polytech.unice.si5.arduinoML.kernel.structural.Actuator;
import fr.polytech.unice.si5.arduinoML.kernel.structural.Sensor;

import java.util.HashMap;
import java.util.Map;

public abstract class Visitor<T> {

	public abstract void visit(App app);

	public abstract void visit(State state);
	public abstract void visit(Transition transition);
	public abstract void visit(Action action);

	public abstract void visit(Actuator actuator);
	public abstract void visit(Sensor sensor);


	/***********************
	 ** Helper mechanisms **
	 ***********************/

	protected Map<String,Object> context = new HashMap<String, Object>();

	protected T result;

	public T getResult() {
		return result;
	}

}

