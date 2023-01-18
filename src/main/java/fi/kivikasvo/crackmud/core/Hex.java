package fi.kivikasvo.crackmud.core;

import java.util.Random;

// Utility class
public class Hex {

    private static Random RANDOM = new Random();
    private static final char[] SYMBOLS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static char randomSymbol() {
        return SYMBOLS[RANDOM.nextInt(SYMBOLS.length)];
    }

    public static String gen() {
        String hex = "";
        for (int i = 0; i < 6; i++) {
            hex += randomSymbol();
        }
        return hex;
    }

}
