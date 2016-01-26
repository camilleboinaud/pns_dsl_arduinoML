package kernel.structural;

import kernel.generator.Visitor;

/**
 * Created by cboinaud on 26/01/16.
 */
public class AnalogicalActuator extends AnalogicalBrick {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
