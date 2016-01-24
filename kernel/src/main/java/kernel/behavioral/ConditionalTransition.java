package kernel.behavioral;

import kernel.generator.Visitor;

/**
 * Created by Quentin on 1/24/2016.
 */
public class ConditionalTransition extends Transition {

    private ConditionalStatement conditionalStatement;

    public ConditionalStatement getConditionalStatements() {
        return conditionalStatement;
    }

    public void setConditionalStatements(ConditionalStatement conditionalStatements) {
        this.conditionalStatement = conditionalStatements;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
