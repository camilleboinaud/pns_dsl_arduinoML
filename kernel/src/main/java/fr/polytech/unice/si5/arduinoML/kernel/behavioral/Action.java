package fr.polytech.unice.si5.arduinoML.kernel.behavioral;

import fr.polytech.unice.si5.arduinoML.kernel.generator.Visitable;
import fr.polytech.unice.si5.arduinoML.kernel.generator.Visitor;
import fr.polytech.unice.si5.arduinoML.kernel.structural.Actuator;
import fr.polytech.unice.si5.arduinoML.kernel.structural.SIGNAL;

public class Action implements Visitable {

	private SIGNAL value;
	private Actuator actuator;


	public SIGNAL getValue() {
		return value;
	}

	public void setValue(SIGNAL value) {
		this.value = value;
	}

	public Actuator getActuator() {
		return actuator;
	}

	public void setActuator(Actuator actuator) {
		this.actuator = actuator;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
