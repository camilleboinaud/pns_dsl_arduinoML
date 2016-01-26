package fr.polytech.unice.si5.arduinoML.kernel.behavioral;

/**
 * Created by cboinaud on 26/01/16.
 */
public enum BOOLEAN {

    AND("&&"),
    OR("||");

    private String symbol;

    BOOLEAN(String symbol){
        this.symbol = symbol;
    }

    public String getSymbol(){
        return symbol;
    }

}
