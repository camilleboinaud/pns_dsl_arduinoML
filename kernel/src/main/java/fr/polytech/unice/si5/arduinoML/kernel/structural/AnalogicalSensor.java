package fr.polytech.unice.si5.arduinoML.kernel.structural;

import fr.polytech.unice.si5.arduinoML.kernel.generator.Visitor;

/**
 * Created by cboinaud on 26/01/16.
 */
public class AnalogicalSensor extends AnalogicalBrick {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
