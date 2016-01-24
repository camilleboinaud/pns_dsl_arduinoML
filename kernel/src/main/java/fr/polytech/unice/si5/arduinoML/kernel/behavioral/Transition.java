package fr.polytech.unice.si5.arduinoML.kernel.behavioral;


import fr.polytech.unice.si5.arduinoML.kernel.generator.Visitable;
import fr.polytech.unice.si5.arduinoML.kernel.generator.Visitor;
import fr.polytech.unice.si5.arduinoML.kernel.structural.SIGNAL;
import fr.polytech.unice.si5.arduinoML.kernel.structural.Sensor;

public class Transition implements Visitable {

	private State next;
	private Sensor sensor;
	private SIGNAL value;


	public State getNext() {
		return next;
	}

	public void setNext(State next) {
		this.next = next;
	}

	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public SIGNAL getValue() {
		return value;
	}

	public void setValue(SIGNAL value) {
		this.value = value;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
