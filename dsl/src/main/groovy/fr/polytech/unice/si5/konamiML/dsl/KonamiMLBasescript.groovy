package fr.polytech.unice.si5.konamiML.dsl

/**
 * Created by user on 09/02/16.
 */
abstract class KonamiMLBasescript extends Script{

    def xAxis(String name){
        [pin : { n -> ((KonamiMLBinding)this.getBinding()).getModel().createXAxis(name, n)}]
    }

    def yAxis(String name){
        [pin : { n -> ((KonamiMLBinding)this.getBinding()).getModel().createYAxis(name, n)}]
    }

    def button(String name){
        [pin : { n -> ((KonamiMLBinding)this.getBinding()).getModel().createButton(name, n)}]
    }

    def redLed(String name){
        [pin : { n -> ((KonamiMLBinding)this.getBinding()).getModel().createRedLed(name, n)}]
    }

    def greenLed(String name){
        [pin : { n -> ((KonamiMLBinding)this.getBinding()).getModel().createGreenLed(name, n)}]

    }

    def buzzer(String name){
        [pin : { n -> ((KonamiMLBinding)this.getBinding()).getModel().createBuzzer(name, n)}]
    }

    def konami(String konamicode){
        ((KonamiMLBinding)this.getBinding()).getModel().createKonami(konamicode)
    }

    // generate name
    def generate(String name) {
        println(((KonamiMLBinding) this.getBinding()).getModel().generate(name).toString())
    }
}
