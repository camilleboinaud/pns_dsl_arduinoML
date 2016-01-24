package kernel.behavioral;

/**
 * Created by Garance on 13/01/2016.
 */
public enum BooleanExpression {
    AND("&&"), OR("||");

    private String expression;

    // Constructor
    BooleanExpression(String exp) {
        expression = exp;
    }

    public String getExpression() {
        return expression;
    }

}
