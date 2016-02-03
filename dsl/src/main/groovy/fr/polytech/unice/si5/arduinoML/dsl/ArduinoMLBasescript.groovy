package fr.polytech.unice.si5.arduinoML.dsl

import com.sun.org.apache.xpath.internal.operations.Bool
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.Action
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.AnalogicalAction
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.AnalogicalCondition
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.BOOLEAN
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.BooleanExpression
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.Condition
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.DigitalAction
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.DigitalCondition
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.Expression
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.OPERATOR
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.SimpleExpression
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.State
import fr.polytech.unice.si5.arduinoML.kernel.structural.AnalogicalActuator
import fr.polytech.unice.si5.arduinoML.kernel.structural.AnalogicalSensor
import fr.polytech.unice.si5.arduinoML.kernel.structural.DigitalActuator
import fr.polytech.unice.si5.arduinoML.kernel.structural.DigitalSensor
import fr.polytech.unice.si5.arduinoML.kernel.structural.SIGNAL
import fr.polytech.unice.si5.arduinoML.utils.BrickType
import fr.polytech.unice.si5.arduinoML.utils.UnitEnum

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

    def change(Map map){
        if(map.from != null && map.to != null){

            Expression expression = new SimpleExpression();
            ((ArduinoMLBinding) this.getBinding()).getModel().createTransition(map.from, map.to, expression)

            def closure

            closure = { sensor ->
                [became: { field ->
                    if (field instanceof SIGNAL) {
                        addExpressionToTransition(map.from, digCondition(sensor, (SIGNAL) field))
                        [and : {
                            moveToAddExpressionToTransition(map.from, BOOLEAN.AND)
                            closure
                        }, or: {
                            moveToAddExpressionToTransition(map.from, BOOLEAN.OR)
                            closure
                        }]
                    } else {
                        [value: { value ->
                            [using: { unit ->
                                addExpressionToTransition(map.from, anaCondition(sensor, (OPERATOR) field, value, unit))
                                [and : { newSensor ->
                                    moveToAddExpressionToTransition(map.from, BOOLEAN.AND)
                                    closure(newSensor)
                                }, or: { newSensor ->
                                    moveToAddExpressionToTransition(map.from, BOOLEAN.OR)
                                    closure(newSensor)
                                }]
                            }]
                        }]
                    }
                }]
            }
            [when: closure]
        }
    }

    def moveToAddExpressionToTransition(State state, BOOLEAN booleanOp){
        BooleanExpression tmp = new BooleanExpression()
        tmp.left = state.transition.expression
        tmp.booleanOperator = booleanOp

        state.transition.expression = tmp
    }

    def addExpressionToTransition(State state, Condition condition){
        if(state.transition.expression instanceof SimpleExpression
                && ((SimpleExpression)state.transition.expression).condition == null){
            ((SimpleExpression)state.transition.expression).condition = condition
        } else if(state.transition.expression instanceof BooleanExpression){
            SimpleExpression expression = new SimpleExpression()
            expression.condition = condition
            ((BooleanExpression)state.transition.expression).right = expression
        }
    }


    def digCondition(DigitalSensor sensor, SIGNAL signal){
        DigitalCondition condition = new DigitalCondition()
        condition.setSensor(sensor)
        condition.setSignal(signal)

        condition
    }

    def anaCondition(AnalogicalSensor sensor, OPERATOR operator, double value, UnitEnum unit){
        AnalogicalCondition condition = new AnalogicalCondition()
        condition.setSensor(sensor)
        condition.setOperator(operator)
        condition.setValueToCompare(convert(unit, value))

        condition
    }

    def initial(Map map){
        ((ArduinoMLBinding) this.getBinding()).getModel().createInitial(map.state)
    }


    def convert(UnitEnum unit, double value){
        def finalValue = value
        switch (unit){
            case UnitEnum.CELSIUS_DEGREE:
                finalValue = value * 20.48 + 102.4
                break;
            case UnitEnum.VOLT:
                break;
        }
        finalValue
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
