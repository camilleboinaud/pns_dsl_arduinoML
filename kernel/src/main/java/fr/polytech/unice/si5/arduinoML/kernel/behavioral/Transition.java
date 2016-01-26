package fr.polytech.unice.si5.arduinoML.kernel.behavioral;

import fr.polytech.unice.si5.arduinoML.kernel.generator.Visitable;
import fr.polytech.unice.si5.arduinoML.kernel.generator.Visitor;

public class Transition implements Visitable {

	private State next;
	private Expression expression;

	public State getNext() {
		return next;
	}

	public void setNext(State next) {
		this.next = next;
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	@Override
	public void accept(Visitor visitor){
		visitor.visit(this);
	}

}
