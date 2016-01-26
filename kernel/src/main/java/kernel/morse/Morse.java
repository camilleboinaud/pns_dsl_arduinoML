package kernel.morse;

import kernel.NamedElement;
import kernel.generator.Visitable;
import kernel.generator.Visitor;

/**
 * Created by user on 26/01/16.
 */
public class Morse implements NamedElement, Visitable {
    private  String name;

    @Override
    public String getName(){
        return this.name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }


}
