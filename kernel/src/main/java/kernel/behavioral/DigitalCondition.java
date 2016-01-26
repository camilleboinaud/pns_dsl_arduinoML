package kernel.behavioral;

import kernel.generator.Visitor;
import kernel.structural.DigitalSensor;
import kernel.structural.SIGNAL;

/**
 * Created by cboinaud on 26/01/16.
 */
public class DigitalCondition extends Condition {

    private DigitalSensor sensor;
    private SIGNAL signal;

    public DigitalSensor getSensor() {
        return sensor;
    }

    public void setSensor(DigitalSensor sensor) {
        this.sensor = sensor;
    }

    public SIGNAL getSignal() {
        return signal;
    }

    public void setSignal(SIGNAL signal) {
        this.signal = signal;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }

}
