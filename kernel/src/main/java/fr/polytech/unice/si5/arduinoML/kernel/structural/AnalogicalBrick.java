package fr.polytech.unice.si5.arduinoML.kernel.structural;

import fr.polytech.unice.si5.arduinoML.kernel.generator.Visitable;

/**
 * Created by cboinaud on 26/01/16.
 */
public abstract class AnalogicalBrick extends Brick implements Visitable {

    private double rangeMin;
    private double rangeMax;

    public double getRangeMin() {
        return rangeMin;
    }

    public void setRangeMin(double rangeMin) {
        this.rangeMin = rangeMin;
    }

    public double getRangeMax() {
        return rangeMax;
    }

    public void setRangeMax(double rangeMax) {
        this.rangeMax = rangeMax;
    }

}
