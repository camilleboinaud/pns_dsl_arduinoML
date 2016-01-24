package fr.polytech.unice.si5.arduinoML.kernel.structural;


import fr.polytech.unice.si5.arduinoML.kernel.NamedElement;
import fr.polytech.unice.si5.arduinoML.kernel.generator.Visitable;

public abstract class Brick implements NamedElement, Visitable {

	private String name;
	private int pin;

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}