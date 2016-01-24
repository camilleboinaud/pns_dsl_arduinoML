package kernel.generator;


import kernel.App;
import kernel.behavioral.*;
import kernel.structural.*;

/**
 * Quick and dirty visitor to support the generation of Wiring code
 */
public class ToWiring extends Visitor<StringBuffer> {

	private final static String CURRENT_STATE = "current_state";

	public ToWiring() {
		this.result = new StringBuffer();
	}

	private void w(String s) {
		result.append(String.format("%s\n",s));
	}

	@Override
	public void visit(App app) {
		w("// Wiring code generated from an ArduinoML model");
		w(String.format("// Application name: %s\n", app.getName()));

		w(String.format("char stringToMorseCode[] = \"%s\";\n", app.getMessageToTranslate()));
		w("void setup(){");
		for(Brick brick: app.getBricks()){
			brick.accept(this);
		}
		w("}\n");

		w("long time = 0; long debounce = 200;\n");

		for(State state: app.getStates()){
			state.accept(this);
		}

		w("void loop() {");
		if(app.isMorseMode()) {
			w("  for (int i = 0; i < sizeof(stringToMorseCode) - 1; i++)\n" +
					"  {\n" +
					"    // Get the character in the current position\n" +
					"\tchar tmpChar = stringToMorseCode[i];\n" +
					"\t// Set the case to lower case\n" +
					"\ttmpChar = toLowerCase(tmpChar);\n" +
					"\t// Call the subroutine to get the morse code equivalent for this character\n" +
					"\tGetChar(tmpChar);\n" +
					"  }");
		} else {
			w(String.format("  state_%s();", app.getInitial().getName()));
		}
		w("}");

		if(app.isMorseMode()) {
			this.generateMorseLanguage(app);
		}
	}

	@Override
	public void visit(Actuator actuator) {
	 	w(String.format("  pinMode(%d, OUTPUT); // %s [Actuator]", actuator.getPin(), actuator.getName()));
	}

	@Override
	public void visit(Sensor sensor) {
		w(String.format("  pinMode(%d, INPUT);  // %s [Sensor]", sensor.getPin(), sensor.getName()));
	}

	@Override
	public void visit(ConditionalTransition conditionalTransition) {
		w(String.format("   if("));
		conditionalTransition.getConditionalStatements().accept(this);
		w(String.format(" guard) {"));
		w("    time = millis();");
		w(String.format("    state_%s();",conditionalTransition.getNext().getName()));
		w("  } else {");
		w(String.format("    state_%s();", ((State) context.get(CURRENT_STATE)).getName()));
		w("  }");
	}

	@Override
	public void visit(TimerTransition timerTransition) {
		w(String.format("delay(%d);", timerTransition.getMoment().getAmount()));
		w(String.format("    state_%s();",timerTransition.getNext().getName()));
	}

	@Override
	public void visit(State state) {
		w(String.format("void state_%s() {",state.getName()));
		for(Action action: state.getActions()) {
			action.accept(this);
		}
		w("  boolean guard = millis() - time > debounce;");
		context.put(CURRENT_STATE, state);
		state.getTransition().accept(this);
		w("}\n");

	}

	@Override
	public void visit(Action action) {
		w(String.format("  digitalWrite(%d,%s);",action.getActuator().getPin(),action.getValue()));
	}

	@Override
	public void visit(ConditionalStatement conditionalStatement) {
		int i;
		for(i = 0; i < conditionalStatement.getSensor().size()-1; i++) {
			//If the value of the sensor we talk about
			//equals the value we want
			w(String.format("digitalRead(%d) == %s %s",
					conditionalStatement.getSensor().get(i).getPin(),conditionalStatement.getValue().get(i),
					conditionalStatement.getBooleanExpressions().get(i).getExpression()));

		}

		w(String.format("digitalRead(%d) == %s &&",
				conditionalStatement.getSensor().get(i).getPin(),conditionalStatement.getValue().get(i)));
	}

	private void generateMorseLanguage(App app) {
		w("int note = 1200;      // music note/pitch\n" +
				"int dotLen = 100;     // length of the morse code 'dot'\n" +
				"int dashLen = dotLen * 3;    // length of the morse code 'dash'\n" +
				"int elemPause = dotLen;  // length of the pause between elements of a character\n" +
				"int Spaces = dotLen * 3;     // length of the spaces between characters\n" +
				"int wordPause = dotLen * 7;  // length of the pause between words");

		w("void LightsOff(int delayTime)\n" +
				"{\n");
		for(Actuator actuator : app.getMorseActuators()) {
			String actuatorMethod = "";
			String optionnalParam = "";
			if (actuator instanceof Buzzer) {
				actuatorMethod = "noTone";
			} else if (actuator instanceof Led) {
				actuatorMethod = "digitalWrite";
				optionnalParam = ", LOW";

			}
			w(String.format("  	%s(%d%s);\t       \t   \t// stop playing a tone\n",
					actuatorMethod, actuator.getPin(), optionnalParam));
		}
			w("  	delay(delayTime);            \t// hold in this position\n" +
		"}");


		w("void MorseDash()\n" +
				"{\n");
		for(Actuator actuator : app.getMorseActuators()) {
			String actuatorMethod = "";
			String param = "";
			if (actuator instanceof Buzzer) {
				actuatorMethod = "tone";
				param = "note, dashLen";
			} else if (actuator instanceof Led) {
				actuatorMethod = "digitalWrite";
				param = "HIGH";
			}

			w(String.format("  	%s(%d, %s);\t// start playing a tone\n",
					actuatorMethod, actuator.getPin(), param));
		}
		w("  delay(dashLen);               // hold in this position\n" +
				"}");

		w("void MorseDot()\n" +
				"{\n");

		for(Actuator actuator : app.getMorseActuators()) {
			String actuatorMethod = "";
			String param = "";
			if (actuator instanceof Buzzer) {
				actuatorMethod = "tone";
				param = "note, dotLen";
			} else if (actuator instanceof Led) {
				actuatorMethod = "digitalWrite";
				param = "HIGH";
			}

			w(String.format("  	%s(%d, %s);\t// start playing a tone\n",
					actuatorMethod, actuator.getPin(), param));
		}
		w("  delay(dotLen);             \t// hold in this position\n" +
				"}");

		w(" // *** Characters to Morse Code Conversion *** //\n" +
				" void GetChar(char tmpChar) {\n" +
				"\t // Take the passed character and use a switch case to find the morse code for that character\n" +
				"\t switch (tmpChar) {\n" +
				"\t\t case 'a':\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'b':\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'c':\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'd':\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'e':\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'f':\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'g':\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'h':\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'i':\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'j':\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'k':\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'l':\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'm':\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'n':\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'o':\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'p':\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'q':\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'r':\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 's':\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 't':\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'u':\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'v':\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'w':\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'x':\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'y':\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t case 'z':\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDash();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t MorseDot();\n" +
				"\t\t\t LightsOff(elemPause);\n" +
				"\t\t\t break;\n" +
				"\t\t default:\n" +
				"\t\t // If a matching character was not found it will default to a blank space\n" +
				"\t\t LightsOff(Spaces);\n" +
				"\t }\n" +
				" }");

	}
}
