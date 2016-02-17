package fr.polytech.unice.si5.kernel.behavioral;

import fr.polytech.unice.si5.kernel.generator.Visitor;

/**
 * Created by SUN on 17/02/2016.
 */
public class DelayAction extends Action {
    private int delay;

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


}
