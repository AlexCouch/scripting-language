package org.ggg.engine.utils;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumLoggerTypes;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public static List<String> complexSplit(String input, String...regex){
        List<String> splitStr = new ArrayList<>();
        String[] result = input.split(regex[0]);
        for(String s : result){
            for(int i = 0; i < regex.length; i++) {
                if(i >= regex.length+1) break;
                String[] res = s.split(regex[i]);
                for(String s1 : res) {

                    Engine.LOGGER.log(s1, EnumLoggerTypes.DEBUG);
                }
            }
        }
        return splitStr;
    }
}
