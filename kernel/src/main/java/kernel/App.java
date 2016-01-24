package kernel;

import kernel.behavioral.State;
import kernel.generator.Visitable;
import kernel.generator.Visitor;
import kernel.structural.Actuator;
import kernel.structural.Brick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quentin on 1/17/2016.
 */
public class App implements NamedElement, Visitable {

    private String name;
    private List<Brick> bricks = new ArrayList<>();
    private List<State> states = new ArrayList<State>();
    private State initial;
    private boolean morseMode = false;
    private String messageToTranslate;
    private List<Actuator> morseActuators;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public void setBricks(List<Brick> bricks) {
        this.bricks = bricks;
    }

    public boolean isMorseMode() {
        return morseMode;
    }

    public void setMorseMode(boolean morseMode) {
        this.morseMode = morseMode;
    }

    public State getInitial() {
        return initial;
    }

    public void setInitial(State initial) {
        this.initial = initial;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public String getMessageToTranslate() {
        return messageToTranslate;
    }

    public void setMessageToTranslate(String messageToTranslate) {
        this.messageToTranslate = messageToTranslate;
    }

    public List<Actuator> getMorseActuators() {
        return morseActuators;
    }

    public void setMorseActuators(List<Actuator> morseActuators) {
        this.morseActuators = morseActuators;
    }
}
