package groovuinoml.dsl

import kernel.behavioral.BooleanExpression
import kernel.structural.Moment
import kernel.structural.MomentUnit
import kernel.structural.SIGNAL
import org.codehaus.groovy.control.CompilerConfiguration


class GroovuinoMLDSL {
	private GroovyShell shell
	private CompilerConfiguration configuration
	private GroovuinoMLBinding binding
	private GroovuinoMLBasescript basescript
	
	GroovuinoMLDSL() {
		binding = new GroovuinoMLBinding()
		binding.setGroovuinoMLModel(new GroovuinoMLModel(binding));
		configuration = new CompilerConfiguration()
		configuration.setScriptBaseClass("groovuinoml.dsl.GroovuinoMLBasescript")
		shell = new GroovyShell(configuration)

		binding.setVariable("high", SIGNAL.HIGH)
		binding.setVariable("low", SIGNAL.LOW)
		binding.setVariable("and", BooleanExpression.AND)
		binding.setVariable("or", BooleanExpression.OR)

		Number.metaClass.getS = { ->
			new Moment(delegate, MomentUnit.second)
		}
	}
	
	void eval(File scriptFile) {
		Script script = shell.parse(scriptFile)
		
		binding.setScript(script)
		script.setBinding(binding)
		
		script.run()
	}
}
