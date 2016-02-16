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

    private AnalogicalSensor xAxis;
    private AnalogicalSensor yAxis;
    private DigitalSensor button;
    private DigitalActuator redled;
    private DigitalActuator greenled;
    private DigitalActuator buzzer;

    public KonamiMLModel(Binding binding){
        this.binding = binding;
    }


    public  void createXAxis(String name, int pin){
        xAxis = new AnalogicalSensor();
        xAxis.setName(name);
        xAxis.setPin(pin);
        this.bricks.add(xAxis);
    }

    public  void createYAxis(String name, int pin){
        yAxis = new AnalogicalSensor();
        yAxis.setName(name);
        yAxis.setPin(pin);
        this.bricks.add(yAxis);
    }


    public void createButton(String name, int pin){
         button = new DigitalSensor();
        button.setName(name);
        button.setPin(pin);
        this.bricks.add(button);
    }

    public void createRedLed(String name,int pin){
        redled = new DigitalActuator();
        redled.setName(name);
        redled.setPin(pin);
        this.bricks.add(redled);
    }

    public void createGreenLed(String name,int pin){
        greenled = new DigitalActuator();
        greenled.setName(name);
        greenled.setPin(pin);
        this.bricks.add(greenled);
    }

    public void createBuzzer(String name,int pin){
        buzzer = new DigitalActuator();
        buzzer.setName(name);
        buzzer.setPin(pin);
        this.bricks.add(buzzer);
    }


    public void createKonami(String konamicode){


        DigitalAction redledlow = new DigitalAction();
        redledlow.setActuator(redled);// first digital actuator => led led
        redledlow.setValue(SIGNAL.LOW);

        DigitalAction redledhigh = new DigitalAction();
        redledhigh.setActuator(redled);  // first digital actuator => led led
        redledhigh.setValue(SIGNAL.HIGH);

        DigitalAction greenledhlow = new DigitalAction(); // second digital actuator => green led
        greenledhlow.setActuator(greenled);
        greenledhlow.setValue(SIGNAL.LOW);

        DigitalAction greenledhigh = new DigitalAction(); // second digital actuator => green led
        greenledhigh.setActuator(greenled);
        greenledhigh.setValue(SIGNAL.HIGH);

        DigitalAction buzzerlow = new DigitalAction(); //third digital actuator => buzzer
        buzzerlow.setActuator(buzzer);
        buzzerlow.setValue(SIGNAL.LOW);

        DigitalAction buzzerhigh = new DigitalAction(); //third digital actuator => buzzer
        buzzerhigh.setActuator(buzzer);
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
        redledOfStateErr.setActuator(redled);// first digital actuator => led led
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
            actions.add(redledhigh);
            actions.add(greenledhlow);
            actions.add(buzzerlow);
            Transition transition = new Transition();
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
                condition.setSensor(yAxis);// Yaxis
                condition.setOperator(OPERATOR.GT);
                condition.setValueToCompare(1000); //todo  value - calY > threshold
            case "D":
                condition.setSensor(yAxis);
                condition.setOperator(OPERATOR.GT);
                condition.setValueToCompare(10); // todo    calY - value > threshold
            case "R":
                condition.setSensor(xAxis);//Xaxis
                condition.setOperator(OPERATOR.GT); //to   value - calX > threshold
                condition.setValueToCompare(1000);
            case"L":
                condition.setSensor(xAxis); //Xaxis
                condition.setOperator(OPERATOR.GT); // todo value - calX > threshold
                condition.setValueToCompare(10);
        }
        return condition;
    }

}
