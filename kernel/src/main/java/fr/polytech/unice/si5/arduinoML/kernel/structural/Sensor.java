package fr.polytech.unice.si5.arduinoML.kernel.structural;


import fr.polytech.unice.si5.arduinoML.kernel.generator.Visitor;

public class Sensor extends Brick {

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
