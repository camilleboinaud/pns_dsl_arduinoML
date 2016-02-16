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

    public void createAnalogicalActuator(String name, int pin){
        AnalogicalActuator anaActuator = new  AnalogicalActuator();
        anaActuator.setName(name);
        anaActuator.setPin(pin);
        this.bricks.add(anaActuator);
    }

    public void createKonami(String konamicode){


        DigitalAction redledlow = new DigitalAction();
        redledlow.setActuator((DigitalActuator) this.bricks.get(3));// first digital actuator => led led
        redledlow.setValue(SIGNAL.LOW);

        DigitalAction redledhigh = new DigitalAction();
        redledhigh.setActuator((DigitalActuator) this.bricks.get(3));// first digital actuator => led led
        redledhigh.setValue(SIGNAL.HIGH);

        DigitalAction greenledhlow = new DigitalAction(); // second digital actuator => green led
        greenledhlow.setActuator((DigitalActuator) this.bricks.get(4));
        greenledhlow.setValue(SIGNAL.LOW);

        DigitalAction greenledhigh = new DigitalAction(); // second digital actuator => green led
        greenledhigh.setActuator((DigitalActuator) this.bricks.get(4));
        greenledhigh.setValue(SIGNAL.HIGH);

        DigitalAction buzzerlow = new DigitalAction(); //third digital actuator => buzzer
        buzzerlow.setActuator((DigitalActuator) this.bricks.get(5));
        buzzerlow.setValue(SIGNAL.LOW);

        DigitalAction buzzerhigh = new DigitalAction(); //third digital actuator => buzzer
        buzzerhigh.setActuator((DigitalActuator) this.bricks.get(5));
        buzzerhigh.setValue(SIGNAL.HIGH);

        //create a success state
        State success = new State();
        success.setName("success");
        List<Action> actionSuccess = new ArrayList<Action>();
        actionSuccess.add(redledlow);
        actionSuccess.add(greenledhigh);
        actionSuccess.add(buzzerlow);
        success.setActions(actionSuccess);
        Transition tranSuc= new Transition();
        tranSuc.setNext(success);
        success.setTransition(tranSuc);


        //create state error, just three attempt
        State error = new State();
        error.setName("error");
        List<Action> actionError = new ArrayList<Action>();
        DigitalAction redledOfStateErr = new DigitalAction();
        redledOfStateErr.setActuator((DigitalActuator) this.bricks.get(3));// first digital actuator => led led
        redledOfStateErr.setValue(SIGNAL.LOW);
        actionError.add(redledOfStateErr);


        //create state fin
        State fin = new State();
        fin.setName("fin");
        List<Action> actionFin = new ArrayList<Action>();
        actionFin.add(redledlow);
        actionFin.add(greenledhlow);
        actionFin.add(buzzerhigh);
        fin.setActions(actionFin);
        Transition tranF = new Transition();
        tranF.setNext(fin);
        fin.setTransition(tranF);

        //create error wait state
        State errorWait = new State();
        errorWait.setName("errorwait");
        List<Action> actionErrorWait = new ArrayList<Action>();
        actionErrorWait.add(redledhigh);
        actionErrorWait.add(greenledhlow);
        actionErrorWait.add(buzzerlow);
        errorWait.setActions(actionErrorWait);
        //todo errorWait Transition

        List<String> code = new ArrayList<>();
        for (String s: konamicode.split("")){
            code.add(s);
        }

        //for each letter except the last one, create two states
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
            Transition transition = new Transition();
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

        this.states.add(success);
        this.states.add(error);
        this.states.add(errorWait);
        this.states.add(fin);

        // if the konami code is "UD", this states list is like {0 1 2 3 4 success error errorwait fin }
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
        app.setInitial(states.get(0));
        Visitor visitor = new ToWiring();
        app.accept(visitor);
        return visitor.getResult();
    }

    public Condition getCondition(String s){
        AnalogicalCondition condition = new AnalogicalCondition();

        switch (s){
            case "U":
                condition.setSensor((AnalogicalSensor) this.bricks.get(1));// Yaxis
                condition.setOperator(OPERATOR.GT);
                condition.setValueToCompare(1000); //todo  value - calY > threshold
            case "D":
                condition.setSensor((AnalogicalSensor) this.bricks.get(1));
                condition.setOperator(OPERATOR.GT);
                condition.setValueToCompare(10); // todo    calY - value > threshold
            case "R":
                condition.setSensor((AnalogicalSensor) this.bricks.get(0));//Xaxis
                condition.setOperator(OPERATOR.GT); //to   value - calX > threshold
                condition.setValueToCompare(1000);
            case"L":
                condition.setSensor((AnalogicalSensor) this.bricks.get(0)); //Xaxis
                condition.setOperator(OPERATOR.GT); // todo value - calX > threshold
                condition.setValueToCompare(10);
        }
        return condition;
    }

}
