package fr.polytech.unice.si5.morseML.dsl;

import fr.polytech.unice.si5.kernel.App;
import fr.polytech.unice.si5.kernel.behavioral.*;
import fr.polytech.unice.si5.kernel.generator.ToWiring;
import fr.polytech.unice.si5.kernel.generator.Visitor;
import fr.polytech.unice.si5.kernel.structural.*;
import fr.polytech.unice.si5.morseML.utils.MorseMapper;
import fr.polytech.unice.si5.morseML.utils.MorseSignalMapper;
import groovy.lang.Binding;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ArduinoMLModel {

    protected List<State> states = new ArrayList<>();
    protected State initial = null;

    protected List<Brick> bricks = new ArrayList<>();

    protected Binding binding;

    public ArduinoMLModel(Binding binding){
        this.binding = binding;
    }

    public void createActuator(String name, int pin) {
        DigitalActuator actuator = new DigitalActuator();
        actuator.setName(name);
        actuator.setPin(pin);
        if(this.bricks.size()> 0) {
            this.bricks.set(0, actuator); // autorise only a digital actuator for the morse code
        } else {
            this.bricks.add(actuator);
        }
        this.binding.setVariable(name, actuator);
    }
    public void createMorse(String name) {
        List<String> signals = new ArrayList<>();
        for(String s : name.split("")) {
            String[] morseMapper = MorseMapper.getMosreMapper(s.toUpperCase()).split("");
            for(String mapper : morseMapper) {
                signals.add(mapper);
            }
        }
        // Add all the states
        IntStream.range(0, signals.size())
                    .forEach(index -> {
                        State state = new State();
                        state.setName("state" + index);
                        DigitalAction action = new DigitalAction();
                        action.setMorse(MorseSignalMapper.getMoserSignal(signals.get(index)));
                        action.setActuator((DigitalActuator) bricks.get(0));
                        List<Action> actions = new ArrayList<>();
                        actions.add(action);
                        state.setActions(actions);
                        this.states.add(state);
                    });
        State endState = new State();
        endState.setName("end");
        DigitalAction endAction = new DigitalAction();
        endAction.setMorse(MorseSignalMapper.getMoserSignal("|"));
        endAction.setActuator((DigitalActuator) bricks.get(0));
        List<Action> endActions = new ArrayList<>();
        endActions.add(endAction);
        endState.setActions(endActions);
        this.states.add(endState);

        // Set the transitions into the state
        IntStream.range(0, states.size())
                .forEach(index -> {
                    Transition transition = new Transition();
                    State to;
                    if(index == states.size()-1) {
                        to = states.get(0);
                    } else {
                        to = states.get(index + 1);
                    }
                    transition.setNext(to);
                    states.get(index).setTransition(transition);
                });

        // Set the initial State
        this.initial = states.get(0);
    }

    public Object generate(String name){
        App app = new App();
        app.setName(name);
        app.setBricks(bricks);
        app.setStates(states);
        app.setInitial(initial);
        Visitor visitor = new ToWiring();
        app.accept(visitor);
        return visitor.getResult();
    }
}
