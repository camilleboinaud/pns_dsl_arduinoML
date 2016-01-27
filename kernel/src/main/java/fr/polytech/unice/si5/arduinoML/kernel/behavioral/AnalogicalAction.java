package fr.polytech.unice.si5.arduinoML.kernel.behavioral;

import fr.polytech.unice.si5.arduinoML.kernel.generator.Visitor;
import fr.polytech.unice.si5.arduinoML.kernel.structural.AnalogicalActuator;

/**
 * Created by cboinaud on 26/01/16.
 */
public class AnalogicalAction extends Action {

    private double value;
    private AnalogicalActuator actuator;


    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public AnalogicalActuator getActuator() {
        return actuator;
    }

    public void setActuator(AnalogicalActuator actuator) {
        this.actuator = actuator;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
