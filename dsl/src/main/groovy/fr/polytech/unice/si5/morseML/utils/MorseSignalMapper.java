package fr.polytech.unice.si5.morseML.utils;

import fr.polytech.unice.si5.kernel.structural.MORSESIGNAL;

import java.util.HashMap;
import java.util.Map;

public class MorseSignalMapper {
    private static final Map<String, MORSESIGNAL> morseSignalMapper = new HashMap<>();

    static {
        morseSignalMapper.put(".", MORSESIGNAL.SHORT);
        morseSignalMapper.put("-", MORSESIGNAL.LONG);
        morseSignalMapper.put("+", MORSESIGNAL.STOP);
        morseSignalMapper.put(" ", MORSESIGNAL.ESPACE);
        morseSignalMapper.put("|", MORSESIGNAL.END);
    }

    public static MORSESIGNAL getMoserSignal(String morse) {
        return morseSignalMapper.get(morse);
    }
}
