/**
 * Created by Quentin on 1/12/2016.
 */

sensor "button1" pin 8
sensor "button2" pin 9
buzzer "buzzer" pin 12

state "on" means buzzer becomes high
state "off" means buzzer becomes low

initial off

from on to off when button1 becomes high, and when button2 becomes high
from off to on when button1 becomes low, or when button2 becomes low

export "DualCheckAlarm"

