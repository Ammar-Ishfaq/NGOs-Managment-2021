package com.ammar.fyp.Tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class validatorUtils {
    //    String regex = "^(.+)@(.+)$";
    String regex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public boolean isValidEmailId(String email) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
