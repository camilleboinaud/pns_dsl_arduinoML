package kernel.structural;

/**
 * Created by Quentin on 1/24/2016.
 */
public class Moment {

    private int amount;
    private MomentUnit momentUnit;

    public Moment(int amount, MomentUnit momentUnit) {
        this.amount = amount;
        this.momentUnit = momentUnit;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public MomentUnit getMomentUnit() {
        return momentUnit;
    }

    public void setMomentUnit(MomentUnit momentUnit) {
        this.momentUnit = momentUnit;
    }
}
