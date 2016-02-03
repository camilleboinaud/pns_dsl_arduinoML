/**
 * Created by cboinaud on 26/01/16.
 */

analogical sensor: "rotation" on pin: 3 with minValue: 0 and maxValue: 1000 using volts
digital actuator: "led" on pin: 9

state "off" means LED becomes low
state "on" means LED becomes high

initial state : OFF

change from: OFF, to: ON when ROTATION became greater_than value 500 using volts
change from: ON, to: OFF when ROTATION became lower_than value 500 using volts

generate("Switch");