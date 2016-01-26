package fr.polytech.unice.si5.arduinoML.kernel.behavioral;

import fr.polytech.unice.si5.arduinoML.kernel.generator.Visitor;
import fr.polytech.unice.si5.arduinoML.kernel.structural.AnalogicalSensor;

/**
 * Created by cboinaud on 26/01/16.
 */
public class AnalogicalCondition extends Condition{

    private AnalogicalSensor sensor;
    private OPERATOR operator;
    private int valueToCompare;

    public AnalogicalSensor getSensor() {
        return sensor;
    }

    public void setSensor(AnalogicalSensor sensor) {
        this.sensor = sensor;
    }

    public OPERATOR getOperator() {
        return operator;
    }

    public void setOperator(OPERATOR operator) {
        this.operator = operator;
    }

    public int getValueToCompare() {
        return valueToCompare;
    }

    public void setValueToCompare(int valueToCompare) {
        this.valueToCompare = valueToCompare;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }

}
