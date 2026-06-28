package ch.fhnw.fitnesscounter.util;

import org.springframework.util.StringUtils;

public final class DataNormalizer {

    // Klasse soll nicht instanzierbar sein
    private DataNormalizer () {}

    /**
     *
     * Normalisiert den eingegebenen String
     *
     * @param input
     * @return
     */
    public static String normalizeString(String input) {
        if (input.isEmpty())
            return input;
        return StringUtils.capitalize(input.trim().toLowerCase());
    }
}
