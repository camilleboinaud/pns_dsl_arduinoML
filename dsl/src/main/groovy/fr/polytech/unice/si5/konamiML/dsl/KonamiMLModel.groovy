package main.groovy.fr.polytech.unice.si5.konamiML.dsl

import fr.polytech.unice.si5.kernel.behavioral.State
import fr.polytech.unice.si5.kernel.structural.Brick

/**
 * Created by user on 09/02/16.
 */
class KonamiMLModel {

    protected List<State> states = new ArrayList<>();
    protected State initial = null;

    protected List<Brick> bricks = new ArrayList<>();

    protected Binding binding;


    public ArduinoMLModel(Binding binding){
        this.binding = binding;
    }

    public void createDigitalActuator(String name, int pin) {

    }
}
