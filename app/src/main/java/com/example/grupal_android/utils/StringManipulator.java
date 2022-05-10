package com.example.grupal_android.utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Clase preparada para manipular Strings.
 */
public class StringManipulator {

    public static String convertToTitleCase(String text) {
        String result = text;

        if ( text != null && !text.isEmpty() ) {
            result = StringManipulator.convertToTitleCaseIteratingChars(text);
        }

        return result;
    }

    private static String convertToTitleCaseIteratingChars(String nonEmptyText) {
        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : nonEmptyText.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }

    public static ArrayList<String> getArrayListFromStringifiedArray(String stringifiedArray) {
        // Quitando las llaves cuadradas del principio y del final
        String joinedMinusBrackets = stringifiedArray.substring( 1, stringifiedArray.length() - 1);
        // Separando los elementos
        String[] list = joinedMinusBrackets.split( ", ");
        // Devolviendo la lista como ArrayList
        return new ArrayList<>(Arrays.asList(list));
    }
}
