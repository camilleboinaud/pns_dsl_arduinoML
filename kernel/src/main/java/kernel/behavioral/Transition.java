package kernel.behavioral;

import kernel.generator.Visitable;

public abstract class Transition implements Visitable {

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
}
