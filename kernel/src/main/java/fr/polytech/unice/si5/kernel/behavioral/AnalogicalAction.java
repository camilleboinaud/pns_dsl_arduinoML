package fr.polytech.unice.si5.kernel.behavioral;

import fr.polytech.unice.si5.kernel.generator.Visitor;
import fr.polytech.unice.si5.kernel.structural.AnalogicalActuator;
import fr.polytech.unice.si5.kernel.structural.DIRECTION;

/**
 * Created by cboinaud on 26/01/16.
 */
public class AnalogicalAction extends Action {

    private double value;
    private AnalogicalActuator actuator;
    private DIRECTION direction;


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

    public DIRECTION getDirection() {
        return direction;
    }

    public void setDirection(DIRECTION direction) {
        this.direction = direction;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
