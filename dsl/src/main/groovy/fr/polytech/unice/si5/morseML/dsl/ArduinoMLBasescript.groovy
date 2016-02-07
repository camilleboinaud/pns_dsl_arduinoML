package fr.polytech.unice.si5.morseML.dsl

abstract class ArduinoMLBasescript extends Script{
    // actuator "name" pin n
    def actuator(String name) {
        [pin : { n -> ((ArduinoMLBinding)this.getBinding()).getModel().createActuator(name, n)}]
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
