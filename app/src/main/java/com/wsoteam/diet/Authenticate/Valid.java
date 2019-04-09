package com.wsoteam.diet.Authenticate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Valid {

    public static boolean isValidEmail(String string) {
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public static boolean isValidPhone(String string){
        final  String PHONE_PATTERN = "^[+][0-9]{10,13}$";
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public static boolean isValidCode(String string){
        final  String CODE_PATTERN = "^[0-9]{6}$";
        Pattern pattern = Pattern.compile(CODE_PATTERN);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
}
