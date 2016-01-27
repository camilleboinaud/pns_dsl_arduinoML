package fr.polytech.unice.si5.arduinoML.kernel;


import fr.polytech.unice.si5.arduinoML.kernel.behavioral.State;
import fr.polytech.unice.si5.arduinoML.kernel.generator.Visitable;
import fr.polytech.unice.si5.arduinoML.kernel.generator.Visitor;
import fr.polytech.unice.si5.arduinoML.kernel.structural.Brick;

import java.util.ArrayList;
import java.util.List;

public class App implements NamedElement, Visitable {

    private String name;
    private List<Brick> bricks = new ArrayList<Brick>();
    private List<State> states = new ArrayList<State>();
    private State initial;
    private boolean morseEnable = false;
    private String morseCode = null;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public void setBricks(List<Brick> bricks) {
        this.bricks = bricks;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public boolean isMorseEnable() {
        return morseEnable;
    }

    public void setMorseEnable(boolean morseEnable) {
        this.morseEnable = morseEnable;
    }

    public String getMorseCode() {
        return morseCode;
    }

    public void setMorseCode(String morseCode) {
        this.morseCode = morseCode;
    }

    public State getInitial() {
        return initial;
    }

    public void setInitial(State initial) {
        this.initial = initial;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
