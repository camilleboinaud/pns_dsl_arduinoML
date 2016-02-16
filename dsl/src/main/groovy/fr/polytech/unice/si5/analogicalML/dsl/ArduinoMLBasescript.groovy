package fr.polytech.unice.si5.analogicalML.dsl

import fr.polytech.unice.si5.kernel.behavioral.BOOLEAN
import fr.polytech.unice.si5.kernel.behavioral.BooleanExpression
import fr.polytech.unice.si5.kernel.behavioral.Condition
import fr.polytech.unice.si5.kernel.behavioral.DigitalAction
import fr.polytech.unice.si5.kernel.behavioral.OPERATOR
import fr.polytech.unice.si5.kernel.behavioral.SimpleExpression
import fr.polytech.unice.si5.kernel.behavioral.Transition
import fr.polytech.unice.si5.kernel.structural.AnalogicalSensor
import fr.polytech.unice.si5.kernel.structural.DigitalActuator
import fr.polytech.unice.si5.kernel.structural.DigitalSensor
import fr.polytech.unice.si5.analogicalML.utils.LinearUnitEnum
import fr.polytech.unice.si5.kernel.behavioral.Action
import fr.polytech.unice.si5.kernel.behavioral.AnalogicalAction
import fr.polytech.unice.si5.kernel.behavioral.AnalogicalCondition
import fr.polytech.unice.si5.kernel.behavioral.DigitalCondition
import fr.polytech.unice.si5.kernel.behavioral.State
import fr.polytech.unice.si5.kernel.structural.AnalogicalActuator
import fr.polytech.unice.si5.kernel.structural.SIGNAL

/**
 * Created by cboinaud on 26/01/16.
 */
abstract class ArduinoMLBasescript extends Script{

    def digital(Map map){
        if(map.actuator != null) {
            [on: {
                Map on -> ((ArduinoMLBinding) this.getBinding()).getModel().createDigitalActuator(map.actuator, on.pin)
            }]
        } else if (map.sensor != null) {
            [on: {
                Map on -> ((ArduinoMLBinding) this.getBinding()).getModel().createDigitalSensor(map.sensor, on.pin)
            }]
        }
    }

    def analogical(Map map){
        if(map.actuator != null) {
            [on: {
                Map on -> ((ArduinoMLBinding)this.getBinding()).getModel().createAnalogicalActuator(map.actuator, on.pin);
            }]
        } else if (map.sensor != null) {
            [on: {
                Map on -> ((ArduinoMLBinding)this.getBinding()).getModel().createAnalogicalSensor(map.sensor, on.pin);
            }]
        }
    }

    def state(String name) {
        List<Action> actions = new ArrayList<Action>()
        State state = ((ArduinoMLBinding) this.getBinding()).getModel().createState(name, actions)
        // recursive closure to allow multiple and statements
        def closure
        closure = { actuator ->
            [becomes: { signal ->
                state.actions.add(createAction(actuator, signal))
                [and: closure]
            }]
        }
        [means: closure]
    }

    def from(State first){

        Transition transition = new Transition();

        def closure

        closure = { sensor ->
            [became: { field ->
                if (field instanceof SIGNAL) {
                    addExpressionToTransition(first, transition, digCondition(sensor, (SIGNAL) field))
                    [and : {
                        moveToAddExpressionToTransition(first, transition, BOOLEAN.AND)
                        closure
                    }, or: {
                        moveToAddExpressionToTransition(first, transition, BOOLEAN.OR)
                        closure
                    }]
                } else {
                    [value: { value ->
                        addExpressionToTransition(first, transition, anaCondition(sensor, (OPERATOR) field, value))
                        [and : { newSensor ->
                            moveToAddExpressionToTransition(first, transition, BOOLEAN.AND)
                            closure(newSensor)
                        }, or: { newSensor ->
                            moveToAddExpressionToTransition(first, transition, BOOLEAN.OR)
                            closure(newSensor)
                        }]
                    }]
                }
            }]
        }

        [to: { second ->

            transition.next = second
            transition.expression = new SimpleExpression();
            first.transition.add(transition)

            [when: closure]
        }]

    }

    def moveToAddExpressionToTransition(State state, Transition transition, BOOLEAN booleanOp){
        state.getTransition().remove(transition)
        BooleanExpression tmp = new BooleanExpression()
        tmp.left = transition.expression
        tmp.booleanOperator = booleanOp

        transition.expression = tmp
        state.getTransition().add(transition)
        transition
    }

    def addExpressionToTransition(State state, Transition transition, Condition condition){
        state.getTransition().remove(transition)
        if(transition.expression instanceof SimpleExpression
                && ((SimpleExpression)transition.expression).condition == null){
            ((SimpleExpression)transition.expression).condition = condition
        } else if(transition.expression instanceof BooleanExpression){
            SimpleExpression expression = new SimpleExpression()
            expression.condition = condition
            ((BooleanExpression)transition.expression).right = expression
        }
        state.getTransition().add(transition)
        transition
    }


    def digCondition(DigitalSensor sensor, SIGNAL signal){
        DigitalCondition condition = new DigitalCondition()
        condition.setSensor(sensor)
        condition.setSignal(signal)

        condition
    }

    def anaCondition(AnalogicalSensor sensor, OPERATOR operator, double value){
        AnalogicalCondition condition = new AnalogicalCondition()
        condition.setSensor(sensor)
        condition.setOperator(operator)
        condition.setValueToCompare(value)

        condition
    }

    def initial(Map map){
        ((ArduinoMLBinding) this.getBinding()).getModel().createInitial(map.state)
    }


    def createAction(DigitalActuator actuator, SIGNAL value){
        Action action = new DigitalAction()
        action.setActuator(actuator)
        action.setValue(value)
        action
    }

    def createAction(AnalogicalActuator actuator, double value){
        Action action = new AnalogicalAction()
        action.setActuator(actuator)
        action.setValue(value)
        action
    }

    // morse "name"
    def morse(String name) {
        ((ArduinoMLBinding)this.getBinding()).getModel().createMorse(name)
    }

    // generate name
    def generate(String name) {
        println(((ArduinoMLBinding) this.getBinding()).getModel().generate(name).toString())
    }
}
