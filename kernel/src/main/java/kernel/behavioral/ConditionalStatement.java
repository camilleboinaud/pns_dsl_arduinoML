package kernel.behavioral;

import kernel.generator.Visitable;
import kernel.generator.Visitor;
import kernel.structural.SIGNAL;
import kernel.structural.Sensor;

import java.util.List;

/**
 * Created by Garance on 13/01/2016.
 */
public class ConditionalStatement implements Visitable {

    //Les 2 listes sont forc�ment de la m�me longueurs :
    // dans une transition, on donne la nouvelle valeur attendue
    //d'un sensor. Si ces listes sont > 1, ALORS existe une transition
    private List<Sensor> sensor;
    private List<SIGNAL> value;
    private List <BooleanExpression> booleanExpressions;

    public List<BooleanExpression> getBooleanExpressions() {
        return booleanExpressions;
    }

    public void setBooleanExpressions(List<BooleanExpression> booleanExpressions) {
        this.booleanExpressions = booleanExpressions;
    }


    public List<Sensor> getSensor() {
        return sensor;
    }

    public void setSensor(List<Sensor> sensor) {
        this.sensor = sensor;
    }

    public List<SIGNAL> getValue() {
        return value;
    }

    public void setValue(List<SIGNAL> value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor) {
            visitor.visit(this);
    }
}
