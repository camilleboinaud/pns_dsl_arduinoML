package fr.polytech.unice.si5.arduinoML.kernel.behavioral;

import fr.polytech.unice.si5.arduinoML.kernel.generator.Visitor;
import fr.polytech.unice.si5.arduinoML.kernel.structural.DigitalActuator;
import fr.polytech.unice.si5.arduinoML.kernel.structural.SIGNAL;

/**
 * Created by cboinaud on 26/01/16.
 */
public class DigitalAction extends Action {

    private SIGNAL value;
    private DigitalActuator actuator;


    public SIGNAL getValue() {
        return value;
    }

    public void setValue(SIGNAL value) {
        this.value = value;
    }

    public DigitalActuator getActuator() {
        return actuator;
    }

    public void setActuator(DigitalActuator actuator) {
        this.actuator = actuator;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
