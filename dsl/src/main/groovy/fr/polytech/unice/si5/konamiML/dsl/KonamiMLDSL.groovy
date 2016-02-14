package fr.polytech.unice.si5.konamiML.dsl

import org.codehaus.groovy.control.CompilerConfiguration


/**
 * Created by user on 14/02/16.
 */
class KonamiMLDSL {
    private GroovyShell shell;
    private CompilerConfiguration configuration;
    private KonamiMLBinding binding;
    private KonamiMLBasescript basescript;

    KonamiMLDSL() {
        binding = new KonamiMLBinding();
        binding.setModel(new KonamiMLModel(binding));
        configuration = new CompilerConfiguration();
        configuration.setScriptBaseClass("fr.polytech.unice.si5.konamiML.dsl.KonamiMLBasescript");
        shell = new GroovyShell(configuration);
    }

    void eval(File scriptFile) {
        Script script = shell.parse(scriptFile);
        binding.setScript(script);
        script.setBinding(binding);
        script.run();
    }
}
