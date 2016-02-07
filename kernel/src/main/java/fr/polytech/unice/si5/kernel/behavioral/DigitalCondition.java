package fr.polytech.unice.si5.kernel.behavioral;

import fr.polytech.unice.si5.kernel.generator.Visitor;
import fr.polytech.unice.si5.kernel.structural.DigitalSensor;
import fr.polytech.unice.si5.kernel.structural.SIGNAL;

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
