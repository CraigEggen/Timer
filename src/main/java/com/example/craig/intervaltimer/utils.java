package com.example.craig.intervaltimer;

import java.lang.reflect.Array;
import java.security.InvalidParameterException;

/**
 * Created by Craig on 9/4/2016.
 */
public class utils {
    public static int ParseToSeconds(String inputString){

        String[] splitString = inputString.split(":");

        if (Array.getLength(splitString) == 1){
            //if time is only in seconds
            return Integer.parseInt(splitString[0]);
        }
        if (Array.getLength(splitString) == 2){
            //if time has minutes and seconds
            Integer timeInSeconds = Integer.parseInt(splitString[0]) * 60;
            timeInSeconds += Integer.parseInt(splitString[1]);
            return timeInSeconds;
        }
        else {
            System.out.println("Error: expected a time as input");
            throw new InvalidParameterException();
        }

    }
}
