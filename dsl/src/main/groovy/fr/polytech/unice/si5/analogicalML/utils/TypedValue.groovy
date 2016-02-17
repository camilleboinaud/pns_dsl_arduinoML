package fr.polytech.unice.si5.analogicalML.utils;

import groovy.transform.TupleConstructor;

/**
 * Created by cboinaud on 16/02/16.
 */
@TupleConstructor
public class TypedValue {

    double value;
    LinearUnitEnum unit;

    String toString(){
        "$value $unit"
    }

    double mVoltConvertion(){
        return value * unit.a + unit.b;
    }

}
