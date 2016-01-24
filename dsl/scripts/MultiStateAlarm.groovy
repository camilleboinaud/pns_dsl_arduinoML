sensor "button" pin 9
led "led" pin 12
buzzer "buzz" pin 13

state "buzzOn" means buzz becomes high
state "ledOn" means led becomes high and buzz becomes low
state "off" means led becomes low

initial off

from off to buzzOn when button becomes high
from buzzOn to ledOn when button becomes high
from ledOn to off when button becomes high

export "MultiStateAlarm!"


state a composed onShort then offShort thn offLong with buzzer1 and buzzer2

state onshort means