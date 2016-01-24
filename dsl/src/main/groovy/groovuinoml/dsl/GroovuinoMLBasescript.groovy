package groovuinoml.dsl
import kernel.behavioral.Action
import kernel.behavioral.BooleanExpression
import kernel.behavioral.State
import kernel.structural.Moment
import kernel.structural.SIGNAL
import kernel.structural.Sensor

abstract class GroovuinoMLBasescript extends Script {
	// sensor "name" pin n
	def sensor(String name) {
		[pin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createSensor(name, n) }]
	}
	
	// buzzer "name" pin n
	def buzzer(String name) {
		[pin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createBuzzer(name, n) }]
	}

	// led "name" pin n
	def led(String name) {
		[pin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createLed(name, n) }]
	}
	
	// state "name" means actuator becomes signal [and actuator becomes signal]*n
	def state(String name) {
		List<Action> actions = new ArrayList<>()
		((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createState(name, actions)
		// recursive closure to allow multiple and statements
		def closure
		closure = { actuator -> 
			[becomes: { signal ->
				Action action = new Action()
				action.setActuator(actuator)
				action.setValue(signal)
				actions.add(action)
				[and: closure]
			}]
		}
		[means: closure]
	}
	
	// initial state
	def initial(State state) {
		((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().setInitialState(state)
	}
	
	// from state1 to state2 when sensor becomes signal [and sensor2 becomes signal2]
	def from(State state1) {
        List<Sensor> sensors = new ArrayList<>()
        List<SIGNAL> signals = new ArrayList<>()
		List<BooleanExpression> booleanExpressions = new ArrayList<>()

        def closure
		[to: { state2 ->
			[when: closure = { transitionBegin ->
				if(transitionBegin instanceof Sensor) {
					((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createConditionalTransition(state1,state2, booleanExpressions, sensors,signals)

					[becomes: { signal, bool = BooleanExpression.AND ->
						print "$signal"
						sensors.add(transitionBegin)
						signals.add(signal)
						booleanExpressions.add(bool)
						[when: closure]
					}]
				} else if( transitionBegin instanceof Moment) {
					((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createTimerTransition(state1, state2, transitionBegin)
				}
			}]

		}]
	}

	def when(Sensor sensor) {
		def closure;

	}
	
	// export name
	def export(String name) {
		println(((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().generateCode(name).toString())
	}
}
