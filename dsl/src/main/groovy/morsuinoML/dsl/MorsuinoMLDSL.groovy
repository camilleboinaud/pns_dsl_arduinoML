package morsuinoML.dsl

import kernel.behavioral.BooleanExpression
import kernel.structural.SIGNAL
import org.codehaus.groovy.control.CompilerConfiguration

class MorsuinoMLDSL {
	private GroovyShell shell
	private CompilerConfiguration configuration
	private MorsuinoMLBinding binding
	private MorsuinoMLBasescript basescript

	MorsuinoMLDSL() {
		binding = new MorsuinoMLBinding()
		binding.setMorsuinoMLModel(new MorsuinoMLModel(binding));
		configuration = new CompilerConfiguration()
		configuration.setScriptBaseClass("morsuinoML.dsl.MorsuinoMLBasescript")
		shell = new GroovyShell(configuration)
		
		binding.setVariable("high", SIGNAL.HIGH)
		binding.setVariable("low", SIGNAL.LOW)
		binding.setVariable("and", BooleanExpression.AND)
	}
	
	void eval(File scriptFile) {
		Script script = shell.parse(scriptFile)
		
		binding.setScript(script)
		script.setBinding(binding)
		
		script.run()
	}
}
