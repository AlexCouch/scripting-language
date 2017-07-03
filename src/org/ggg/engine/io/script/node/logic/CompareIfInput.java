package org.ggg.engine.io.script.node.logic;

import org.ggg.engine.io.script.node.logic.consts.VariableStorage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This is used to create conditional logic within the scripts. There is not much to document as it is not finished.
 * @author Alex Couch
 * @since 0.1.0
 */
public class CompareIfInput {
    private static Map<String, String> input = new HashMap<>();

    public static boolean compareInput(String variable, String value){
    	return VariableStorage.getVars(variable).equals(value);
    }

    public static void setValues(String variable, String value){
        input.put(variable, value);
    }

    public static String getValue(String variable){
        return input.get(variable);
    }

    public static String getVariable(String value){
        return input.get(value);
    }
}
