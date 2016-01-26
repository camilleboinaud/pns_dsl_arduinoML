package fr.polytech.unice.si5.arduinoML.kernel.behavioral;

import fr.polytech.unice.si5.arduinoML.kernel.generator.Visitor;

/**
 * Created by cboinaud on 26/01/16.
 */
public class BooleanExpression extends Expression {

    private Expression left, right;
    private BOOLEAN booleanOperator;

    public Expression getLeft() {
        return left;
    }

    public void setLeft(Expression left) {
        this.left = left;
    }

    public BOOLEAN getBooleanOperator() {
        return booleanOperator;
    }

    public void setBooleanOperator(BOOLEAN booleanOperator) {
        this.booleanOperator = booleanOperator;
    }

    public Expression getRight() {
        return right;
    }

    public void setRight(Expression right) {
        this.right = right;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
