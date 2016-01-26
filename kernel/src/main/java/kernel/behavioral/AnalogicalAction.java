package kernel.behavioral;

import kernel.generator.Visitor;
import kernel.structural.AnalogicalActuator;

/**
 * Created by cboinaud on 26/01/16.
 */
public class AnalogicalAction extends Action {

    private int value;
    private AnalogicalActuator actuator;


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
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
