package fr.polytech.unice.si5.arduinoML.main;

import fr.polytech.unice.si5.arduinoML.dsl.ArduinoMLDSL;

import java.io.File;

/**
 * Created by cboinaud on 26/01/16.
 */
public class ArduinoML {

    public static void main(String[] args) {

        ArduinoMLDSL dsl = new ArduinoMLDSL();
        /*dsl.eval(new File("dsl/script/morseSOS3.groovy"));*/
        if(args.length > 0) {
            dsl.eval(new File(args[0]));
        } else {
            System.out.println("/!\\ Missing arg: Please specify the path to a Groovy script file to execute");
        }

    }

}
