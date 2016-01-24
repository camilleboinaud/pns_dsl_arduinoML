package morsuinoML.dsl


import kernel.behavioral.State
import kernel.structural.Actuator

abstract class MorsuinoMLBasescript extends Script {

	// buzzer "name" pin n
	def buzzer(String name) {
		System.out.println("saw a buzzer");
		[pin: { n -> ((MorsuinoMLBinding)this.getBinding()).getMorsuinoMLModel().addActuator(name)
					((MorsuinoMLBinding)this.getBinding()).getMorsuinoMLModel().addBuzzerDeclaration(name, n);
		}]
		//[pin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createBuzzer(name, n) }]

	}

	// led "name" pin n
	def led(String name) {
		[pin: { n -> ((MorsuinoMLBinding)this.getBinding()).getMorsuinoMLModel().addActuator(name)
			((MorsuinoMLBinding)this.getBinding()).getMorsuinoMLModel().addLedDeclaration(name, n);}]
	}
	
	// initial state
	def initial(State state) {
		((MorsuinoMLBinding) this.getBinding()).getMorsuinoMLModel().setInitialState(state)
	}

	// export name
	def export(String name) {
		System.out.println("Export being called");
		println(((MorsuinoMLBinding) this.getBinding()).getMorsuinoMLModel().generateCode(name).toString())
	}


    def translate(String message) {
		System.out.println("I see translate but what should i do");
		/*List<Actuator> morseActuators = new ArrayList<>()

		((MorsuinoMLBinding)this.getBinding()).getMorsuinoMLModel().createMorse(message, morseActuators)
		[into: { language ->
			def closure
            [with: closure = { actuator ->
				morseActuators.add(actuator)
				[and: closure]
            }]
        }]
*/

    }
}
