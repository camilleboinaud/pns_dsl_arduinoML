package fr.polytech.unice.si5.analogicalML.utils;

/**
 * Created by cboinaud on 26/01/16.
 */
public enum LinearUnitEnum {

    degrees("°C", 20.48, 102.4),

    kiloVolts("kV", 0.01, 0),
    volts("V", 1, 0),
    milivolts("mV", 1000, 0);

    private String symbol;
    private double a;
    private double b;


    LinearUnitEnum(String symbol, double a, double b){
        this.symbol = symbol;
        this.a = a;
        this.b = b;
    }

    public String getSymbol(){
        return symbol;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }
}
