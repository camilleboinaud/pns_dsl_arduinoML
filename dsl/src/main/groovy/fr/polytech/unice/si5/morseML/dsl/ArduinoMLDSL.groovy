package fr.polytech.unice.si5.morseML.dsl

import org.codehaus.groovy.control.CompilerConfiguration

class ArduinoMLDSL {

    private GroovyShell shell
    private CompilerConfiguration configuration
    private ArduinoMLBinding binding
    private ArduinoMLBasescript basescript

    ArduinoMLDSL() {
        binding = new ArduinoMLBinding()
        binding.setModel(new ArduinoMLModel(binding));
        configuration = new CompilerConfiguration()
        configuration.setScriptBaseClass("fr.polytech.unice.si5.morseML.dsl.ArduinoMLBasescript")
        shell = new GroovyShell(configuration)
    }

    void eval(File scriptFile) {
        Script script = shell.parse(scriptFile)

        binding.setScript(script)
        script.setBinding(binding)

        script.run()
    }
}
