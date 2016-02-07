package fr.polytech.unice.si5.analogicalML.utils;

/**
 * Created by cboinaud on 26/01/16.
 */
public enum UnitEnum {

    CELSIUS_DEGREE("°C"),
    VOLT("V");

    private String symbol;

    UnitEnum(String symbol){
        this.symbol = symbol;
    }

    public String getSymbol(){
        return symbol;
    }

}
