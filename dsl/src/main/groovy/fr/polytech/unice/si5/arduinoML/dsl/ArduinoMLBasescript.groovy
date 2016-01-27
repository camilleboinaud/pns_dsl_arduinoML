package fr.polytech.unice.si5.arduinoML.dsl

import fr.polytech.unice.si5.arduinoML.kernel.behavioral.Action
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.AnalogicalAction
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.AnalogicalCondition
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.BOOLEAN
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.BooleanExpression
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
                Map on -> [with: {
                    Map with -> [and: {
                        Map and -> [using: {
                            degrees -> ((ArduinoMLBinding) this.getBinding()).getModel()
                                    .createAnalogicalActuator(map.actuator, on.pin, convert(degrees, with.minValue), convert(degrees,and.maxValue))
                        }]
                    }]
                }]
            }]
        } else if (map.sensor != null) {
            [on: {
                Map on -> [with: {
                    Map with -> [and: {
                        Map and -> [using: {
                            degrees -> ((ArduinoMLBinding) this.getBinding()).getModel()
                                    .createAnalogicalSensor(map.sensor, on.pin, convert(degrees, with.minValue), convert(degrees,and.maxValue))
                        }]
                    }]
                }]
            }]
        }
    }

    def state(String name) {
        List<Action> actions = new ArrayList<Action>()
        ((ArduinoMLBinding) this.getBinding()).getModel().createState(name, actions)
        // recursive closure to allow multiple and statements
        def closure
        closure = { actuator ->
            [becomes: { signal ->
                actions.add(createAction(actuator, signal))
                [and: closure]
            }]
        }
        [means: closure]
    }

    def change(Map map){
        if(map.from != null && map.to != null){

            Expression expression = new SimpleExpression();
            closure = { sensor ->
                [became: { operator -> println("Haha")
                   /* if(expression == null){

                        expression = new SimpleExpression()
                        expression.condition = digCondition(sensor, operator)
                    } else {
                        ((BooleanExpression)expression).right = digCondition(sensor, operator)
                    }*/
                   /* [value: { value ->
                        *//*[using: { unit ->
                           *//**//*if(expression == null){
                                expression = new SimpleExpression()
                                expression.condition = anaCondition(sensor, operator, value, unit)
                            } else {
                                ((BooleanExpression)expression).right = anaCondition(sensor, operator, value, unit)
                            }*//**//*
                            [and: {
                                *//**//*BooleanExpression tmp = new BooleanExpression()
                                tmp.left = expression
                                tmp.booleanOperator = BOOLEAN.AND
                                expression = tmp*//**//*
                                closure
                            }, or: {
                                *//**//*BooleanExpression tmp = new BooleanExpression()
                                tmp.left = expression
                                tmp.booleanOperator = BOOLEAN.OR
                                expression = tmp*//**//*
                                closure
                            }]
                        }]*//*
                    }, and: {
                        *//*BooleanExpression tmp = new BooleanExpression()
                        tmp.left = expression
                        tmp.booleanOperator = BOOLEAN.AND
                        expression = tmp*//*
                        closure
                    }, or: {
                        *//*BooleanExpression tmp = new BooleanExpression()
                        tmp.left = expression
                        tmp.booleanOperator = BOOLEAN.OR
                        expression = tmp*//*
                        closure
                    }]*/
                }]
            }
            [when: closure]

            ((ArduinoMLBinding) this.getBinding()).getModel().createTransition(map.from, map.to, expression)
        }
    }

    def digCondition(DigitalSensor sensor, SIGNAL signal){
        new DigitalCondition(sensor, signal)
    }

    def anaCondition(AnalogicalSensor sensor, OPERATOR operator, double value, UnitEnum unit){
        new AnalogicalCondition(sensor, operator, convert(unit, value))
    }

    def initial(Map map){
        ((ArduinoMLBinding) this.getBinding()).getModel().createInitial(map.state)
    }


    def convert(UnitEnum unit, double value){
        def finalValue = value
        switch (unit){
            case UnitEnum.CELSIUS_DEGREE:
                finalValue = 2048.0 * (0.05 + value)
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
