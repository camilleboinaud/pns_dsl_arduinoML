package fr.polytech.unice.si5.analogicalML.dsl;

import fr.polytech.unice.si5.kernel.App;
import fr.polytech.unice.si5.kernel.behavioral.Action;
import fr.polytech.unice.si5.kernel.behavioral.Expression;
import fr.polytech.unice.si5.kernel.behavioral.State;
import fr.polytech.unice.si5.kernel.behavioral.Transition;
import fr.polytech.unice.si5.kernel.generator.ToWiring;
import fr.polytech.unice.si5.kernel.generator.Visitor;
import fr.polytech.unice.si5.kernel.structural.*;
import groovy.lang.Binding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cboinaud on 26/01/16.
 */
public class ArduinoMLModel {

    protected List<State> states = new ArrayList<>();
    protected State initial = null;

    protected List<Brick> bricks = new ArrayList<>();

    protected Binding binding;

    public ArduinoMLModel(Binding binding){
        this.binding = binding;
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
