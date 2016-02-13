package fr.polytech.unice.si5.morseML.utils;

import fr.polytech.unice.si5.kernel.structural.MORSESIGNAL;

import java.util.HashMap;
import java.util.Map;

public class MorseMapper {
    private static final Map<String, String> mappers = new HashMap<>();
    static {
        mappers.put("A", ".-+");
        mappers.put("B", "-...+");
        mappers.put("C", "-.-.+");
        mappers.put("D", "-..+");
        mappers.put("E", ".+");
        mappers.put("F", "..-.+");
        mappers.put("G", "--.+");
        mappers.put("H", "....+");
        mappers.put("I", "..+");
        mappers.put("J", ".---+");
        mappers.put("K", ".-.-+");
        mappers.put("L", ".-..+");
        mappers.put("M", "--+");
        mappers.put("N", "-.+");
        mappers.put("O", "---+");
        mappers.put("P", ".--.+");
        mappers.put("Q", "--.-+");
        mappers.put("R", ".-.+");
        mappers.put("S", "...+");
        mappers.put("T", "-+");
        mappers.put("U", "..-+");
        mappers.put("V", "...-+");
        mappers.put("W", ".--+");
        mappers.put("X", "-..-+");
        mappers.put("Y", "-.--+");
        mappers.put("Z", "--..+");
        mappers.put("0", "-----+");
        mappers.put("1", ".----+");
        mappers.put("2", "..---+");
        mappers.put("3", "...--+");
        mappers.put("4", "....-+");
        mappers.put("5", ".....+");
        mappers.put("6", "-....+");
        mappers.put("7", "--...+");
        mappers.put("8", "---..+");
        mappers.put("9", "----.+");
        mappers.put(" ", "     ");

    }
    public static String getMosreMapper(String letter) {
        return mappers.get(letter);
    }
}
