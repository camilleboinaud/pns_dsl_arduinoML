package fr.polytech.unice.si5.konamiML.dsl;

import fr.polytech.unice.si5.kernel.App;
import fr.polytech.unice.si5.kernel.behavioral.*;
import fr.polytech.unice.si5.kernel.generator.ToWiring;
import fr.polytech.unice.si5.kernel.generator.Visitor;
import fr.polytech.unice.si5.kernel.structural.*;
import fr.polytech.unice.si5.konamiML.utils.KonamiMapper;
import groovy.lang.Binding;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by user on 14/02/16.
 */
public class KonamiMLModel {
    protected List<State> states = new ArrayList<>();
    protected State initial = null;

    protected List<Brick> bricks = new ArrayList<>();

    protected Binding binding;


    public KonamiMLModel(Binding binding){
        this.binding = binding;
    }


    public void createDigitalSensor(String name, int pin){
        DigitalSensor dSensor = new DigitalSensor();
        dSensor.setName(name);
        dSensor.setPin(pin);
        this.bricks.add(dSensor);
    }

    public void createDigitalActuator(String name, int pin) {
        DigitalActuator digActuator = new DigitalActuator();
        digActuator.setName(name);
        digActuator.setPin(pin);
        this.bricks.add(digActuator);
    }

    public void createAnalogicalSensor(String name, int pin){
        AnalogicalSensor aSensor = new AnalogicalSensor();
        aSensor.setName(name);
        aSensor.setPin(pin);
        this.bricks.add(aSensor);
    }

    public void createAnalogicalActuatro(String name, int pin){
        AnalogicalActuator anaActuator = new  AnalogicalActuator();
        anaActuator.setName(name);
        anaActuator.setPin(pin);
        this.bricks.add(anaActuator);
    }

    public void createKonami(String konamicode){
        List<String> code = new ArrayList<>();
        for (String s: konamicode.split("")){
            code.add(s);
        }
        IntStream.range(0, code.size()).forEach(index -> {
            State state1 = new State();
            state1.setName("state" + index*2);
            List<Action> actions = new ArrayList<Action>();
            DigitalAction digAc = new DigitalAction();
            digAc.setActuator((DigitalActuator) this.bricks.get(3));// first digital actuator => led led
            digAc.setValue(SIGNAL.HIGH);
            actions.add(digAc);
            DigitalAction digAc2 = new DigitalAction(); // second digital actuator => green led
            digAc2.setActuator((DigitalActuator) this.bricks.get(4));
            digAc2.setValue(SIGNAL.LOW);
            actions.add(digAc2);
            DigitalAction digAc3 = new DigitalAction(); //third digital actuator => buzzer
            digAc3.setActuator((DigitalActuator) this.bricks.get(5));
            digAc3.setValue(SIGNAL.LOW);
            actions.add(digAc3);
           // AnalogicalAction analogicalAction = new AnalogicalAction();
            //analogicalAction.setDirection(KonamiMapper.getDirection(code.get(index).toUpperCase()));
            //actions.add(analogicalAction);
            state1.setActions(actions);
            this.states.add(state1);
            if(index < code.size()) {
                State state2 = new State();
                state2.setName("state" + (index * 2 + 1));
                this.states.add(state2);
            }
        });

        IntStream.range(0, states.size()).forEach(index -> {
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

    }

    public Object generate(String name){
        App app = new App();
        app.setName(name);
        app.setBricks(bricks);
        app.setStates(states);
        app.setInitial(new State());
        Visitor visitor = new ToWiring();
        app.accept(visitor);
        return visitor.getResult();
    }

}
