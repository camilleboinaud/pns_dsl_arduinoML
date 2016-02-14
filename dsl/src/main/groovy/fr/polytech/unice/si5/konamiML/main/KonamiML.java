package fr.polytech.unice.si5.konamiML.main;


import fr.polytech.unice.si5.konamiML.dsl.KonamiMLDSL;

import java.io.File;

/**
 * Created by user on 14/02/16.
 */
public class KonamiML {

    public static void  main(String[] args){
        KonamiMLDSL dsl = new KonamiMLDSL();
        if(args.length > 0) {
            dsl.eval(new File(args[0]));
        } else {
            System.out.println("/!\\ Missing arg: Please specify the path to a Groovy script file to execute");
        }
    }
}
