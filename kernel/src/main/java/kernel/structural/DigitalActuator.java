package kernel.structural;

import kernel.generator.Visitor;

/**
 * Created by cboinaud on 26/01/16.
 */
public class DigitalActuator extends DigitalBrick {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
