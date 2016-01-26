package kernel.behavioral;

import kernel.generator.Visitor;
import kernel.structural.DigitalActuator;
import kernel.structural.SIGNAL;

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
