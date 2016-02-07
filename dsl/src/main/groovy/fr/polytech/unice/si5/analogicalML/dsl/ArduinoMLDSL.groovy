package fr.polytech.unice.si5.analogicalML.dsl

import fr.polytech.unice.si5.kernel.behavioral.OPERATOR
import fr.polytech.unice.si5.kernel.structural.SIGNAL
import fr.polytech.unice.si5.analogicalML.utils.UnitEnum
import org.codehaus.groovy.control.CompilerConfiguration

/**
 * Created by cboinaud on 26/01/16.
 */
class ArduinoMLDSL {

    private GroovyShell shell
    private CompilerConfiguration configuration
    private ArduinoMLBinding binding
    private ArduinoMLBasescript basescript

    ArduinoMLDSL() {
        binding = new ArduinoMLBinding()
        binding.setModel(new ArduinoMLModel(binding));
        configuration = new CompilerConfiguration()
        configuration.setScriptBaseClass("fr.polytech.unice.si5.analogicalML.dsl.ArduinoMLBasescript")
        shell = new GroovyShell(configuration)

        binding.setVariable("high", SIGNAL.HIGH)
        binding.setVariable("low", SIGNAL.LOW)

        binding.setVariable("deg", UnitEnum.CELSIUS_DEGREE)
        binding.setVariable("V", UnitEnum.VOLT)

        binding.setVariable("greater_than", OPERATOR.GT)
        binding.setVariable("equal_to", OPERATOR.EQ)
        binding.setVariable("not_equal_to", OPERATOR.NE)
        binding.setVariable("greater_or_equal_to", OPERATOR.GE)
        binding.setVariable("lower_than", OPERATOR.LT)
        binding.setVariable("lower_or_equal_to", OPERATOR.LE)
        binding.setVariable("degrees", UnitEnum.CELSIUS_DEGREE)
        binding.setVariable("volts", UnitEnum.VOLT)
    }

    void eval(File scriptFile) {
        Script script = shell.parse(scriptFile)

        binding.setScript(script)
        script.setBinding(binding)

        script.run()
    }
}
