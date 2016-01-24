package morsuinoML.main;


import morsuinoML.dsl.MorsuinoMLDSL;

import java.io.File;

/**
 * This main takes one argument: tht path to the Groovy script file to execute.
 * This Groovy script file must follow MorsuinoML DSL's rules.
 * 
 * "We've Got A Groovy Thing Goin'"!
 * 
 * @author Thomas Moreau
 */
public class MorsuinoML {
	public static void main(String[] args) {
		MorsuinoMLDSL dsl = new MorsuinoMLDSL();
	//	if(args.length > 0) {
			//dsl.eval(new File(args[0]));
	//		dsl.eval(new File("groovy-dsl/scripts/DualCheckAlarm.groovy"));
			dsl.eval(new File("dsl/scripts/MessageInMorse.groovy"));
	//	} else {
	//		System.out.println("/!\\ Missing arg: Please specify the path to a Groovy script file to execute");
	//	}
	}
}
