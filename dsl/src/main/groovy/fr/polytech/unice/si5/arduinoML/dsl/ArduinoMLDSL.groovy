package fr.polytech.unice.si5.arduinoML.dsl

import fr.polytech.unice.si5.arduinoML.kernel.behavioral.BOOLEAN
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.OPERATOR
import fr.polytech.unice.si5.arduinoML.kernel.structural.SIGNAL
import fr.polytech.unice.si5.arduinoML.utils.BrickType
import fr.polytech.unice.si5.arduinoML.utils.UnitEnum
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
        configuration.setScriptBaseClass("fr.polytech.unice.si5.arduinoML.dsl.ArduinoMLBasescript")
        shell = new GroovyShell(configuration)

        binding.setVariable("high", SIGNAL.HIGH)
        binding.setVariable("low", SIGNAL.LOW)

        binding.setVariable("deg", UnitEnum.CELSIUS_DEGREE)
        binding.setVariable("V", UnitEnum.VOLT)

        binding.setVariable("greater", OPERATOR.GT)
        binding.setVariable("equal to", OPERATOR.EQ)
        binding.setVariable("different from", OPERATOR.NE)
        binding.setVariable("greater or equal to", OPERATOR.GE)
        binding.setVariable("lower than", OPERATOR.LT)
        binding.setVariable("lower or equal to", OPERATOR.LE)

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
