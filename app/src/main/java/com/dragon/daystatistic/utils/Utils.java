package com.dragon.daystatistic.utils;

import java.util.regex.Pattern;

/**
 * Created by Phoenix on 2014/12/18.
 */
public class Utils {
    public static boolean isNumeric(String str){
        if (str.length() == 0)
        {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]+\\.{0,1}[0-9]*");
        return pattern.matcher(str).matches();
    }


}
