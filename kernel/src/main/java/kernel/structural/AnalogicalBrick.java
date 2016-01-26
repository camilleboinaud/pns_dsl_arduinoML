package kernel.structural;

import kernel.generator.Visitable;
import kernel.generator.Visitor;

/**
 * Created by cboinaud on 26/01/16.
 */
public abstract class AnalogicalBrick extends Brick implements Visitable {

    private int rangeMin;
    private int rangeMax;

    public int getRangeMin() {
        return rangeMin;
    }

    public void setRangeMin(int rangeMin) {
        this.rangeMin = rangeMin;
    }

    public int getRangeMax() {
        return rangeMax;
    }

    public void setRangeMax(int rangeMax) {
        this.rangeMax = rangeMax;
    }

}
