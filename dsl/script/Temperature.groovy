/**
 * Created by cboinaud on 26/01/16.
 */

digital actuator: "BUZZER" on pin: 9
digital sensor: "BUTTON" on pin: 8
analogical sensor: "TEMPERATURE" on pin: 4
analogical actuator: "LED" on pin: 10

state "OFF" means BUZZER becomes low and LED becomes 10
state "ON" means BUZZER becomes high
state "OTHER" means BUZZER becomes high and LED becomes 50

initial state : OFF

//from OFF to ON when TEMPERATURE == 20.deg

from OFF to ON when TEMPERATURE became greater_than value 20.deg or TEMPERATURE became lower_than value 10.deg
from OFF to OTHER when TEMPERATURE became greater_than value 50.deg or TEMPERATURE became lower_than value 0.deg
from ON to OFF when TEMPERATURE became lower_than value 20.deg and TEMPERATURE became lower_than value 30.deg

generate "Temperature"