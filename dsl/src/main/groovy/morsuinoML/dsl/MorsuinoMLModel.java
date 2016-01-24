package morsuinoML.dsl;

import groovy.lang.Binding;
import kernel.App;

import kernel.behavioral.State;
import kernel.generator.ToWiring;
import kernel.generator.Visitor;
import kernel.structural.*;

import java.util.ArrayList;
import java.util.List;


public class MorsuinoMLModel {
    private List<String> morseActuators = new ArrayList<>();
    private List<String> actuatorDeclarations = new ArrayList<>();
    private State initialState;
    private String messageToTranslate;

    private Binding binding;

    public MorsuinoMLModel(Binding binding) {
        this.binding = binding;
    }


    public void createMorse(String message, List<String> morseActuators) {
        this.morseActuators = morseActuators;
        //TODO: BIM
        this.messageToTranslate = message;
    }

    public void setInitialState(State state) {
        this.initialState = state;
    }

    @SuppressWarnings("rawtypes")
    public Object generateCode(String appName) {
        //MorsuinoML ne touche pas au code JAVA (kernel) parce qu'il ne veut pas générer ce que
        //ce code permet de générer. Il touche éventuellement à groovuinoML par contre. mais éventuellement.
        String groovuinoMLApp = "";
        //	App app = new App();
        //	app.setName(appName);
        System.out.println("in the generate code method");
        for (String actuatorDeclaration : actuatorDeclarations) {
            System.out.println("oh, theres a buzzer to add");
            groovuinoMLApp += "\n"+actuatorDeclaration;
        }
        //	app.setInitial(this.initialState);
        //	app.setMessageToTranslate(this.messageToTranslate);
        //app.setMorseActuators(this.morseActuators);
        //Visitor codeGenerator = new ToWiring();
        //app.accept(codeGenerator);

        //return codeGenerator.getResult();
        return groovuinoMLApp;
    }

    /**
     * This methods allows us to keep the actuator's name for future reference
     *
     * @param actuator
     */
    public void addActuator(String actuator) {
        morseActuators.add(actuator);
    }

    public void addBuzzerDeclaration(String name, Integer pinNumber) {
        actuatorDeclarations.add("buzzer \"" + name + "\" pin " + pinNumber);
    }

    public void addLedDeclaration(String name, Integer pinNumber) {
        actuatorDeclarations.add(("led \"" + name + "\" pin " + pinNumber));
    }
}
