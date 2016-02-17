package fr.polytech.unice.si5.kernel.behavioral;

/**
 * Created by cboinaud on 26/01/16.
 */
public enum OPERATOR {

    NE("!"),
    EQ("="),
    EE("=="),
    LE("<="),
    GE(">="),
    LT("<"),
    GT(">");

    private String symbol;

    OPERATOR(String symbol){
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
