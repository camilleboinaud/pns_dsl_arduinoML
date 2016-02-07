package fr.polytech.unice.si5.kernel.structural;

import fr.polytech.unice.si5.kernel.generator.Visitor;

/**
 * Created by cboinaud on 26/01/16.
 */
public class DigitalSensor extends DigitalBrick {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
