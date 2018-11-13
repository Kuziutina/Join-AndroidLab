package ru.kpfu.itis.androidlab.Join.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generator {
    private static final String DIGITS = "0123456789";

    public static String generate(int length) {
        if (length <= 0) {
            return "";
        }

        StringBuilder result = new StringBuilder(length);
        Random random = new Random(System.nanoTime());

        List<String> charCategories = new ArrayList<>();
        charCategories.add(DIGITS);

        for (int i = 0; i < length; i++) {
            String charCategory = charCategories.get(random.nextInt(charCategories.size()));
            int position = random.nextInt(charCategory.length());
            result.append(charCategory.charAt(position));
        }
        return new String(result);
    }
}
