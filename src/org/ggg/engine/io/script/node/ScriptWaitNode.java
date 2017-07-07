package org.ggg.engine.io.script.node;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumLoggerTypes;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.io.script.ScriptLoader;

import java.util.*;

public class ScriptWaitNode extends Node{

    public Map<String, String> inputMap = new HashMap<>();

    public static final ScriptWaitNode INSTANCE = new ScriptWaitNode(Engine.getScriptLoader());

    public boolean isInt = false;

    public ScriptWaitNode(ScriptLoader loader) {
        super(EnumNodes.WAIT, loader);
    }

    @Override
    public boolean perform(String... params) {
        Scanner scanner = new Scanner(System.in);

        if(isInt(params[0])){
            int time = Integer.valueOf(params[0]);
            isInt = true;
            try {
                Thread.sleep(time * 1000);
                return true;
            } catch(InterruptedException e) {
                Engine.LOGGER.log(e.getMessage(), EnumLoggerTypes.ERROR);
                return false;
            }
        }else {
            isInt = false;
            while (scanner.hasNextLine()) {
                inputMap.put(params[0], scanner.nextLine());
                if (inputMap.size() > 0) {
                    break;
                }
            }
            return true;
        }
    }
    private static boolean isInt(String string) {
    	if(string.isEmpty()) {
    		return false;
    	}
    	for(int i = 0; i < string.length(); i++) {
    		if(i == 0 && string.charAt(i) == '-') {
    			if(string.length() == 1) {
    				return false;
    			}
    			else {
    				continue;
    			}
    		}
    		if(Character.digit(string.charAt(i), 10) < 0) {
    			return false;
    		}
    	}
    	return true;
    }
}
