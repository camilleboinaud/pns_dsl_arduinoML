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

    public static final int THRESHOLD = 490;
    public static final int ORIGIN_XAIS = 512;
    public static final int ORIGIN_YAIS = 512;
    public static final int COUNTER = 3;
    public static final int DELAY_UNIT = 300;

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
        /*List<Transition> transitionsSuc = new ArrayList<>();
        Transition tranSuc= new Transition();
        tranSuc.setNext(success);
        transitionsSuc.add(tranSuc);
        success.setTransition(transitionsSuc);*/


        //create state error, just three attempt
        State error = new State();
        error.setName("error");
        List<Action> actionError = new ArrayList<Action>();
        DigitalAction redledOfStateErr = new DigitalAction();
        redledOfStateErr.setActuator(redled);// first digital actuator => led led
        redledOfStateErr.setValue(SIGNAL.LOW);
        actionError.add(redledOfStateErr);


        //create state success buzzer is on
        State fin = new State();
        fin.setName("exit");
        List<Action> actionFin = new ArrayList<Action>();
        actionFin.add(redledlow);
        actionFin.add(greenledhlow);
        actionFin.add(buzzerhigh);
        fin.setActions(actionFin);
       /* List<Transition> transitionsF = new ArrayList<>();
        Transition tranF = new Transition();
        tranF.setNext(fin);
        transitionsF.add(tranF);
        fin.setTransition(transitionsF);*/

        //create error wait state
        State errorWait = new State();
        errorWait.setName("errorwait");
        List<Action> actionErrorWait = new ArrayList<Action>();
        actionErrorWait.add(redledhigh);
        actionErrorWait.add(greenledhlow);
        actionErrorWait.add(buzzerlow);
        errorWait.setActions(actionErrorWait);


        List<String> code = new ArrayList<>();
        for (String s: konamicode.split("")){
            code.add(s);
        }

        //for each letter except the last one, create two states
        // This block is to create the states
        IntStream.range(0, code.size()).forEach(index -> {
            State state1 = new State();
            state1.setName("state" + index*2);
            if(index == 0){
                List<Action> actions = new ArrayList<Action>();
                actions.add(redledhigh);
                actions.add(greenledhlow);
                actions.add(buzzerlow);
                state1.setActions(actions);
            }
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
        //This block is to create the transitions between the states
        IntStream.range(0, code.size()).forEach(index -> {
            int currentStateIndex = 2 * index;
            int nextStateIndex = currentStateIndex + 1;
            Transition transition1 = new Transition();
            Expression expression = getExpression(code.get(index));
            transition1.setExpression(expression);
            List<Transition> transitionList = new ArrayList<>();
            transition1.setNext(states.get(nextStateIndex));
            transitionList.add(transition1);
            Transition transition2 = new Transition();
            Expression expression2 = getExpression("O" + code.get(index));
            transition2.setExpression(expression2);
            transition2.setNext(errorWait);
            transitionList.add(transition2);
            states.get(currentStateIndex).setTransition(transitionList);
            if(index < code.size() -1) {
                Transition transitionR = new Transition();
                transitionR.setExpression(getExpressionRepeat());
                transitionR.setNext(states.get(nextStateIndex+1));
                List<Transition> transitionListR = new ArrayList<>();
                transitionListR.add(transitionR);
                states.get(nextStateIndex).setTransition(transitionListR);
            } else {
                // wait for push, has two transitions
                List<Transition> transitionListPush = new ArrayList<>();
                Transition transitionPush1 = new Transition();
                transitionPush1.setExpression(getPushSuccessExpression());
                transitionPush1.setNext(success);
                Transition transitionPush2 = new Transition();
                transitionPush2.setExpression(getErrorExpression());
                transitionPush2.setNext(errorWait);
                transitionListPush.add(transitionPush1);
                transitionListPush.add(transitionPush2);
                states.get(nextStateIndex).setTransition(transitionListPush);
            }
        });

        // errorWait Transition
        List<Transition> transitionsErrorWait = new ArrayList<>();
        Transition transitionErrWait = new Transition();
        transitionErrWait.setExpression(getPushSuccessExpression());
        transitionErrWait.setNext(error);
        transitionsErrorWait.add(transitionErrWait);
        errorWait.setTransition(transitionsErrorWait);


        // error Transition
        //Error has two transitions depends on a counter
        List<Transition> transitionsErr = new ArrayList<>();
        Transition transitionTo1 = new Transition();
        transitionTo1.setNext(states.get(0));
        transitionTo1.setExpression(getExpressionTo1());
        Transition transitionToExit = new Transition();
        transitionToExit.setNext(fin);
        transitionToExit.setExpression(getExpressionToExit());
        transitionsErr.add(transitionTo1);
        transitionsErr.add(transitionToExit);
        error.setTransition(transitionsErr);

        //Set the actions of the Error state
        List<Action> errActions = new ArrayList<>();
        DelayAction delayAction1 = new DelayAction();
        delayAction1.setDelay(DELAY_UNIT);
        errActions.add(delayAction1);
        error.setActions(errActions);

    }

    @SuppressWarnings("rawtypes")
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

    private Expression getExpression(String s){
        Expression expression = null;
        AnalogicalCondition condition = new AnalogicalCondition();
        switch (s){
            case "U":
                condition.setSensor(yAxis);// Yaxis
                condition.setOperator(OPERATOR.GT);
                condition.setValueToCompare(ORIGIN_YAIS + THRESHOLD); //todo  value - calY > threshold
                expression = new SimpleExpression();
                ((SimpleExpression)expression).setCondition(condition);
                break;
            case "D":
                condition.setSensor(yAxis);
                condition.setOperator(OPERATOR.LT);
                condition.setValueToCompare(ORIGIN_YAIS - THRESHOLD); // todo    calY - value > threshold
                expression = new SimpleExpression();
                ((SimpleExpression)expression).setCondition(condition);
                break;
            case "R":
                condition.setSensor(xAxis);//Xaxis
                condition.setOperator(OPERATOR.GT); //to   value - calX > threshold
                condition.setValueToCompare(ORIGIN_XAIS + THRESHOLD);
                expression = new SimpleExpression();
                ((SimpleExpression)expression).setCondition(condition);
                break;
            case "L":
                condition.setSensor(xAxis); //Xaxis
                condition.setOperator(OPERATOR.LT); // todo value - calX > threshold
                condition.setValueToCompare(ORIGIN_YAIS - THRESHOLD);
                expression = new SimpleExpression();
                ((SimpleExpression)expression).setCondition(condition);
                break;
            case "OU":case "OD" :case "OR" :case "OL" : // The direction is not clear
                BooleanExpression booleanExpression = new BooleanExpression();
                BooleanExpression expressionLeft = new BooleanExpression();
                SimpleExpression expression1 = new SimpleExpression();
                SimpleExpression expression2 = new SimpleExpression();
                SimpleExpression expression3 = new SimpleExpression();
                SimpleExpression expression4 = new SimpleExpression();

                // condition for the direction right
                if(!s.endsWith("R")) {
                    AnalogicalCondition condition1 = new AnalogicalCondition();
                    condition1.setSensor(xAxis);
                    condition1.setOperator(OPERATOR.GT);
                    condition1.setValueToCompare(ORIGIN_XAIS + THRESHOLD);
                    expression1.setCondition(condition1);
                }
                // condition for the direction left
                if(!s.endsWith("L")) {
                    AnalogicalCondition condition2 = new AnalogicalCondition();
                    condition2.setSensor(xAxis);
                    condition2.setOperator(OPERATOR.LT);
                    condition2.setValueToCompare(ORIGIN_XAIS - THRESHOLD);
                    expression2.setCondition(condition2);
                }
                // condition for the direction UP
                if(!s.endsWith("U")) {
                    AnalogicalCondition condition3 = new AnalogicalCondition();
                    condition3.setSensor(yAxis);
                    condition3.setOperator(OPERATOR.GT);
                    condition3.setValueToCompare(ORIGIN_YAIS + THRESHOLD);
                    expression3.setCondition(condition3);
                }
                // condition for the direction DOWN
                    if(!s.endsWith("D")) {
                        AnalogicalCondition condition4 = new AnalogicalCondition();
                        condition4.setSensor(yAxis);
                        condition4.setOperator(OPERATOR.LT);
                        condition4.setValueToCompare(ORIGIN_YAIS - THRESHOLD);
                        expression4.setCondition(condition4);
                    }
                if(expression1.getCondition()==null) {
                    expressionLeft.setLeft(expression2);
                    expressionLeft.setRight(expression3);
                    expressionLeft.setBooleanOperator(BOOLEAN.OR);
                    booleanExpression.setLeft(expressionLeft);
                    booleanExpression.setRight(expression4);
                    booleanExpression.setBooleanOperator(BOOLEAN.OR);
                } else if(expression2.getCondition()==null) {
                    expressionLeft.setLeft(expression1);
                    expressionLeft.setRight(expression3);
                    expressionLeft.setBooleanOperator(BOOLEAN.OR);
                    booleanExpression.setLeft(expressionLeft);
                    booleanExpression.setRight(expression4);
                    booleanExpression.setBooleanOperator(BOOLEAN.OR);
                } else if(expression3.getCondition()==null) {
                    expressionLeft.setLeft(expression1);
                    expressionLeft.setRight(expression2);
                    expressionLeft.setBooleanOperator(BOOLEAN.OR);
                    booleanExpression.setLeft(expressionLeft);
                    booleanExpression.setRight(expression4);
                    booleanExpression.setBooleanOperator(BOOLEAN.OR);
                } else {
                    expressionLeft.setLeft(expression1);
                    expressionLeft.setRight(expression3);
                    expressionLeft.setBooleanOperator(BOOLEAN.OR);
                    booleanExpression.setLeft(expressionLeft);
                    booleanExpression.setRight(expression2);
                    booleanExpression.setBooleanOperator(BOOLEAN.OR);
                }
                expression = booleanExpression;
                break;
        }
        return expression;
    }

    private Expression getExpressionRepeat() {
        BooleanExpression booleanExpression = new BooleanExpression();
        BooleanExpression expressionLeft = new BooleanExpression();
        BooleanExpression expressionRight = new BooleanExpression();
        SimpleExpression expression1 = new SimpleExpression();
        SimpleExpression expression2 = new SimpleExpression();
        SimpleExpression expression3 = new SimpleExpression();
        SimpleExpression expression4 = new SimpleExpression();

        // condition for the direction right
            AnalogicalCondition condition1 = new AnalogicalCondition();
            condition1.setSensor(xAxis);
            condition1.setOperator(OPERATOR.LT);
            condition1.setValueToCompare(ORIGIN_XAIS + THRESHOLD);
            expression1.setCondition(condition1);

        // condition for the direction left
            AnalogicalCondition condition2 = new AnalogicalCondition();
            condition2.setSensor(xAxis);
            condition2.setOperator(OPERATOR.GT);
            condition2.setValueToCompare(ORIGIN_XAIS - THRESHOLD);
            expression2.setCondition(condition2);
        // condition for the direction UP
            AnalogicalCondition condition3 = new AnalogicalCondition();
            condition3.setSensor(yAxis);
            condition3.setOperator(OPERATOR.LT);
            condition3.setValueToCompare(ORIGIN_YAIS + THRESHOLD);
            expression3.setCondition(condition3);
        // condition for the direction DOWN
            AnalogicalCondition condition4 = new AnalogicalCondition();
            condition4.setSensor(yAxis);
            condition4.setOperator(OPERATOR.GT);
            condition4.setValueToCompare(ORIGIN_YAIS - THRESHOLD);
            expression4.setCondition(condition4);

            expressionLeft.setLeft(expression1);
            expressionLeft.setRight(expression2);
            expressionLeft.setBooleanOperator(BOOLEAN.AND);
            expressionRight.setLeft(expression3);
            expressionRight.setRight(expression4);
            expressionRight.setBooleanOperator(BOOLEAN.AND);

            booleanExpression.setLeft(expressionLeft);
            booleanExpression.setRight(expressionRight);
            booleanExpression.setBooleanOperator(BOOLEAN.AND);

        return booleanExpression;
    }

    private Expression getPushSuccessExpression() {
        SimpleExpression expression = new SimpleExpression();
        DigitalCondition digitalCondition = new DigitalCondition();
        digitalCondition.setSensor(button);
        digitalCondition.setSignal(SIGNAL.HIGH);
        expression.setCondition(digitalCondition);
        return expression;
    }

    private Expression getErrorExpression() {
        BooleanExpression booleanExpression = new BooleanExpression();
        BooleanExpression expressionLeft = new BooleanExpression();
        BooleanExpression expressionRight = new BooleanExpression();
        SimpleExpression expression1 = new SimpleExpression();
        SimpleExpression expression2 = new SimpleExpression();
        SimpleExpression expression3 = new SimpleExpression();
        SimpleExpression expression4 = new SimpleExpression();

        // condition for the direction right
        AnalogicalCondition condition1 = new AnalogicalCondition();
        condition1.setSensor(xAxis);
        condition1.setOperator(OPERATOR.GT);
        condition1.setValueToCompare(ORIGIN_XAIS + THRESHOLD);
        expression1.setCondition(condition1);

        // condition for the direction left
        AnalogicalCondition condition2 = new AnalogicalCondition();
        condition2.setSensor(xAxis);
        condition2.setOperator(OPERATOR.LT);
        condition2.setValueToCompare(ORIGIN_XAIS - THRESHOLD);
        expression2.setCondition(condition2);
        // condition for the direction UP
        AnalogicalCondition condition3 = new AnalogicalCondition();
        condition3.setSensor(yAxis);
        condition3.setOperator(OPERATOR.GT);
        condition3.setValueToCompare(ORIGIN_YAIS + THRESHOLD);
        expression3.setCondition(condition3);
        // condition for the direction DOWN
        AnalogicalCondition condition4 = new AnalogicalCondition();
        condition4.setSensor(yAxis);
        condition4.setOperator(OPERATOR.LT);
        condition4.setValueToCompare(ORIGIN_YAIS - THRESHOLD);
        expression4.setCondition(condition4);

        expressionLeft.setLeft(expression1);
        expressionLeft.setRight(expression2);
        expressionLeft.setBooleanOperator(BOOLEAN.OR);
        expressionRight.setLeft(expression3);
        expressionRight.setRight(expression4);
        expressionRight.setBooleanOperator(BOOLEAN.OR);

        booleanExpression.setLeft(expressionLeft);
        booleanExpression.setRight(expressionRight);
        booleanExpression.setBooleanOperator(BOOLEAN.OR);

        return booleanExpression;
    }

    private Expression getExpressionTo1() {
        SimpleExpression expression = new SimpleExpression();
        AnalogicalCondition analogicalCondition = new AnalogicalCondition();
        analogicalCondition.setOperator(OPERATOR.LT);
        analogicalCondition.setValueToCompare(COUNTER);
        expression.setCondition(analogicalCondition);
        return expression;
    }

    private Expression getExpressionToExit() {
        SimpleExpression expression = new SimpleExpression();
        AnalogicalCondition analogicalCondition = new AnalogicalCondition();
        analogicalCondition.setOperator(OPERATOR.GE);
        analogicalCondition.setValueToCompare(COUNTER);
        expression.setCondition(analogicalCondition);
        return expression;
    }

}
