package com.spring.start.springProjekt.utilities;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppdemoUtils {
    public static boolean checkEmailOrPassword(String pattern, String pStr) {

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(pStr);
        return m.matches();
    }


    public static String randomStringGenerator() {
        String randomString = "";

        var signs = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        Random rnd = new Random();

        for (int i = 0; i < 32; i++) {
            var liczba = rnd.nextInt(signs.length());
            randomString += signs.substring(liczba, liczba + 1);
        }
        return randomString;
    }
}
