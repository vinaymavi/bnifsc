package util;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by vinaymavi on 19/12/15.
 */
public class Word {
    /**
     * Convert every string to capitalize.
     *
     * @param word String
     * @return String
     */
    public static String capitalize(String word) {
        if (word == null) {
            return null;
        } else {
            return WordUtils.capitalize(word.toLowerCase()).trim();
        }

    }
}
