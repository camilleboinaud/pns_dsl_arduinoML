package fr.polytech.unice.si5.arduinoML.kernel.generator;


import fr.polytech.unice.si5.arduinoML.kernel.Morse;
import fr.polytech.unice.si5.arduinoML.kernel.behavioral.*;
import fr.polytech.unice.si5.arduinoML.kernel.App;
import fr.polytech.unice.si5.arduinoML.kernel.structural.*;

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

		w("void setup(){");
		for(Brick brick: app.getBricks()){
			brick.accept(this);
		}
		w("}\n");

		w("long time = 0; long debounce = 200;\n");

		for(State state: app.getStates()){
			state.accept(this);
		}


		if(app.isMorseEnable()) {
			mappingMorseCode();
			encodingMorseCode();
			// Write morse code
			w("void loop() {");
			w(String.format("\tint UNIT_LENGTH = 300;"));
			w(String.format("\tString morseWord = encode(\"%s\");", app.getMorseCode()));
			generateMorseArduinoLoop(app);

		} else {
			w("void loop() {");
			w(String.format("  state_%s();", app.getInitial().getName()));
		}
		w("}");
	}

	@Override
	public void visit(DigitalActuator actuator) {
		w(String.format("  pinMode(%d, OUTPUT); // %s [DigitalActuator]", actuator.getPin(), actuator.getName()));
	}

	@Override
	public void visit(AnalogicalActuator actuator) {
		w(String.format("  pinMode(%d, OUTPUT); // %s [AnalogicalActuator]", actuator.getPin(), actuator.getName()));
	}

	@Override
	public void visit(DigitalSensor sensor) {
		w(String.format("  pinMode(%d, INPUT);  // %s [DigitalSensor]", sensor.getPin(), sensor.getName()));
	}

	@Override
	public void visit(AnalogicalSensor sensor) {
		w(String.format("  pinMode(%d, INPUT);  // %s [AnalogicalSensor]", sensor.getPin(), sensor.getName()));
	}



	@Override
	public void visit(SimpleExpression simpleExpression) {
		simpleExpression.getCondition().accept(this);
	}

	@Override
	public void visit(BooleanExpression booleanExpression) {
		booleanExpression.getLeft().accept(this);
		w(String.format(" %s ", booleanExpression.getBooleanOperator().getSymbol()));
		booleanExpression.getRight().accept(this);
	}

	@Override
	public void visit(DigitalCondition digitalCondition) {
		w(String.format("digitalRead(%d) == %s", digitalCondition.getSensor().getPin(), digitalCondition.getSignal().toString()));
	}

	@Override
	public void visit(AnalogicalCondition analogicalCondition) {
		w(String.format("analogRead(%d) %s %d", analogicalCondition.getSensor().getPin(),
				analogicalCondition.getOperator().getSymbol(), (int)analogicalCondition.getValueToCompare()));
	}

	@Override
	public void visit(Transition transition) {
		w(String.format("  if(("));
		transition.getExpression().accept(this);
		w(String.format(") && guard ) {"));

		w("    time = millis();");

		w(String.format("    state_%s();",transition.getNext().getName()));
		w("  } else {");
		w(String.format("    state_%s();",((State) context.get(CURRENT_STATE)).getName()));
		w("  }");
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
	public void visit(DigitalAction action) {
		w(String.format("  digitalWrite(%d,%s);",action.getActuator().getPin(), action.getValue()));
	}

	@Override
	public void visit(AnalogicalAction action) {
		w(String.format("  analogWrite(%d,%d);",action.getActuator().getPin(), (int) action.getValue()));
	}

	@Override
	public void visit(Morse morse){
		w(String.format("%s",morse.getCode().getContent()));

	}
	private void generateMorseArduinoLoop(App app) {
		w(String.format("\n\tfor(int i=0; i<=morseWord.length(); i++) {"));
		w(String.format("\t\tswitch(morseWord[i]) {"));
		w(String.format("\t\t\tcase '.' : //dot"));
		writeDigtalOutputs(app, SIGNAL.HIGH);
		w(String.format("\t\t\t\tdelay(UNIT_LENGTH);"));
		writeDigtalOutputs(app, SIGNAL.LOW);
		w(String.format("\t\t\t\tdelay(UNIT_LENGTH);"));
		w(String.format("\t\t\t\tbreak;\n"));
		w(String.format("\t\t\tcase '-': //dash"));
		writeDigtalOutputs(app, SIGNAL.HIGH);
		w(String.format("\t\t\t\tdelay(UNIT_LENGTH * 3);"));
		writeDigtalOutputs(app, SIGNAL.LOW);
		w(String.format("\t\t\t\tdelay(UNIT_LENGTH);"));
		w(String.format("\t\t\t\tbreak;\n"));
		w(String.format("\t\t\tcase '+': //Finish a letter"));
		writeDigtalOutputs(app, SIGNAL.LOW);
		w(String.format("\t\t\t\tdelay(UNIT_LENGTH);"));
		w(String.format("\t\t\t\tbreak;\n"));
		w(String.format("\t\t\tcase ' ' :"));
		w(String.format("\t\t\t\tdelay(UNIT_LENGTH);"));
		w(String.format("\t\t}"));
		w(String.format("\t}"));
	}

	private void writeDigtalOutputs(App app, SIGNAL signal) {
		for(Brick brick : app.getBricks()) {
			if(brick instanceof DigitalActuator) { // Output pins
				w(String.format("\t\t\t\tdigitalWrite(%d,%s);", brick.getPin() , signal.name()));
			}
		}
	}
	private void mappingMorseCode() {
		w(String.format("\n//Build a struct with the morse code mapping"));
		w(String.format("static const struct {"));
		w(String.format("\tconst char letter, *code;"));
		w(String.format("} MorseMap[] = {"));
		w(String.format("\t{ 'A', \".-+\" },"));
		w(String.format("\t{ 'B', \"-...+\" },"));
		w(String.format("\t{ 'C', \"-.-.+\" },"));
		w(String.format("\t{ 'D', \"-..+\" },"));
		w(String.format("\t{ 'E', \".+\" },"));
		w(String.format("\t{ 'F', \"..-.+\" },"));
		w(String.format("\t{ 'G', \"--.+\" },"));
		w(String.format("\t{ 'H', \"....+\" },"));
		w(String.format("\t{ 'I', \"..+\" },"));
		w(String.format("\t{ 'J', \".---+\" },"));
		w(String.format("\t{ 'K', \".-.-+\" },"));
		w(String.format("\t{ 'L', \".-..+\" },"));
		w(String.format("\t{ 'M', \"--+\" },"));
		w(String.format("\t{ 'N', \"-.+\" },"));
		w(String.format("\t{ 'O', \"---+\" },"));
		w(String.format("\t{ 'P', \".--.+\" },"));
		w(String.format("\t{ 'Q', \"--.-+\" },"));
		w(String.format("\t{ 'R', \".-.+\" },"));
		w(String.format("\t{ 'S', \"...+\" },"));
		w(String.format("\t{ 'T', \"-+\" },"));
		w(String.format("\t{ 'U', \"..-+\" },"));
		w(String.format("\t{ 'V', \"...-+\" },"));
		w(String.format("\t{ 'W', \".--+\" },"));
		w(String.format("\t{ 'X', \"-..-+\" },"));
		w(String.format("\t{ 'Y', \"-.--+\" },"));
		w(String.format("\t{ 'Z', \"--..+\" },"));
		w(String.format("\t{ ' ', \"     \" },"));
		w(String.format("\t{ '1', \".----+\" },"));
		w(String.format("\t{ '2', \"..---+\" },"));
		w(String.format("\t{ '3', \"...--+\" },"));
		w(String.format("\t{ '4', \"....-+\" },"));
		w(String.format("\t{ '5', \".....+\" },"));
		w(String.format("\t{ '6', \"-....+\" },"));
		w(String.format("\t{ '7', \"--...+\" },"));
		w(String.format("\t{ '8', \"---..+\" },"));
		w(String.format("\t{ '9', \"----.+\" },"));
		w(String.format("\t{ '0', \"-----+\" }"));
		w(String.format("};"));
	}

	private void encodingMorseCode() {
		w(String.format("\nString encode(const char *src) {"));
		w(String.format("\tsize_t i, j;"));
		w(String.format("\tString morseWord = \"\";\n"));
		w(String.format("\tfor( i = 0; src[i]; ++i ) {"));
		w(String.format("\t\tfor( j = 0; j < sizeof MorseMap / sizeof *MorseMap; ++j ) {"));
		w(String.format("\t\t\tif( toupper(src[i]) == MorseMap[j].letter ) {"));
		w(String.format("\t\t\t\tmorseWord += MorseMap[j].code;"));
		w(String.format("\t\t\t\tbreak;"));
		w(String.format("\t\t\t}"));
		w(String.format("\t\t}"));
		w(String.format("\t\tmorseWord += \" \";"));
		w(String.format("\t}\n"));
		w(String.format("\treturn morseWord;"));
		w(String.format("}\n"));
	}
}