package kernel.structural;

import kernel.generator.Visitor;

public abstract class Actuator extends Brick {
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

}
