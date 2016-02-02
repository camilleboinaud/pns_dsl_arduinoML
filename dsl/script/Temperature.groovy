import fr.polytech.unice.si5.arduinoML.kernel.behavioral.OPERATOR
import fr.polytech.unice.si5.arduinoML.utils.UnitEnum

/**
 * Created by cboinaud on 26/01/16.
 */

digital actuator: "buzzer" on pin: 9
digital sensor: "button" on pin: 8
analogical sensor: "temperature" on pin: 4 with minValue: 0 and maxValue: 40 using degrees
analogical actuator: "led" on pin: 10 with minValue: 0 and maxValue: 100 using volts

state "off" means BUZZER becomes low and LED becomes 10
state "on" means BUZZER becomes high

initial state : OFF

change from: OFF, to: ON when TEMPERATURE became greater_than value 20 using degrees or TEMPERATURE became lower_than value 10 using degrees
change from: ON, to: OFF when TEMPERATURE became lower_than value 20 using degrees and TEMPERATURE became equal_to value 30 using degrees

generate("Temperature");