package fr.polytech.unice.si5.konamiML.utils;

import fr.polytech.unice.si5.kernel.structural.DIRECTION;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 14/02/16.
 */
public class KonamiMapper {
    private static final Map<String, DIRECTION> konamiMapper = new HashMap<>() ;

    static{
        konamiMapper.put("U", DIRECTION.UP);
        konamiMapper.put("D", DIRECTION.DOWN);
        konamiMapper.put("L", DIRECTION.LEFT);
        konamiMapper.put("R", DIRECTION.RIGHT);
    }

    public static DIRECTION getDirection(String dir){
        return konamiMapper.get(dir);
    }
}
