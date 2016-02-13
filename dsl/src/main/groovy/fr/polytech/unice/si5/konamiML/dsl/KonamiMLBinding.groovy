package main.groovy.fr.polytech.unice.si5.konamiML.dsl

/**
 * Created by user on 09/02/16.
 */
class KonamiMLBinding extends Binding{
    private Script script;
    private KonamiMLModel model;

    public KonamiMLBinding(){
        super();
    }

    @SuppressWarnings("rawtypes")
    public KonamiMLBinding(Map variables){
        super(variables);
    }

    public KonamiMLBinding(Script script){
        super();
        this.script = script;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public KonamiMLModel getModel() {
        return model;
    }

    public void setModel(KonamiMLModel model) {
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
