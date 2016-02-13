package fr.polytech.unice.si5.kernel.behavioral;

import fr.polytech.unice.si5.kernel.generator.Visitor;
import fr.polytech.unice.si5.kernel.structural.DigitalActuator;
import fr.polytech.unice.si5.kernel.structural.MORSESIGNAL;
import fr.polytech.unice.si5.kernel.structural.SIGNAL;

/**
 * Created by cboinaud on 26/01/16.
 */
public class DigitalAction extends Action {

    private SIGNAL value;
    private DigitalActuator actuator;
    private MORSESIGNAL morse;


    public SIGNAL getValue() {
        return value;
    }

    public void setValue(SIGNAL value) {
        this.value = value;
    }

    public MORSESIGNAL getMorse() {
        return this.morse;
    }

    public void setMorse(MORSESIGNAL morse) {
        this.morse = morse;
    }

    public DigitalActuator getActuator() {
        return actuator;
    }

    public void setActuator(DigitalActuator actuator) {
        this.actuator = actuator;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
