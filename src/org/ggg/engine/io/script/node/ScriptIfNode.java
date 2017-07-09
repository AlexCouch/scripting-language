package org.ggg.engine.io.script.node;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.io.script.ScriptLoader;
import org.ggg.engine.io.script.node.logic.CompareIfInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ScriptIfNode extends Node{

    public static final ScriptIfNode INSTANCE = new ScriptIfNode(Engine.getScriptLoader());

    public ScriptIfNode(ScriptLoader loader) {
        super(EnumNodes.IF, loader);
    }

    @Override
    public boolean perform(String...params) {
        Boolean bool = compInput(params[0], params[1], params[2]);
        if(bool){
            return true;
        }else{
            return false;
        }
    }

    private boolean compInput(String variable, String op, String value){
        return CompareIfInput.compareInput(variable, op, value);
    }
}
