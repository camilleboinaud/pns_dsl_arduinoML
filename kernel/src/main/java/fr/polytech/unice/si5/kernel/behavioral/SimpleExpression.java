package fr.polytech.unice.si5.kernel.behavioral;

import fr.polytech.unice.si5.kernel.generator.Visitor;

/**
 * Created by cboinaud on 26/01/16.
 */
public class SimpleExpression extends Expression {

    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }

}
