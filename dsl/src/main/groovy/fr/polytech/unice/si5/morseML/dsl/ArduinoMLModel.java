package fr.polytech.unice.si5.morseML.dsl;

import fr.polytech.unice.si5.kernel.App;
import fr.polytech.unice.si5.kernel.behavioral.Action;
import fr.polytech.unice.si5.kernel.behavioral.Expression;
import fr.polytech.unice.si5.kernel.behavioral.State;
import fr.polytech.unice.si5.kernel.behavioral.Transition;
import fr.polytech.unice.si5.kernel.generator.ToWiring;
import fr.polytech.unice.si5.kernel.generator.Visitor;
import fr.polytech.unice.si5.kernel.structural.*;
import fr.polytech.unice.si5.morseML.utils.MorseMapper;
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
        this.bricks.add(actuator);
        this.binding.setVariable(name, actuator);
        System.out.println("name :" + name + " , pin : " + pin);
    }
    public void createMorse(String name) {
        List<Character> characters = new ArrayList<>();
        for(String s : name.split("")) {
            char[] chars = MorseMapper.getMosreMapper(s).toCharArray();
            for(char c : chars) {
                characters.add(new Character(c));
            }
        }
        // Add all the states
        IntStream.range(0, characters.size())
                    .forEach(index -> {
                        State state = new State();
                        state.setName("state" + index);
                        this.states.add(state);
                    });
        State endState = new State();
        endState.setName("end");
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



































    public void createAnalogicalSensor(String name, int pin){
        AnalogicalSensor sensor = new AnalogicalSensor();
        sensor.setName(name);
        sensor.setPin(pin);
        this.bricks.add(sensor);
        this.binding.setVariable(name.toUpperCase(), sensor);

    }

    public void createDigitalSensor(String name, int pin){
        DigitalSensor sensor = new DigitalSensor();
        sensor.setName(name);
        sensor.setPin(pin);
        this.bricks.add(sensor);
        this.binding.setVariable(name.toUpperCase(), sensor);
    }

    public void createAnalogicalActuator(String name, int pin){
        AnalogicalActuator actuator = new AnalogicalActuator();
        actuator.setName(name);
        actuator.setPin(pin);
        this.bricks.add(actuator);
        this.binding.setVariable(name.toUpperCase(), actuator);
    }


    public void createDigitalActuator(String name, int pin){
        DigitalActuator actuator = new DigitalActuator();
        actuator.setName(name);
        actuator.setPin(pin);
        this.bricks.add(actuator);
        this.binding.setVariable(name.toUpperCase(), actuator);
    }

    public State createState(String name, List<Action> actions){
        State state = new State();
        state.setName(name);
        this.states.add(state);
        this.binding.setVariable(name.toUpperCase(), state);
        return state;
    }

    public void createInitial(State init){
        this.initial = init;
    }

    public void createTransition(State from, State to, Expression expression){
        Transition transition = new Transition();
        transition.setNext(to);
        transition.setExpression(expression);
        from.setTransition(transition);
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
