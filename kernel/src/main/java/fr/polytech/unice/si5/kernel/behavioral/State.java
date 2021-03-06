package fr.polytech.unice.si5.kernel.behavioral;


import fr.polytech.unice.si5.kernel.generator.Visitable;
import fr.polytech.unice.si5.kernel.generator.Visitor;
import fr.polytech.unice.si5.kernel.NamedElement;

import java.util.ArrayList;
import java.util.List;

public class State implements NamedElement, Visitable {

	private String name;
	private List<Action> actions = new ArrayList<>();
	private List<Transition> transition = new ArrayList<>();

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public List<Transition> getTransition() {
		return transition;
	}

	public void setTransition(List<Transition> transition) {
		this.transition = transition;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
