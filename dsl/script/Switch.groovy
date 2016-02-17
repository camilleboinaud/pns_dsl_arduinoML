/**
 * Created by cboinaud on 26/01/16.
 */

analogical sensor: "rotation" on pin: 3
digital actuator: "led" on pin: 9

state "off" means LED becomes low
state "on" means LED becomes high

initial state : OFF

from OFF to ON when ROTATION became greater_than value 50.deg
from ON to OFF when ROTATION became lower_than value 50.deg

generate "Switch"