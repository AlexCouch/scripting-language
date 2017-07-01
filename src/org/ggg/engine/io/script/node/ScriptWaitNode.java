package org.ggg.engine.io.script.node;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.io.script.ScriptLoader;

import java.util.*;

public class ScriptWaitNode extends Node{

    public Map<String, String> inputMap = new HashMap<>();

    public static final ScriptWaitNode INSTANCE = new ScriptWaitNode(Engine.getScriptLoader());

    public ScriptWaitNode(ScriptLoader loader) {
        super(EnumNodes.WAIT, loader);
    }

    @Override
    public boolean perform(String... params) {
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            inputMap.put(params[0], scanner.nextLine());
            if(inputMap.size() > 0){
                break;
            }
        }
        return true;
    }
}
