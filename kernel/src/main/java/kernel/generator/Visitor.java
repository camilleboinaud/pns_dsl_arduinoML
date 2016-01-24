package kernel.generator;

import kernel.App;
import kernel.behavioral.*;
import kernel.structural.Actuator;
import kernel.structural.Sensor;

import java.util.HashMap;
import java.util.Map;

public abstract class Visitor<T> {

	public abstract void visit(App app);

	public abstract void visit(State state);
	public abstract void visit(Action action);

	public abstract void visit(Actuator actuator);
	public abstract void visit(Sensor sensor);

	public abstract void visit(ConditionalStatement conditionalStatement);
	public abstract void visit(ConditionalTransition conditionalTransition);

	public abstract void visit(TimerTransition timerTransition);

	/***********************
	 ** Helper mechanisms **
	 ***********************/

	protected Map<String,Object> context = new HashMap<>();

	protected T result;

	public T getResult() {
		return result;
	}

}

