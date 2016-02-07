package fr.polytech.unice.si5.analogicalML.dsl;

import groovy.lang.Binding;
import groovy.lang.Script;

import java.util.Map;

/**
 * Created by cboinaud on 26/01/16.
 */
public class ArduinoMLBinding extends Binding {

    private Script script;
    private ArduinoMLModel model;

    public ArduinoMLBinding(){
        super();
    }

    @SuppressWarnings("rawtypes")
    public ArduinoMLBinding(Map variables){
        super(variables);
    }

    public ArduinoMLBinding(Script script){
        super();
        this.script = script;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public ArduinoMLModel getModel() {
        return model;
    }

    public void setModel(ArduinoMLModel model) {
        this.model = model;
    }

    public void setVariable(String name, Object object){
        super.setVariable(name, object);
    }

    public Object getVariable(String name){
        //TODO
        return super.getVariable(name);
    }

}
