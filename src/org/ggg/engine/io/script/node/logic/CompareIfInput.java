package org.ggg.engine.io.script.node.logic;

import org.ggg.engine.io.script.node.logic.consts.VariableStorage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CompareIfInput {
    private static Map<String, String> input = new HashMap<>();

    public static boolean compareInput(String variable, String value){
        return VariableStorage.getVarValue(variable).equals(value);
    }

    public static void setValues(String variable, String value){
        input.put(variable, value);
    }

    public static String getValue(String variable){
        return input.get(variable);
    }

    public static String getVaraible(String value){
        return input.get(value);
    }
}
