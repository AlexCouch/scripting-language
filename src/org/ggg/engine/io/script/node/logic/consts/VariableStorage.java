package org.ggg.engine.io.script.node.logic.consts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This is the class that will store variables with a name and a value, and this way it can be manipulated or used
 * at will.
 * @author Alex Couch
 * @since 0.1.0
 */
public class VariableStorage {
    private static Map<String, String> variables = new HashMap<>();

    public static void setVars(String name, String value){
        variables.put(name, value);
    }

    public static String getVars(String value){
        return variables.get(value);
    }

    public static String getVarValue(String varName){
        return variables.get(varName);
    }
}
