/**
 * Created by Quentin on 1/12/2016.
 */

sensor "button" pin 9
led "led" pin 12
buzzer "buzzer" pin 13

state "on" means led becomes high and buzzer becomes high
state "off" means led becomes low and buzzer becomes low

initial off

from on to off when button becomes high
from off to on when button becomes high

export "VerySimpleAlarm!"