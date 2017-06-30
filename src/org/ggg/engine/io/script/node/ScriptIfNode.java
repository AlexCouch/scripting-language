package org.ggg.engine.io.script.node;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.io.script.ScriptLoader;

public class ScriptIfNode extends Node{

    public static final Node INSTANCE = new ScriptIfNode(Engine.getScriptLoader());

    public ScriptIfNode(ScriptLoader loader) {
        super(EnumNodes.IF, loader);
    }

    @Override
    public <T> boolean perform(T...params) {
        if(params[0] instanceof String){
            String condstr = (String)params[0];
            if(Boolean.parseBoolean(condstr)){
                return true;
            }else{
                return true;
            }
        }
        return false;
    }
}
