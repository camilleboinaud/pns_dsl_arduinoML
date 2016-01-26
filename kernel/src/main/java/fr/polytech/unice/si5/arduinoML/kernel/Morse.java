package fr.polytech.unice.si5.arduinoML.kernel;

import fr.polytech.unice.si5.arduinoML.kernel.generator.*;
import fr.polytech.unice.si5.arduinoML.kernel.structural.Code;


/**
 * Created by user on 26/01/16.
 */
public class Morse implements NamedElement, Visitable {
    private  String name;
    private Code code;

    @Override
    public String getName(){
        return this.name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }


    public Code getCode() {
        return code;
    }
}
