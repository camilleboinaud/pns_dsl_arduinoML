package fr.polytech.unice.si5.konamiML.dsl

/**
 * Created by user on 09/02/16.
 */
abstract class KonamiMLBasescript extends Script{

    // anaSensor "Xaxis" pin 2
    def anaSensor(String name){
        [pin : { n -> ((KonamiMLBinding)this.getBinding()).getModel().createAnalogicalSensor(name, n)}]
    }

    def digActuator(String name){
        [pin : { n -> ((KonamiMLBinding)this.getBinding()).getModel().createDigitalActuator(name, n)}]
    }

    def digSensor(String name){
        [pin : {n -> ((KonamiMLBinding)this.getBinding()).getModel().createDigitalSensor(name,n)}]
    }

    def konami(String konamicode){
        ((KonamiMLBinding)this.getBinding()).getModel().createKonami(konamicode)
    }
    // def digital{
       // [pin: { n -> (this.getBinding()).getModel().createActuator(name, n) }]
    //}

    // generate name
    def generate(String name) {
        println(((KonamiMLBinding) this.getBinding()).getModel().generate(name).toString())
    }
}
