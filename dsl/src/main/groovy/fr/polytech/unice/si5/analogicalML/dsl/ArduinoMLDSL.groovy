package fr.polytech.unice.si5.analogicalML.dsl

import fr.polytech.unice.si5.analogicalML.utils.TypedValue
import fr.polytech.unice.si5.kernel.behavioral.OPERATOR
import fr.polytech.unice.si5.kernel.structural.SIGNAL
import fr.polytech.unice.si5.analogicalML.utils.LinearUnitEnum
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

        binding.setVariable("greater_than", OPERATOR.GT)
        binding.setVariable("equal_to", OPERATOR.EQ)
        binding.setVariable("not_equal_to", OPERATOR.NE)
        binding.setVariable("greater_or_equal_to", OPERATOR.GE)
        binding.setVariable("lower_than", OPERATOR.LT)
        binding.setVariable("lower_or_equal_to", OPERATOR.LE)

        ExpandoMetaClass.enableGlobally();

        Number.metaClass.getDeg = {-> (new TypedValue(delegate, LinearUnitEnum.degrees)).mVoltConvertion()}
        Number.metaClass.getV = {-> (new TypedValue(delegate, LinearUnitEnum.volts)).mVoltConvertion()}
        Number.metaClass.getMv = {-> (new TypedValue(delegate, LinearUnitEnum.milivolts)).mVoltConvertion()}
        Number.metaClass.getKv = {-> (new TypedValue(delegate, LinearUnitEnum.kiloVolts)).mVoltConvertion()}
    }

    void eval(File scriptFile) {
        Script script = shell.parse(scriptFile)

        binding.setScript(script)
        script.setBinding(binding)

        script.run()
    }
}
