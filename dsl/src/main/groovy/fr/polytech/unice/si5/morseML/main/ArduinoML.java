package fr.polytech.unice.si5.morseML.main;

import fr.polytech.unice.si5.morseML.dsl.ArduinoMLDSL;

import java.io.File;

public class ArduinoML {

    public static void main(String[] args) {

        ArduinoMLDSL dsl = new ArduinoMLDSL();
        if(args.length > 0) {
            dsl.eval(new File(args[0]));
        } else {
            System.out.println("/!\\ Missing arg: Please specify the path to a Groovy script file to execute");
        }

    }

}
