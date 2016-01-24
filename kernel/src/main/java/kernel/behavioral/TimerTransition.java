package kernel.behavioral;

import kernel.generator.Visitor;
import kernel.structural.Moment;

/**
 * Created by Quentin on 1/24/2016.
 */
public class TimerTransition extends Transition {

    private Moment moment;

    public Moment getMoment() {
        return moment;
    }

    public void setMoment(Moment moment) {
        this.moment = moment;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
