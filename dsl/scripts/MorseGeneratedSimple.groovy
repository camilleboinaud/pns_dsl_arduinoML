led "led" pin 13


state "off" means led becomes low
state "sOn1" means led becomes high
state "sWait1" means led becomes low
state "sOn2" means led becomes high
state "sWait2" means led becomes low
state "sOn3" means led becomes high
//state "sWait3" means led becomes low
state "sOff1" means led becomes low

state "OOn1" means led becomes high
state "OWait1" means led becomes low

// Note : Penser a mettre une transition pour chacun des états sinon NullPointerException
initial off
//Lettre S
from off to sOn1 when 3.s
from sOn1 to sWait1 when 3.s
from sWait1 to sOn2 when 3.s
from sOn2 to sWait2 when 3.s
from sWait2 to sOn3 when 3.s

//fin de la lettre S
from sOn3 to sOff1 when 3.s
//D�but de la lettre 0
from sOff1 to OOn1 when 3.s
from OOn1 to OWait1 when 6.s

from OWait1 to off when 3.s

//final : on passe � off, ce qui nous fait boucler.

export "MultiStateAlarm!"